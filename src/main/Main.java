package main;

import javax.swing.SwingUtilities;
import controller.CineController;
import persistence.PeliculaDAO;
import view.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        
        // se asegura de que la inicializacion de la interfaz Swing ocurra
        SwingUtilities.invokeLater(() -> {
            
            // 1 instanciar la capa de persistencia 
            PeliculaDAO dao = new PeliculaDAO();
            
            // 2 instanciar la capa de vista 
            VentanaPrincipal view = new VentanaPrincipal();
            
            // 3 instanciar y conectar el controlador pasandole la vista y el DAO
            CineController controller = new CineController(view, dao);
        });
    }
}
