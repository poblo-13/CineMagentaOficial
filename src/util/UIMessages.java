package util;

import java.awt.Component;
import javax.swing.JOptionPane;

public class UIMessages {

    private UIMessages() {}

    public static void mostrarMensaje(Component parent, String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(parent, mensaje, titulo, tipo);
    }

    public static int confirmarOperacion(Component parent, String mensaje, String titulo) {
        return JOptionPane.showConfirmDialog(parent, mensaje, titulo, JOptionPane.YES_NO_OPTION);
    }
}
