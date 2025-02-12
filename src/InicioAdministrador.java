import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioAdministrador {
    private JButton btnEmpleado;
    private JButton btnRevision;
    private JButton btnVenta;
    private JButton btnCliente;
    public JPanel VentanaOpcionesAdmin;
    private JButton btnPaquetes;

    public InicioAdministrador() {
        btnEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesAdmin);
                loginFrame.dispose();

                JFrame empleadoFrame = new JFrame("Ventana Ingresar Empleado");
                empleadoFrame.setContentPane(new IngresarEmpleado().VentanaIngresoEmpleado);
                empleadoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                empleadoFrame.setSize(600, 600);
                empleadoFrame.setPreferredSize(new Dimension(300, 300));
                empleadoFrame.pack();
                empleadoFrame.setVisible(true);
            }
        });
        btnRevision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesAdmin);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Empleado");
                ventasFrame.setContentPane(new RevisionVentas().VentanaRevisionVentas);
                ventasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventasFrame.setSize(600, 600);
                ventasFrame.setPreferredSize(new Dimension(300, 300));
                ventasFrame.pack();
                ventasFrame.setVisible(true);

            }
        });
        btnVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesAdmin);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Empleado");
                ventasFrame.setContentPane(new RegistrarVenta().VentanaRegistrarVenta);
                ventasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventasFrame.setSize(600, 600);
                ventasFrame.setPreferredSize(new Dimension(300, 300));
                ventasFrame.pack();
                ventasFrame.setVisible(true);

            }
        });
        btnCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesAdmin);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Empleado");
                ventasFrame.setContentPane(new IngresarCliente().VentanaIngresoCliente);
                ventasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventasFrame.setSize(600, 600);
                ventasFrame.setPreferredSize(new Dimension(300, 300));
                ventasFrame.pack();
                ventasFrame.setVisible(true);
            }
        });
        btnPaquetes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesAdmin);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Empleado");
                ventasFrame.setContentPane(new IngresarPaquete().VentanaIngresarPaquete);
                ventasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventasFrame.setSize(600, 600);
                ventasFrame.setPreferredSize(new Dimension(300, 300));
                ventasFrame.pack();
                ventasFrame.setVisible(true);
            }
        });
    }
}
