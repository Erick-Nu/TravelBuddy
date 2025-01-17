import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
    private JTextField user;
    private JButton LOGINButton;
    private JPasswordField password;
    public JPanel VentanaLogin;
    private Image image;


    public Login() {
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* CONEXION A LA BASE DE DATOS Y CONSULTA SQL*/
                String query = "SELECT * FROM USERS WHERE USUARIO=? AND CONTRASENA=?";
                try (Connection con = Conexion.getConnection();
                     PreparedStatement ps = con.prepareStatement(query)) {

                    /* OBTENER LOS DATOS DE USER Y PASSWORD */
                    String usuario = user.getText();
                    String contrasena = password.getText();

                    ps.setString(1, usuario);
                    ps.setString(2, contrasena);
                    ResultSet rs = ps.executeQuery();

                    // Verificar si se encontró el usuario
                    if (rs.next()) {
                        System.out.println("INICIO DE SESION EXITOSO");
                    } else {
                        System.out.println("Usuario o contraseña incorrectos");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
