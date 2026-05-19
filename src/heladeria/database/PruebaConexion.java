package heladeria.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Clase temporal para verificar la comunicación con MySQL.
 */
public class PruebaConexion {
    public static void main(String[] args) {
        System.out.println("=== Iniciando prueba de conexión ===");
        
        // 1. Intentar obtener la conexión Singleton
        Connection con = ConexionBD.getConexion();
        
        if (con != null) {
            System.out.println("-> Conexión física verificada correctamente.");
            
            // 2. Intentar una consulta rápida a la tabla de usuarios para certificar la lectura
            String sql = "SELECT username FROM usuarios";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                
                System.out.println("-> Consulta de prueba ejecutada con éxito.");
                while (rs.next()) {
                    System.out.println("   Usuario encontrado en la BD: " + rs.getString("username"));
                }
                System.out.println("=== PRUEBA COMPLETADA CON ÉXITO ===");
                
            } catch (Exception e) {
                System.err.println("X Error al consultar las tablas: " + e.getMessage());
            }
        } else {
            System.err.println("X Falló la conexión. Verifica que tu servidor MySQL esté encendido.");
        }
    }
}