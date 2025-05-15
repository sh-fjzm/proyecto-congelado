package app.controlador;

import app.modelo.Empresa;
import app.modelo.Direccion;
import app.modelo.Telefono;
import app.modelo.Email;
import app.modelo.Etiqueta;  // Asegúrate de que esta clase esté importada
import app.servicio.BDServicio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusquedaController {

    @FXML
    private ListView<String> listaResultados;

    private static List<String> etiquetasSeleccionadas;
    private static List<Empresa> empresas;  // Para almacenar las empresas completas

    public static void setEtiquetasSeleccionadas(List<String> etiquetas) {
        etiquetasSeleccionadas = etiquetas;
    }

    @FXML
    public void initialize() {
        cargarResultados();
    }

    private void cargarResultados() {
        try (Connection conn = BDServicio.getConnection()) {
            StringBuilder query = new StringBuilder("SELECT DISTINCT e.id AS empresa_id, e.nombre, d.id AS direccion_id, d.pais, d.ccaa, d.provincia, d.ciudad, d.calle, d.cp, t.id AS telefono_id, t.telefono, em.id AS email_id, em.email, e.linkedin_url ");
            query.append("FROM Empresas e ");
            query.append("JOIN Direcciones d ON e.id_direccion = d.id ");
            query.append("JOIN Telefonos t ON e.id_telefono = t.id ");
            query.append("JOIN Emails em ON e.id_email = em.id ");
            query.append("WHERE 1=1");  // Aquí no estamos filtrando por etiquetas, por lo que añadimos una condición siempre verdadera

            // Si hay etiquetas seleccionadas, las agregamos a la consulta
            if (etiquetasSeleccionadas != null && !etiquetasSeleccionadas.isEmpty()) {
                query.append(" AND EXISTS (SELECT 1 FROM EmpresaEtiqueta ee ");
                query.append("JOIN Etiquetas et ON ee.id_etiqueta = et.id ");
                query.append("WHERE ee.id_empresa = e.id AND et.etiqueta IN (");

                for (int i = 0; i < etiquetasSeleccionadas.size(); i++) {
                    query.append("?");
                    if (i < etiquetasSeleccionadas.size() - 1) {
                        query.append(", ");
                    }
                }
                query.append("))");
            }

            System.out.println("Consulta generada: " + query.toString()); // Para depuración

            PreparedStatement ps = conn.prepareStatement(query.toString());
            if (etiquetasSeleccionadas != null && !etiquetasSeleccionadas.isEmpty()) {
                for (int i = 0; i < etiquetasSeleccionadas.size(); i++) {
                    ps.setString(i + 1, etiquetasSeleccionadas.get(i));
                }
            }

            ResultSet rs = ps.executeQuery();
            int resultadoCount = 0;

            // Limpiar la lista de resultados antes de llenarla
            listaResultados.getItems().clear();

            while (rs.next()) {
                // Extraer los datos de la empresa
                int empresaId = rs.getInt("empresa_id");
                String nombre = rs.getString("nombre");

                // Crear el objeto Direccion
                Direccion direccion = new Direccion(
                        rs.getInt("direccion_id"),
                        rs.getString("pais"),
                        rs.getString("ccaa"),
                        rs.getString("provincia"),
                        rs.getString("ciudad"),
                        rs.getString("calle"),
                        rs.getInt("cp")
                );

                // Crear el objeto Telefono
                Telefono telefono = new Telefono(rs.getInt("telefono_id"), rs.getString("telefono"));

                // Crear el objeto Email
                Email email = new Email(rs.getInt("email_id"), rs.getString("email"));

                String linkedin = rs.getString("linkedin_url");

                // Obtener las etiquetas asociadas a esta empresa desde la tabla EmpresaEtiqueta
                List<Etiqueta> etiquetas = new ArrayList<>();
                String etiquetasQuery = "SELECT et.id, et.etiqueta FROM EmpresaEtiqueta ee " +
                        "JOIN Etiquetas et ON ee.id_etiqueta = et.id " +
                        "WHERE ee.id_empresa = ?";
                try (PreparedStatement etiquetasStmt = conn.prepareStatement(etiquetasQuery)) {
                    etiquetasStmt.setInt(1, empresaId);
                    ResultSet etiquetasRs = etiquetasStmt.executeQuery();

                    while (etiquetasRs.next()) {
                        int etiquetaId = etiquetasRs.getInt("id");
                        String etiquetaNombre = etiquetasRs.getString("etiqueta");
                        etiquetas.add(new Etiqueta(etiquetaId, etiquetaNombre));  // Crear el objeto Etiqueta
                    }
                }

                // Crear la empresa con la direccion, telefono, email y etiquetas
                Empresa empresa = new Empresa(empresaId, nombre, direccion, telefono, email, linkedin, etiquetas);

                // Añadir la empresa a la lista de empresas
                if (empresas == null) {
                    empresas = new ArrayList<>();
                }
                empresas.add(empresa);
                resultadoCount++;

                // Añadir solo el nombre de la empresa a la lista de resultados
                listaResultados.getItems().add(empresa.getNombre());
            }

            // Depuración: Verificar si se encontraron resultados
            if (resultadoCount == 0) {
                System.out.println("No se encontraron empresas con las etiquetas seleccionadas.");
            } else {
                System.out.println("Se encontraron " + resultadoCount + " empresas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onVerDetalles() {
        String empresaSeleccionada = listaResultados.getSelectionModel().getSelectedItem();
        if (empresaSeleccionada != null) {
            // Buscar la empresa completa basándose en el nombre
            Empresa empresa = null;
            for (Empresa e : empresas) {
                if (e.getNombre().equals(empresaSeleccionada)) {
                    empresa = e;
                    break;
                }
            }

            if (empresa != null) {
                try {
                    // Pasar la empresa completa al controlador de detalles
                    DetalleEmpresaController.setEmpresa(empresa);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detalleEmpresa.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) listaResultados.getScene().getWindow(); // misma ventana
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Sin selección");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor selecciona una empresa de la lista.");
            alerta.showAndWait();
        }
    }

    @FXML
    public void onVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Buscador de Empresas TIC");
            stage.setScene(new Scene(root));
            stage.show();

            Stage actual = (Stage) listaResultados.getScene().getWindow();
            actual.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}