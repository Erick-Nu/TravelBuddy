import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class VerPaquetes {
    private JTable table1;
    public JPanel VentanaVerPaquetes;
    private JTextField textBuscar;

    private DefaultTableModel model;

    public VerPaquetes() {
        // Establecer el modelo de la tabla
        model = new DefaultTableModel();
        model.addColumn("Nombre");
        model.addColumn("Descripción");
        model.addColumn("Precio");
        model.addColumn("Stock");
        table1.setModel(model);

        // Cargar los paquetes iniciales
        cargarPaquetes("");

        // Agregar evento de búsqueda en el JTextField
        textBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Filtrar paquetes según el texto ingresado
                cargarPaquetes(textBuscar.getText());
            }
        });
    }

    // Método para cargar los paquetes en la tabla
    private void cargarPaquetes(String filtro) {
        try {
            // Obtener la conexión de la base de datos
            Connection connection = Conexion.getConnection();

            // Usamos un preparedStatement para realizar la consulta con el filtro
            String sql = "SELECT nombre, descripcion, precio, stock FROM paquetes WHERE nombre LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + filtro + "%");  // Agregar el filtro de búsqueda
            ResultSet rs = statement.executeQuery();

            // Limpiar la tabla antes de agregar los nuevos datos
            model.setRowCount(0);

            // Recorrer los resultados y agregar filas a la tabla
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                // Agregar los datos a la tabla
                model.addRow(new Object[] { nombre, descripcion, precio, stock });
            }

            // Cerrar la conexión
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
