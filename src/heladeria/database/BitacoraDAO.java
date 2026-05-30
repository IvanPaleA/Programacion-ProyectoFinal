package heladeria.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de consultar el historial de eliminaciones para auditoría.
 */
public class BitacoraDAO {

    /**
     * Obtiene una lista con todos los registros de la bitácora de eliminación de la base de datos.
     * 
     * @return Una lista de arreglos de texto con los datos de la bitácora
     */
    public List<String[]> listarBitacora() {
        List<String[]> lista = new ArrayList<>();
        // Consulta SQL para traer las columnas exactas que pide el prototipo
        String sql = "SELECT clave_producto, nombre_producto, razon_eliminacion, "
                + "fecha_eliminacion, usuario_sistema FROM bitacora_eliminacion "
                + "ORDER BY fecha_eliminacion DESC";
        
        Connection con = ConexionBD.getConexion();
        
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
             
            while (rs.next()) {
                //Se empaquita cada fila como un arreglo de textos
                lista.add(new String[]{
                    rs.getString("clave_producto"),
                    rs.getString("nombre_producto"),
                    rs.getString("razon_eliminacion"),
                    rs.getString("fecha_eliminacion"),
                    rs.getString("usuario_sistema")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar la bitácora: " + e.getMessage());
        }
        return lista;
    }
}