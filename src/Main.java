import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /* PANTALLA LOGIN */
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setContentPane(new Login().VentanaLogin);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(600, 600);
        loginFrame.setPreferredSize(new Dimension(300, 300));
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}