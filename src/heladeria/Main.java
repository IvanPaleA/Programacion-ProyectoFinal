package heladeria;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import heladeria.view.LoginView;

/**
 * Clase de entrada principal (Main Class) para el sistema Luxe Gelato.
 * Se encarga de inicializar el entorno y lanzar el control de acceso.
 */
public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo aplicar el Look and Feel del sistema.");
        }

        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setLocationRelativeTo(null); 
            login.setVisible(true); 
        });
    }
}
