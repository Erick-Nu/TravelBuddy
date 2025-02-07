import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class IngresarPaquete {
    private JComboBox<String> boxPais;
    private JComboBox<String> boxCiudad;
    private JTextField textNombre;
    private JTextField textDescripcion;
    private JTextField textPrecio;
    private JTextField textStock;
    private JComboBox<String> boxTransporte;
    private JTextField textFecha;
    private JButton btnRegresar;
    private JButton registrarButton;
    public JPanel VentanaIngresarPaquete;

    public IngresarPaquete() {
        cargarComboBox("SELECT id, nombre FROM paises", boxPais);
        cargarComboBox("SELECT id, nombre FROM ciudades", boxCiudad);
        cargarComboBox("SELECT id, nombre FROM transportes", boxTransporte);

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {
                    registrarPaquete();
                }
            }
        });
    }

    private void cargarComboBox(String query, JComboBox<String> comboBox) {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            comboBox.removeAllItems(); // Limpiamos el ComboBox antes de agregar los nuevos elementos

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int id = rs.getInt("id");
                comboBox.addItem(nombre); // Solo agregamos el nombre al ComboBox
                comboBox.putClientProperty(nombre, id); // Asociamos el ID con el nombre
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (!Pattern.matches("^[a-zA-Z ]+$", textNombre.getText().trim())) {
            JOptionPane.showMessageDialog(null, "El nombre solo puede contener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Pattern.matches("^\\d+(\\.\\d{1,2})?$", textPrecio.getText().trim())) {
            JOptionPane.showMessageDialog(null, "El precio debe ser un número válido (entero o decimal).", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Pattern.matches("^\\d+$", textStock.getText().trim())) {
            JOptionPane.showMessageDialog(null, "El stock debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validarFecha(textFecha.getText().trim())) {
            JOptionPane.showMessageDialog(null, "La fecha debe estar en formato dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validarFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false);
        try {
            formato.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void registrarPaquete() {
        String nombre = textNombre.getText().trim();
        String descripcion = textDescripcion.getText().trim();
        double precio = Double.parseDouble(textPrecio.getText().trim());
        int stock = Integer.parseInt(textStock.getText().trim());
        String fecha = convertirFecha(textFecha.getText().trim());

        int idPais = obtenerIdSeleccionado(boxPais);
        int idCiudad = obtenerIdSeleccionado(boxCiudad);
        int idTransporte = obtenerIdSeleccionado(boxTransporte);

        String query = "INSERT INTO paquetes (nombre, id_pais, id_ciudad, descripcion, precio, stock, id_transporte, fecha_viaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nombre);
            ps.setInt(2, idPais);
            ps.setInt(3, idCiudad);
            ps.setString(4, descripcion);
            ps.setDouble(5, precio);
            ps.setInt(6, stock);
            ps.setInt(7, idTransporte);
            ps.setString(8, fecha);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Paquete registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el paquete.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int obtenerIdSeleccionado(JComboBox<String> comboBox) {
        String seleccionado = (String) comboBox.getSelectedItem();
        // Obtenemos el ID de la propiedad asociada con el nombre seleccionado
        return (int) comboBox.getClientProperty(seleccionado); // Obtener el ID guardado en la propiedad
    }

    private String convertirFecha(String fecha) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatoSalida.format(formatoEntrada.parse(fecha));
        } catch (ParseException e) {
            return null;
        }
    }

    private void limpiarCampos() {
        textNombre.setText("");
        textDescripcion.setText("");
        textPrecio.setText("");
        textStock.setText("");
        textFecha.setText("");
    }
}
