import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:\\Tercer Semestre  2024\\POO\\Bases de Datos\\agenciaViajes.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}
