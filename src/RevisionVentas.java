import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RevisionVentas {
    private JTextField textBuscar;
    private JTable ventasEmpleados;
    public JPanel VentanaRevisionVentas;
    private JButton btnRegresar;

    public RevisionVentas() {
        // Configurar la tabla ventasEmpleados
        ventasEmpleados.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Empleado", "Cliente", "Paquete", "Valor"}
        ));

        // Cargar todas las ventas al iniciar
        cargarVentasEmpleado(""); // Cargar todos los datos al principio

        // Acción del botón para filtrar por empleado
        textBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empleado = textBuscar.getText().trim();
                cargarVentasEmpleado(empleado);  // Filtrar por empleado (vacío o con nombre)
            }
        });

        // Acción del botón "Regresar"
        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame revision = (JFrame) SwingUtilities.getWindowAncestor(VentanaRevisionVentas);
                revision.dispose();

                JFrame  revisionVentas = new JFrame("Ventana Opciones");
                revisionVentas.setContentPane(new InicioAdministrador().VentanaOpcionesAdmin);
                revisionVentas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                revisionVentas.setSize(600, 600);
                revisionVentas.setPreferredSize(new Dimension(300, 300));
                revisionVentas.pack();
                revisionVentas.setVisible(true);
            }
        });
    }

    private void cargarVentasEmpleado(String empleado) {
        // Consulta SQL para obtener todas las ventas o filtrar por el nombre del empleado
        String query;
        if (empleado.isEmpty()) {
            // Si el campo de búsqueda está vacío, mostrar todas las ventas
            query = "SELECT empleado, cliente, paquete, valor FROM vista_ventas_empleados";
        } else {
            // Filtrar por el nombre del empleado, utilizando LIKE para coincidencias parciales
            query = "SELECT empleado, cliente, paquete, valor FROM vista_ventas_empleados WHERE empleado LIKE ?";
        }

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Si hay un nombre, establecemos el parámetro para la búsqueda
            if (!empleado.isEmpty()) {
                ps.setString(1, "%" + empleado + "%");  // Usamos el operador LIKE para permitir coincidencias parciales
            }

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) ventasEmpleados.getModel();
                model.setRowCount(0); // Limpiar la tabla antes de agregar los datos

                // Procesar los resultados y agregarlos a la tabla
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("empleado"),
                            rs.getString("cliente"),
                            rs.getString("paquete"),
                            rs.getDouble("valor")
                    };
                    model.addRow(row);
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar las ventas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
