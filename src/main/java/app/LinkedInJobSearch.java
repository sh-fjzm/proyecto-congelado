package app;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;


public class LinkedInJobSearch {

    public static void main(String[] args) {
        // Realizar la solicitud GET a la API de LinkedIn Jobs con parámetros de búsqueda para Zaragoza o Aragón
        try {
            HttpResponse<String> response = Unirest.get("https://linkedin-job-search-api.p.rapidapi.com/active-jb-7d?limit=10&offset=0&location_filter=%22Zaragoza%22%20OR%20%22Arag%C3%B3n%22%20OR%20%22Aragon%22")
                    .header("x-rapidapi-key", "9cdcdf5b70msh861ba844d2f3889p1208aejsn9ff6ffc3bd4a")
                    .header("x-rapidapi-host", "linkedin-job-search-api.p.rapidapi.com")
                    .asString();  // Esto obtiene la respuesta en formato String

            // Verificar si la solicitud fue exitosa (código 200)
            if (response.getStatus() == 200) {
                // Muestra la respuesta completa de la API (por si quieres ver todo el JSON)
                System.out.println("Respuesta completa de la API:");
                System.out.println(response.getBody());
                System.out.println("\n");

                // Aquí analizamos los resultados de la respuesta (el JSON devuelto por la API)
                // Primero, parseamos la respuesta JSON para extraer los trabajos
                String jsonResponse = response.getBody();
                int startIndex = jsonResponse.indexOf("[");  // Empieza del primer objeto de trabajo
                int endIndex = jsonResponse.indexOf("]");    // Termina con el último objeto de trabajo
                String jobsData = jsonResponse.substring(startIndex, endIndex + 1);

                System.out.println("Resultados obtenidos:");
                System.out.println(jobsData);

                // Mostrar solo los primeros 2 resultados de la lista
                // Aquí puedes agregar más lógica para filtrar o procesar los resultados más a fondo
                String[] jobEntries = jobsData.split("},");
                for (int i = 0; i < Math.min(2, jobEntries.length); i++) {
                    System.out.println("Resultado #" + (i + 1) + ":");
                    System.out.println(jobEntries[i] + "}\n");
                }
            } else {
                System.out.println("Error en la solicitud. Código de estado: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
