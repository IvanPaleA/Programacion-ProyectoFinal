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
        // Aplicar el estilo visual nativo del sistema operativo (Look and Feel)
        // Esto evita que las ventanas se vean con el diseño gris antiguo de Java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo aplicar el Look and Feel del sistema.");
        }

        // Lanzar la interfaz gráfica de forma segura en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setLocationRelativeTo(null); // Centra la pantalla de Login en el monitor
            login.setVisible(true); // Hace visible la ventana de inicio de sesión
        });
    }
}