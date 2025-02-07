import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngresarCliente {
    public JPanel VentanaIngresoCliente;
    private JButton registrarButton;
    private JTextField name;
    private JTextField email;
    private JTextField lastNameFather;
    private JTextField lastNameMother;
    private JTextField cedula;

    public IngresarCliente() {
        // Acción del botón "Registrar"
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos antes de insertar los datos
                if (validarCampos()) {
                    registrarCliente();
                }
            }
        });
    }

    private boolean validarCampos() {
        // Validar cédula (debe tener 10 dígitos y ser solo numérica)
        String cedulaTexto = cedula.getText().trim();
        if (cedulaTexto.isEmpty() || cedulaTexto.length() != 10 || !cedulaTexto.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "La cédula debe tener 10 dígitos y ser numérica.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar email (debe tener un formato válido de correo electrónico)
        String emailTexto = email.getText().trim();
        if (emailTexto.isEmpty() || !esEmailValido(emailTexto)) {
            JOptionPane.showMessageDialog(null, "El correo electrónico no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar los nombres y apellidos (deben contener solo letras y no estar vacíos)
        if (!esNombreValido(name.getText().trim()) || !esNombreValido(lastNameFather.getText().trim()) || !esNombreValido(lastNameMother.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Los campos de nombre y apellidos deben contener solo letras.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar los demás campos (deben ser no vacíos)
        if (name.getText().trim().isEmpty() || lastNameFather.getText().trim().isEmpty() || lastNameMother.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;  // Todos los campos son válidos
    }

    // Función para validar el formato de un correo electrónico
    private boolean esEmailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Función para validar si un nombre/apellido contiene solo letras
    private boolean esNombreValido(String nombre) {
        String regex = "^[a-zA-Z]+$"; // Solo letras (sin espacios, números o caracteres especiales)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombre);
        return matcher.matches();
    }

    private void registrarCliente() {
        // Obtener los datos de los campos
        String nombre = name.getText().trim();
        String apellidoPaterno = lastNameFather.getText().trim();
        String apellidoMaterno = lastNameMother.getText().trim();
        String correo = email.getText().trim();
        String cedulaTexto = cedula.getText().trim();

        // Consulta SQL para insertar un nuevo cliente
        String query = "INSERT INTO clientes (nombre, apellido_paterno, apellido_materno, cedula, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Establecer los valores en el PreparedStatement
            ps.setString(1, nombre);
            ps.setString(2, apellidoPaterno);
            ps.setString(3, apellidoMaterno);
            ps.setString(4, cedulaTexto);
            ps.setString(5, correo);

            // Ejecutar la consulta
            int resultado = ps.executeUpdate();

            // Verificar si la inserción fue exitosa
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar los campos después de registrar
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        name.setText("");
        email.setText("");
        lastNameFather.setText("");
        lastNameMother.setText("");
        cedula.setText("");
    }
}
