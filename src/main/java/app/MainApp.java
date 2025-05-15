package app;

import app.servicio.BDServicio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Buscador Empresas TIC");
        primaryStage.setScene(scene);
        primaryStage.setWidth(800); // Ancho ajustable
        primaryStage.setHeight(600); // Alto ajustable
        primaryStage.centerOnScreen(); // Centra la ventana en la pantalla
        primaryStage.show();
    }

    public static void main(String[] args) {
        BDServicio.crearYPoblarBaseDeDatos(); // Iniciar la base de datos al arrancar la aplicación
        launch(args);
    }
}