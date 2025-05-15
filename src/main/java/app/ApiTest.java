package app;

import org.json.JSONArray;
import org.json.JSONObject;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class ApiTest {
    public static void main(String[] args) {
        HttpResponse<String> response = Unirest.get("https://linkedin-job-search-api.p.rapidapi.com/active-jb-7d?limit=2&offset=0&location_filter=%22Zaragoza%22")
                .header("x-rapidapi-key", "9cdcdf5b70msh861ba844d2f3889p1208aejsn9ff6ffc3bd4a")
                .header("x-rapidapi-host", "linkedin-job-search-api.p.rapidapi.com")
                .asString();

        // Imprimir el cuerpo de la respuesta para diagnosticar el problema
        System.out.println(response.getBody());

        if (response.getStatus() == 200) {
            try {
                // Ahora procesamos como un JSONArray
                String body = response.getBody();
                JSONArray jsonArray = new JSONArray(body);

                // Solo tomamos los dos primeros resultados
                for (int i = 0; i < Math.min(2, jsonArray.length()); i++) {
                    JSONObject job = jsonArray.getJSONObject(i);
                    System.out.println("Título: " + job.getString("title"));
                    System.out.println("Empresa: " + job.optString("organization", "Desconocida"));
                    System.out.println("Ubicación: " + job.optString("locations_raw", "No especificada"));
                    System.out.println("Enlace: " + job.getString("url"));
                    System.out.println("-------------------------");
                }
            } catch (Exception e) {
                System.out.println("Error al procesar la respuesta JSON: " + e.getMessage());
            }
        } else {
            System.out.println("Error en la petición: " + response.getStatusText());
        }
    }
}