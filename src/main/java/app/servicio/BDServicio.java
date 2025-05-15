package app.servicio;

import java.sql.*;
import app.modelo.*;

import java.util.Arrays;
import java.util.List;

public class BDServicio {
    private static final String URL = "jdbc:sqlite:empresas.db";  // Ruta de la base de datos
    private static boolean inicializado = false;

    // Método para obtener la conexión con la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Método que inicializa la base de datos: crea tablas y las puebla si es necesario
    public static void crearYPoblarBaseDeDatos() {
        if (!inicializado) {
            try (Connection conn = getConnection()) {
                if (conn != null) {
                    crearTablas(conn);
                    poblarDatos(conn);
                    inicializado = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Crea las tablas de la base de datos si no existen
    private static void crearTablas(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Direcciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pais TEXT, ccaa TEXT, provincia TEXT, ciudad TEXT, calle TEXT, cp INTEGER)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Emails (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Telefonos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "telefono TEXT)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Etiquetas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "etiqueta TEXT)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Empresas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "id_direccion INTEGER," +
                "id_telefono INTEGER," +
                "id_email INTEGER," +
                "linkedin_url TEXT," +
                "FOREIGN KEY(id_direccion) REFERENCES Direcciones(id)," +
                "FOREIGN KEY(id_telefono) REFERENCES Telefonos(id)," +
                "FOREIGN KEY(id_email) REFERENCES Emails(id))");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS EmpresaEtiqueta (" +
                "id_empresa INTEGER," +
                "id_etiqueta INTEGER," +
                "FOREIGN KEY(id_empresa) REFERENCES Empresas(id)," +
                "FOREIGN KEY(id_etiqueta) REFERENCES Etiquetas(id))");
    }

    // Inserta datos de prueba en todas las tablas si la base está vacía
    private static void poblarDatos(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Empresas");
        if (rs.next() && rs.getInt("total") > 0) return;

        // Direcciones
        stmt.executeUpdate("INSERT INTO Direcciones (pais, ccaa, provincia, ciudad, calle, cp) VALUES " +
                "('España','Madrid','Madrid','Madrid','Calle Gran Vía, 28', 28013)," +
                "('España','Cataluña','Barcelona','Barcelona','Avenida Diagonal, 7', 08019)," +
                "('España','Andalucía','Sevilla','Sevilla','Calle Sierpes, 12', 41004)," +
                "('España','Valencia','Valencia','Valencia','Calle Xativa, 30', 46002)," +
                "('España','Galicia','Vigo','Vigo','Calle Urzaiz, 35', 36201)," +
                "('España','País Vasco','Bilbao','Bilbao','Gran Vía, 15', 48009)," +
                "('España','Castilla y León','Valladolid','Valladolid','Calle de la Constitución, 3', 47001)," +
                "('España','Madrid','Madrid','Madrid','Calle de Alcalá, 45', 28014)," +
                "('España','Castilla-La Mancha','Toledo','Toledo','Calle de la Paz, 10', 45001)," +
                "('España','Murcia','Murcia','Murcia','Calle Ceballos, 14', 30001)");

        // Teléfonos
        for (int i = 0; i < 20; i++) {
            stmt.executeUpdate("INSERT INTO Telefonos (telefono) VALUES ('+34 600 000 " + String.format("%03d", i) + "')");
        }

        // Emails
        for (int i = 0; i < 20; i++) {
            stmt.executeUpdate("INSERT INTO Emails (email) VALUES ('empresa" + (i + 1) + "@empresa.com')");
        }

        // Etiquetas (lenguajes, frameworks, tecnologías, etc)
        String[] etiquetas = {
                "Java", "Python", "JavaScript", "C#", "C++", "Ruby", "PHP", "SQL", "NoSQL",
                "HTML", "CSS", "TypeScript", "React", "Angular", "Vue.js", "Spring Boot", "Django",
                "Flask", "Node.js", "Express", "MongoDB", "PostgreSQL", "MySQL", "Firebase", "AWS",
                "Azure", "Docker", "Kubernetes", "DevOps", "Git", "Linux", "Android", "iOS"
        };
        for (String etiqueta : etiquetas) {
            stmt.executeUpdate("INSERT INTO Etiquetas (etiqueta) VALUES ('" + etiqueta + "')");
        }

        // Empresas
        List<String> nombresEmpresas = Arrays.asList(
                "Microsoft", "Google", "Apple", "Amazon", "Facebook", "IBM", "Oracle", "SAP", "Intel",
                "Cisco", "Tesla", "Adobe", "Dell", "Spotify", "Twitter", "Red Hat", "Zoom", "Airbnb",
                "Slack", "Snapchat");

        for (int i = 0; i < 20; i++) {
            stmt.executeUpdate("INSERT INTO Empresas (nombre, id_direccion, id_telefono, id_email, linkedin_url) VALUES (" +
                    "'" + nombresEmpresas.get(i) + "', " + (i + 1) + ", " + (i + 1) + ", " + (i + 1) +
                    ", 'https://www.linkedin.com/company/" + nombresEmpresas.get(i).toLowerCase() + "')");
        }

        // Relación Empresa-Etiqueta (cada empresa con 3 etiquetas aleatorias)
        for (int empresaId = 1; empresaId <= 20; empresaId++) {
            for (int i = 0; i < 3; i++) {
                int etiquetaId = 1 + (int)(Math.random() * etiquetas.length);
                stmt.executeUpdate("INSERT INTO EmpresaEtiqueta (id_empresa, id_etiqueta) VALUES (" +
                        empresaId + ", " + etiquetaId + ")");
            }
        }
    }
}