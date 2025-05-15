package app.utilidad;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GestorEscenas {

    private static Stage escenarioPrincipal;

    public static void setEscenarioPrincipal(Stage stage) {
        escenarioPrincipal = stage;
    }

    public static void cambiarEscena(String rutaFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(GestorEscenas.class.getResource("/fxml/" + rutaFXML + ".fxml"));
            Parent root = loader.load();

            Scene nuevaEscena = new Scene(root);
            nuevaEscena.getStylesheets().add(GestorEscenas.class.getResource("/css/estilos.css").toExternalForm());

            escenarioPrincipal.setTitle(titulo);
            escenarioPrincipal.setScene(nuevaEscena);
            escenarioPrincipal.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + rutaFXML);
        }
    }
}