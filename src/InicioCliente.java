import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioCliente {
    private JTable table1;
    private JLabel nameCliente;
    private JButton button1;
    public JPanel VentanaCliente;
    private int idCliente; // Almacena el ID del cliente logueado

    // Constructor que recibe el ID del cliente
    public InicioCliente(int idCliente) {
        this.idCliente = idCliente;

        // Obtener y mostrar el nombre del cliente
        String nombreCliente = obtenerNombreCliente(idCliente);
        nameCliente.setText("Bienvenido, " + nombreCliente);

        // Configurar la tabla
        table1.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Paquete", "Transporte", "Fecha Viaje", "Precio"}
        ));

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatosTabla();
            }
        });

        cargarDatosTabla(); // Cargar datos automáticamente al abrir
    }

    private void cargarDatosTabla() {
        String query = """
            SELECT paquete, transporte, fecha_viaje, precio 
            FROM vista_viajes_cliente 
            WHERE id_cliente = ?;
        """;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idCliente); // Pasar el ID del cliente logueado

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.setRowCount(0); // Limpiar la tabla antes de agregar datos

                while (rs.next()) {
                    Object[] row = {
                            rs.getString("paquete"),
                            rs.getString("transporte"),
                            rs.getString("fecha_viaje"),
                            rs.getInt("precio")
                    };
                    model.addRow(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerNombreCliente(int idCliente) {
        String query = "SELECT cliente FROM vista_viajes_cliente WHERE id_cliente = ? LIMIT 1;";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cliente");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Cliente"; // Valor por defecto si no se encuentra el cliente
    }

    public JPanel getVentanaCliente() {
        return VentanaCliente;
    }
}
