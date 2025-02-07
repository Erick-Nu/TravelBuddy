import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrarVenta {
    public JPanel VentanaRegistrarVenta;
    private JTextField textField1;  // Campo de texto para la cédula del cliente
    private JTable table1;  // Tabla para mostrar el nombre del cliente
    private JComboBox boxEmpleado;  // ComboBox para seleccionar al empleado
    private JComboBox boxPaquete;  // ComboBox para seleccionar el paquete
    private JButton btnRegistrar;  // Botón para registrar la venta
    private JButton btnRegresar;

    public RegistrarVenta() {
        // Configuración inicial de la tabla
        table1.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Cliente"}
        ));

        // Llenar ComboBox con la información de empleados y paquetes
        llenarComboBoxs();

        // Acción del botón para buscar cliente por cédula
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedula = textField1.getText().trim();
                if (!cedula.isEmpty()) {
                    buscarClientePorCedula(cedula);
                }
            }
        });

        // Acción del botón para registrar la venta
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarVenta();
            }
        });
        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame revision = (JFrame) SwingUtilities.getWindowAncestor(VentanaRegistrarVenta);
                revision.dispose();

                JFrame  opcionesAdmin = new JFrame("Ventana Opciones");
                opcionesAdmin.setContentPane(new InicioAdministrador().VentanaOpcionesAdmin);
                opcionesAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                opcionesAdmin.setSize(600, 600);
                opcionesAdmin.setPreferredSize(new Dimension(300, 300));
                opcionesAdmin.pack();
                opcionesAdmin.setVisible(true);
            }
        });
    }

    private void buscarClientePorCedula(String cedula) {
        // Consulta SQL para obtener el nombre del cliente según la cédula
        String query = "SELECT nombre, apellido_paterno, apellido_materno FROM clientes WHERE cedula = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, cedula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
                    // Mostrar el nombre del cliente en la tabla
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Limpiar la tabla antes de agregar los datos
                    model.addRow(new Object[]{nombreCompleto});
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void llenarComboBoxs() {
        // Llenar ComboBox de empleados
        String queryEmpleado = "SELECT id, nombre FROM empleados";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(queryEmpleado);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                boxEmpleado.addItem(rs.getString("nombre"));  // Agregar el nombre del empleado
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los empleados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        // Llenar ComboBox de paquetes
        String queryPaquete = "SELECT id, nombre FROM paquetes";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(queryPaquete);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                boxPaquete.addItem(rs.getString("nombre"));  // Agregar el nombre del paquete
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los paquetes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void registrarVenta() {
        String clienteCedula = textField1.getText().trim();
        String empleado = (String) boxEmpleado.getSelectedItem();
        String paquete = (String) boxPaquete.getSelectedItem();

        if (clienteCedula.isEmpty() || empleado == null || paquete == null) {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del cliente por su cédula
        int clienteId = obtenerIdCliente(clienteCedula);
        int empleadoId = obtenerIdEmpleado(empleado);
        int paqueteId = obtenerIdPaquete(paquete);

        // Registrar la venta en la base de datos
        String query = "INSERT INTO ventas (id_empleado, id_cliente, id_paquete) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empleadoId);
            ps.setInt(2, clienteId);
            ps.setInt(3, paqueteId);

            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Venta registrada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar la venta", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int obtenerIdCliente(String cedula) {
        String query = "SELECT id FROM clientes WHERE cedula = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return -1;  // Si no se encuentra el cliente, retornar -1
    }

    private int obtenerIdEmpleado(String nombreEmpleado) {
        String query = "SELECT id FROM empleados WHERE nombre = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nombreEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return -1;  // Si no se encuentra el empleado, retornar -1
    }

    private int obtenerIdPaquete(String nombrePaquete) {
        String query = "SELECT id FROM paquetes WHERE nombre = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nombrePaquete);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del paquete: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return -1;  // Si no se encuentra el paquete, retornar -1
    }
}

