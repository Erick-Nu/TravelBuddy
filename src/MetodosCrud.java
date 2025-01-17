import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MetodosCrud {

    /* Insertar Usuario */
    public void insertUser(int cedula, String nombre, String correo, String usuario, int cargo, String password) {
        String query = "INSERT INTO USERS (CEDULA, NOMBRE, EMAIL, USUARIO, CARGO, CONTRASENA) VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cedula);
            ps.setString(2, nombre);
            ps.setString(3, correo);
            ps.setString(4, usuario);
            ps.setInt(5, cargo);
            ps.setString(6, password);
            ps.executeUpdate();
            System.out.println(" USER INSERT SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
