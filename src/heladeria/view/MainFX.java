package heladeria.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        // Aquí carga tu archivo FXML. Ojo: en tu foto vi que lo llamaste "login.fxml" (con minúscula)
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        
        Scene escena = new Scene(root);
        escenarioPrincipal.setTitle("Luxe Gelato - Sistema");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}