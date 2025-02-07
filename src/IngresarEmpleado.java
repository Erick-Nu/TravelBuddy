import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IngresarEmpleado {
    public JPanel VentanaIngresoEmpleado;
    private JTextField name;
    private JTextField lastName;
    private JTextField user;
    private JTextField password;
    private JButton btnAgregar;

    public IngresarEmpleado() {
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores de los campos
                String nombre = name.getText();
                String apellido = lastName.getText();
                String username = user.getText();
                String pass = password.getText();

                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Si algún campo está vacío, no se ejecuta la consulta
                }

                // Validar que el nombre y apellido contengan solo letras
                if (!nombre.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(null, "El nombre solo debe contener letras", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!apellido.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(null, "El apellido solo debe contener letras", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Llamar a la función para agregar un empleado
                agregarEmpleado(nombre, apellido, username, pass);
            }
        });
    }

    private void agregarEmpleado(String nombre, String apellido, String username, String pass) {
        // Consulta SQL para insertar un nuevo empleado
        String query = "INSERT INTO empleados (nombre, apellido, nameUser, contrasena) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection(); // Obtener conexión a la base de datos
             PreparedStatement ps = con.prepareStatement(query)) {

            // Establecer los parámetros de la consulta
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, username);
            ps.setString(4, pass);

            // Ejecutar la consulta de inserción
            int consulta = ps.executeUpdate();

            if (consulta > 0) {
                JOptionPane.showMessageDialog(null, "Empleado agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar los campos después de agregar el empleado
                name.setText("");
                lastName.setText("");
                user.setText("");
                password.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
