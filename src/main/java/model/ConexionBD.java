package model;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a MySQL.
 *
 * Las credenciales (URL, usuario, contraseña) ya NO están escritas en este
 * archivo: se leen desde un archivo .env ubicado en la raíz del proyecto,
 * el cual NO se sube al repositorio (está en .gitignore). Esto evita que
 * la contraseña quede expuesta en el código fuente o en GitHub.
 */
public class ConexionBD {

    // Se carga UNA sola vez, la primera vez que se usa la clase
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()   // no falla si alguien olvida crear el .env (usa valores por defecto)
            .load();

    private static final String URL = dotenv.get("DB_URL", "jdbc:mysql://localhost:3306/gestion_notas");
    private static final String USUARIO = dotenv.get("DB_USER", "root");
    private static final String CONTRASENA = dotenv.get("DB_PASSWORD", "");

    private static Connection conexion = null;

    private ConexionBD() {}

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("Conexión exitosa a la base de datos");
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Driver MySQL no encontrado");
                throw new SQLException("Driver no encontrado", e);
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    public static boolean probarConexion() {
        try {
            Connection conn = getConexion();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
