package heladeria.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import heladeria.database.UsuarioDAO;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPass;
    @FXML private RadioButton rbEmpleado;
    @FXML private RadioButton rbCliente;

    @FXML
    private void ingresarAlSistema(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String password = txtPass.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Llena todos los campos bro.");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.validarUsuario(usuario, password)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Bienvenido " + usuario);
            
            // 1. Abre la pantalla principal de tu heladería (MainView)
            java.awt.EventQueue.invokeLater(() -> {
                new MainView().setVisible(true);
            });
            
            // 2. Cierra esta ventana de login
            javafx.stage.Stage stage = (javafx.stage.Stage) txtUsuario.getScene().getWindow();
            stage.close();
            
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Credenciales incorrectas.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}