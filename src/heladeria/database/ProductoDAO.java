package heladeria.database;

import heladeria.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Administra las transacciones SQL de los productos y la bitácora.
 */
public class ProductoDAO {

    // registrar un producto en la BD
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (clave, nombre, existencia, ubicacion, precio, foto) VALUES (?, ?, ?, ?, ?, ?)";
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
        Connection con = ConexionBD.getConexion(); 

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

    // Modificar un producto )
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

    public boolean eliminar(String clave, String razon, String usuarioActivo) {
        String buscarSql = "SELECT * FROM productos WHERE clave = ?";
        String bitacoraSql = "INSERT INTO bitacora_eliminacion (clave_producto, nombre_producto, razon_eliminacion, fecha_eliminacion, usuario_sistema) VALUES (?, ?, ?, NOW(), ?)";
        String eliminarSql = "DELETE FROM productos WHERE clave = ?";
        
        Connection con = null;
        try {
            con = ConexionBD.getConexion();
            con.setAutoCommit(false); 

            String nombreProducto = "";
            try (PreparedStatement psBuscar = con.prepareStatement(buscarSql)) {
                psBuscar.setString(1, clave);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        nombreProducto = rs.getString("nombre");
                    } else {
                        return false; 
                    }
                }
            }

            try (PreparedStatement psBitacora = con.prepareStatement(bitacoraSql)) {
                psBitacora.setString(1, clave);
                psBitacora.setString(2, nombreProducto);
                psBitacora.setString(3, razon);
                psBitacora.setString(4, usuarioActivo);
                psBitacora.executeUpdate();
            }

            int filasAfectadas;
            try (PreparedStatement psEliminar = con.prepareStatement(eliminarSql)) {
                psEliminar.setString(1, clave);
                filasAfectadas = psEliminar.executeUpdate();
            }

            con.commit(); 
            return filasAfectadas > 0;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { System.err.println(ex.getMessage()); }
            }
            System.err.println("Error en el proceso de eliminación: " + e.getMessage());
            return false;
        }
    }
    
    // Método que elimina un producto y guarda el registro en la bitácora de forma segura
    public boolean eliminarConBitacora(Producto p, String razon, String usuario) {
        String sqlBitacora = "INSERT INTO bitacora_eliminacion (clave_producto, nombre_producto, razon_eliminacion, fecha_eliminacion, usuario_sistema) VALUES (?, ?, ?, NOW(), ?)";
        String sqlEliminar = "DELETE FROM productos WHERE clave = ?";

        java.sql.Connection con = ConexionBD.getConexion();
        try {
            // Pausamos el autoguardado para asegurar que ambas cosas pasen juntas (Transacción)
            con.setAutoCommit(false); 
            
            // 1. Guardar la razón en la bitácora
            try (java.sql.PreparedStatement psBit = con.prepareStatement(sqlBitacora)) {
                psBit.setString(1, p.getClave());
                psBit.setString(2, p.getNombre());
                psBit.setString(3, razon);
                psBit.setString(4, usuario);
                psBit.executeUpdate();
            }
            
            // 2. Eliminar el helado del inventario
            try (java.sql.PreparedStatement psEli = con.prepareStatement(sqlEliminar)) {
                psEli.setString(1, p.getClave());
                psEli.executeUpdate();
            }
            
            con.commit(); // Confirmamos los cambios
            return true;
        } catch (java.sql.SQLException e) {
            try { con.rollback(); } catch (java.sql.SQLException ex) {} // Si algo falla, cancelamos todo para no dañar la BD
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (java.sql.SQLException ex) {}
        }
    }
    
    // Actualizar un producto existente en la BD
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, existencia = ?, ubicacion = ?, precio = ?, foto = ? WHERE clave = ?";
        java.sql.Connection con = ConexionBD.getConexion();
        
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getExistencia());
            ps.setString(3, p.getUbicacion());
            ps.setDouble(4, p.getPrecio());
            
            // Si la foto no es nula, la actualizamos.
            if (p.getFoto() != null) {
                ps.setBytes(5, p.getFoto());
            } else {
                ps.setNull(5, java.sql.Types.BLOB);
            }
            
            ps.setString(6, p.getClave()); // La clave va al final para el WHERE
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
}