package app.controlador;

import app.modelo.Empresa;
import app.modelo.Etiqueta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DetalleEmpresaController {

    @FXML
    private Label nombreEmpresaLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label linkedinLabel;
    @FXML
    private Label etiquetasLabel;

    private static Empresa empresaSeleccionada;

    // Método para setear la empresa seleccionada
    public static void setEmpresa(Empresa empresa) {
        empresaSeleccionada = empresa;
    }

    @FXML
    public void initialize() {
        if (empresaSeleccionada != null) {
            // Mostrar los detalles de la empresa seleccionada
            nombreEmpresaLabel.setText(empresaSeleccionada.getNombre());
            direccionLabel.setText(empresaSeleccionada.getDireccion().toString()); // Asegúrate de que la clase Direccion tenga un toString adecuado
            telefonoLabel.setText(empresaSeleccionada.getTelefono().toString()); // Asegúrate de que la clase Telefono tenga un toString adecuado
            emailLabel.setText(empresaSeleccionada.getEmail().toString()); // Asegúrate de que la clase Email tenga un toString adecuado
            linkedinLabel.setText(empresaSeleccionada.getLinkedinUrl());

            // Mostrar las etiquetas
            StringBuilder etiquetasTexto = new StringBuilder();
            for (Etiqueta etiqueta : empresaSeleccionada.getEtiquetas()) {
                etiquetasTexto.append(etiqueta.toString()).append(", ");
            }
            etiquetasLabel.setText(etiquetasTexto.length() > 0 ? etiquetasTexto.substring(0, etiquetasTexto.length() - 2) : "Sin etiquetas");
        }
    }

    @FXML
    public void onVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/busqueda.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) nombreEmpresaLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}