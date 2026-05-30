package heladeria.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que maneja las operaciones de base de datos para las ventas.
 */
public class VentaDAO {

    // Registra la venta completa: ticket, detalles y actualización de inventario
    /**
     * Registra una venta completa en la base de datos, incluyendo el ticket, los detalles y actualizando el inventario.
     * 
     * @param total El total a pagar de la venta
     * @param usuario El nombre del usuario que realiza la venta
     * @param carrito El modelo de la tabla que contiene los productos de la venta
     * @return true si la venta se registró correctamente, false si hubo un error
     */
    public boolean registrarVentaCompleta(double total, String usuario, DefaultTableModel carrito) {
        String sqlVenta = "INSERT INTO ventas (fecha_venta, total, usuario_vendedor) VALUES (NOW(), ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, clave_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarStock = "UPDATE productos SET existencia = existencia - ? WHERE clave = ?";

        Connection con = ConexionBD.getConexion();
        
        try {
            // Iniciamos la transacción (pausamos el autoguardado)
            con.setAutoCommit(false);

            // 1. Insertar el ticket en la tabla 'ventas' y obtener su ID generado
            int idVentaGenerado = 0;
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setDouble(1, total);
                psVenta.setString(2, usuario);
                psVenta.executeUpdate();
                
                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        idVentaGenerado = rs.getInt(1);
                    }
                }
            }

            // 2. Recorrer el carrito para guardar los detalles y descontar el stock
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                
                for (int i = 0; i < carrito.getRowCount(); i++) {
                    String clave = carrito.getValueAt(i, 0).toString();
                    int cantidad = Integer.parseInt(carrito.getValueAt(i, 2).toString());
                    double importe = Double.parseDouble(carrito.getValueAt(i, 3).toString().replace("$", ""));
                    double precioUnitario = importe / cantidad;

                    // Preparar el INSERT del detalle
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setString(2, clave);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setDouble(4, precioUnitario);
                    psDetalle.setDouble(5, importe);
                    psDetalle.addBatch(); // Se agrega a un lote de tareas

                    // Preparar el UPDATE del inventario
                    psStock.setInt(1, cantidad);
                    psStock.setString(2, clave);
                    psStock.addBatch(); // Se agrega a un lote de tareas
                }
                
                // Ejecutamos todos los inserts y updates de golpe
                psDetalle.executeBatch();
                psStock.executeBatch();
            }

            // 3. Si todo salió perfecto, confirmamos los cambios en MySQL
            con.commit();
            return true;

        } catch (SQLException e) {
            try { 
                con.rollback(); // Si algo falló (ej. se fue la luz), deshacemos todo
            } catch (SQLException ex) {}
            System.err.println("Error en la transacción de venta: " + e.getMessage());
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
}