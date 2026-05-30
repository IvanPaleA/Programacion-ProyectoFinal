package heladeria.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión centralizada a MySQL.
 */
public class ConexionBD {
    private static Connection conexion = null;
    private static final String URL = "jdbc:mysql://localhost:3306/heladeria";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Goku2004";

    public static Connection getConexion() {
    try {
        if (conexion == null || conexion.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("¡Conexión exitosa con la base de datos de la Heladería!");
        }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}