package heladeria.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Operaciones de acceso a datos para la autenticación de usuarios.
 */
public class UsuarioDAO {
    
    /**
     * Verifica si el usuario y la contraseña coinciden en la base de datos.
     * 
     * @param username El nombre de usuario a validar
     * @param password La contraseña del usuario
     * @return true si las credenciales son correctas, false si no lo son
     */
    public boolean validarUsuario(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}