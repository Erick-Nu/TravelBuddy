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

    public Login() {
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = user.getText();
                String contrasena = new String(password.getPassword());

                String[] queries = {
                        "SELECT 'ADMINISTRADOR' as tipo FROM administrador WHERE nameUser=? AND contrasena=?",
                        "SELECT 'EMPLEADO' as tipo FROM empleados WHERE nameUser=? AND contrasena=?",
                        "SELECT 'CLIENTE' as tipo FROM clientes WHERE email=? AND cedula=?"
                };

                String tipoUsuario = null;
                int idCliente = -1; // Inicializamos con un valor no válido

                try (Connection con = Conexion.getConnection()) {
                    for (String query : queries) {
                        try (PreparedStatement ps = con.prepareStatement(query)) {
                            ps.setString(1, usuario);
                            ps.setString(2, contrasena);
                            ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                tipoUsuario = rs.getString("tipo");
                                break; // Sale del bucle cuando encuentra el usuario válido
                            }
                        }
                    }


                    // Si encontramos un usuario válido
                    if (tipoUsuario != null) {
                        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaLogin);
                        if (loginFrame != null) {
                            loginFrame.dispose();
                        }

                        JFrame inicioFrame = new JFrame("Inicio");
                        switch (tipoUsuario) {
                            case "ADMINISTRADOR":
                                System.out.println("ADMINISTRADOR");
                                JFrame login = (JFrame) SwingUtilities.getWindowAncestor(VentanaLogin);
                                login.dispose();

                                JFrame loginVenta = new JFrame("Ventana Administrador");
                                loginVenta.setContentPane(new InicioAdministrador().VentanaOpcionesAdmin);
                                loginVenta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                loginVenta.setSize(600, 600);
                                loginVenta.setPreferredSize(new Dimension(300, 300));
                                loginVenta.pack();
                                loginVenta.setVisible(true);
                                break;
                            case "EMPLEADO":
                                System.out.println("EMPLEADO");
                                JFrame empleado = (JFrame) SwingUtilities.getWindowAncestor(VentanaLogin);
                                empleado.dispose();

                                JFrame inicioEmpleado = new JFrame("Ventana Empleado");
                                inicioEmpleado.setContentPane(new InicioEmpleado().VentanaOpcionesEmpleado);
                                inicioEmpleado.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                inicioEmpleado.setSize(600, 600);
                                inicioEmpleado.setPreferredSize(new Dimension(300, 300));
                                inicioEmpleado.pack();
                                inicioEmpleado.setVisible(true);
                                break;
                            case "CLIENTE":
                                String queryIdCliente = "SELECT id FROM clientes WHERE email=? AND cedula=?";
                                try (PreparedStatement ps = con.prepareStatement(queryIdCliente)) {
                                    ps.setString(1, usuario);
                                    ps.setString(2, contrasena);
                                    ResultSet rs = ps.executeQuery();
                                    if (rs.next()) {
                                        idCliente = rs.getInt("id");
                                    }
                                }
                                System.out.println("CLIENTE - ID: " + idCliente);
                                JFrame InicioCliente = new JFrame("Ventana Cliente");
                                InicioCliente.setContentPane(new InicioCliente(idCliente).getVentanaCliente());
                                InicioCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                InicioCliente.setSize(600, 600);
                                InicioCliente.setPreferredSize(new Dimension(300, 300));
                                InicioCliente.pack();
                                InicioCliente.setVisible(true);
                                break;
                        }
                    } else {
                        throw new Exception("CREDENCIALES INCORRECTAS");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            "Error de inicio de sesión",
                            JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });
    }
}
