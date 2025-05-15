package app.controlador;

import app.servicio.BDServicio;
import app.modelo.Etiqueta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrincipalController {

    @FXML
    private VBox vboxEtiquetas; // Contenedor de las etiquetas seleccionables

    private List<CheckBox> checkboxesEtiquetas = new ArrayList<>();

    @FXML
    public void initialize() {
        // Inicializar la base de datos y cargar las etiquetas desde la base de datos
        BDServicio.crearYPoblarBaseDeDatos();
        cargarEtiquetas();
    }

    private void cargarEtiquetas() {
        try (Connection conn = BDServicio.getConnection()) {
            String sql = "SELECT * FROM Etiquetas";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String etiqueta = rs.getString("etiqueta");
                CheckBox checkBox = new CheckBox(etiqueta);
                vboxEtiquetas.getChildren().add(checkBox);
                checkboxesEtiquetas.add(checkBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBuscar() {
        List<String> etiquetasSeleccionadas = new ArrayList<>();
        for (CheckBox checkBox : checkboxesEtiquetas) {
            if (checkBox.isSelected()) {
                etiquetasSeleccionadas.add(checkBox.getText());
            }
        }

        if (!etiquetasSeleccionadas.isEmpty()) {
            // Abrir ventana de resultados con las empresas relacionadas con las etiquetas seleccionadas
            BusquedaController.setEtiquetasSeleccionadas(etiquetasSeleccionadas);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/busqueda.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Resultados de la búsqueda");
                stage.setScene(new Scene(root));
                stage.show();

                // Cierra la ventana principal
                Stage actual = (Stage) vboxEtiquetas.getScene().getWindow();
                actual.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onDesmarcarTodo() {
        for (CheckBox checkBox : checkboxesEtiquetas) {
            checkBox.setSelected(false);
        }
    }
}