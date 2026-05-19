package heladeria.database;

import heladeria.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Administra las transacciones SQL de los productos y la bitácora.
 */
public class ProductoDAO {

    // Registrar un producto en la BD
    // Registrar un producto en la BD
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (clave, nombre, existencia, ubicacion, precio, foto) VALUES (?, ?, ?, ?, ?, ?)";
        // Sacamos la conexion del try() para que no se cierre de forma automatica
        Connection con = ConexionBD.getConexion(); 

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getClave());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getExistencia());
            ps.setString(4, p.getUbicacion());
            ps.setDouble(5, p.getPrecio());

            if (p.getFoto() != null) {
                ps.setBytes(6, p.getFoto());
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // Listar todos los productos (se usa para poblar el ABB al iniciar)
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        Connection con = ConexionBD.getConexion(); // Sacada del try()

        try (Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Producto(
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    rs.getInt("existencia"),
                    rs.getString("ubicacion"),
                    rs.getDouble("precio"),
                    rs.getBytes("foto")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // Modificar un producto (Sin incluir la clave, que es inmutable)
    public boolean editar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, existencia = ?, ubicacion = ?, precio = ?, foto = ? WHERE clave = ?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getExistencia());
            ps.setString(3, p.getUbicacion());
            ps.setDouble(4, p.getPrecio());
            ps.setBytes(5, p.getFoto());
            ps.setString(6, p.getClave());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar producto: " + e.getMessage());
            return false;
        }
    }

    // Eliminar producto por clave guardando registro previo en Bitácora
    public boolean eliminar(String clave, String razon, String usuarioActivo) {
        String buscarSql = "SELECT * FROM productos WHERE clave = ?";
        String bitacoraSql = "INSERT INTO bitacora_eliminacion (clave_producto, nombre_producto, razon_eliminacion, fecha_eliminacion, usuario_sistema) VALUES (?, ?, ?, NOW(), ?)";
        String eliminarSql = "DELETE FROM productos WHERE clave = ?";
        
        Connection con = null;
        try {
            con = ConexionBD.getConexion();
            con.setAutoCommit(false); // Iniciamos una transacción para asegurar consistencia

            // 1. Conseguir datos del producto antes de borrarlo
            String nombreProducto = "";
            try (PreparedStatement psBuscar = con.prepareStatement(buscarSql)) {
                psBuscar.setString(1, clave);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        nombreProducto = rs.getString("nombre");
                    } else {
                        return false; // El producto no existe
                    }
                }
            }

            // 2. Insertar los datos en la tabla de bitácora
            try (PreparedStatement psBitacora = con.prepareStatement(bitacoraSql)) {
                psBitacora.setString(1, clave);
                psBitacora.setString(2, nombreProducto);
                psBitacora.setString(3, razon);
                psBitacora.setString(4, usuarioActivo);
                psBitacora.executeUpdate();
            }

            // 3. Eliminar el producto del inventario
            int filasAfectadas;
            try (PreparedStatement psEliminar = con.prepareStatement(eliminarSql)) {
                psEliminar.setString(1, clave);
                filasAfectadas = psEliminar.executeUpdate();
            }

            con.commit(); // Confirmamos la transacción completa
            return filasAfectadas > 0;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { System.err.println(ex.getMessage()); }
            }
            System.err.println("Error en el proceso de eliminación: " + e.getMessage());
            return false;
        }
    }
}