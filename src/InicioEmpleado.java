import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioEmpleado {
    private JButton btnCliente;
    private JButton btnVenta;
    private JButton btnVerPaquetes;
    public JPanel VentanaOpcionesEmpleado;

    public InicioEmpleado() {
        btnVerPaquetes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesEmpleado);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ver Paquetes");
                ventasFrame.setContentPane(new VerPaquetes().VentanaVerPaquetes);
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
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesEmpleado);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Cliente");
                ventasFrame.setContentPane(new IngresarCliente().VentanaIngresoCliente);
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
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOpcionesEmpleado);
                loginFrame.dispose();

                JFrame ventasFrame = new JFrame("Ventana Ingresar Cliente");
                ventasFrame.setContentPane(new RegistrarVenta().VentanaRegistrarVenta);
                ventasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventasFrame.setSize(600, 600);
                ventasFrame.setPreferredSize(new Dimension(300, 300));
                ventasFrame.pack();
                ventasFrame.setVisible(true);
            }
        });
    }
}
