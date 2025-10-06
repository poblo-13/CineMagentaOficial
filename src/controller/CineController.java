package controller;

import view.VentanaPrincipal;
import persistence.PeliculaDAO;
import model.Pelicula;
import util.UIMessages; // Asegúrate de tener esta clase implementada
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Objects; 

public class CineController implements ActionListener {

    private final VentanaPrincipal view; 
    private final PeliculaDAO dao; 

    public CineController(VentanaPrincipal view, PeliculaDAO dao) {
        this.view = view;
        this.dao = dao;
        this.view.setVisible(true);
        cargarPeliculas();    
        asignarListeners();
    }

    private void asignarListeners() {
        view.getBtnAgregar().addActionListener(this);
        view.getBtnModificar().addActionListener(this);
        view.getBtnEliminar().addActionListener(this);
        view.getBtnListar().addActionListener(this);
        view.getBtnLimpiar().addActionListener(this);
        view.getBtnBuscar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.getBtnAgregar()) {
            agregarPelicula();
        } else if (source == view.getBtnModificar()) {
            modificarPelicula();
        } else if (source == view.getBtnEliminar()) {
            eliminarPelicula();
        } else if (source == view.getBtnListar() || source == view.getBtnBuscar()) {
            cargarPeliculas();    
        } else if (source == view.getBtnLimpiar()) {
            view.limpiarCampos();
        }
    }
    
    private void cargarPeliculas() {
        String titulo = view.getTxtBusquedaTitulo().getText();
        String genero = (String) view.getCmbBusquedaGenero().getSelectedItem();
        
        // Uso de JSpinner: obtenemos directamente enteros
        int anioMin = (int) view.getSpnBusquedaAnioMin().getValue();
        int anioMax = (int) view.getSpnBusquedaAnioMax().getValue();
        
        // No filtrar si el género es "Cualquiera"
        if (genero != null && genero.equals("Cualquiera")) {
            genero = "";
        }
        
        // Validación de rango de años 
        if (anioMin > 0 && anioMax > 0 && anioMin > anioMax) {
             UIMessages.mostrarMensaje(view, "El año mínimo no puede ser mayor que el año máximo.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
             return;
        }

        List<Pelicula> peliculas = dao.buscarPeliculas(titulo, genero, anioMin, anioMax);
        view.actualizarTabla(peliculas);
        
        if (peliculas.isEmpty() && (!Objects.toString(titulo, "").isEmpty() || !Objects.toString(genero, "").isEmpty() || anioMin > 0 || anioMax > 0)) {
              UIMessages.mostrarMensaje(view, "No se encontraron películas con los filtros especificados.", "Búsqueda Vacía", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void agregarPelicula() {
        Pelicula pelicula = obtenerPeliculaDesdeFormulario();
        if (pelicula == null) return;
        
        // MEJORA: Validación de Duplicados Rápida (Título y Año)
        List<Pelicula> duplicados = dao.buscarPeliculas(pelicula.getTitulo(), "", pelicula.getAnio(), pelicula.getAnio());
        if (!duplicados.isEmpty()) {
            UIMessages.mostrarMensaje(view, "Ya existe una película con el título y año especificados.", "Error de Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = UIMessages.confirmarOperacion(view, "¿Desea agregar la película: " + pelicula.getTitulo() + "?", "Confirmar Agregado");
        if (confirm != JOptionPane.YES_OPTION) return;
        
        if (dao.agregarPelicula(pelicula)) {
            UIMessages.mostrarMensaje(view, "Película agregada con éxito. ID asignado: " + pelicula.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
            cargarPeliculas();
        } else {
            UIMessages.mostrarMensaje(view, "Error al intentar agregar la película. Por favor, revise los logs o la conexión a la base de datos.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarPelicula() {
        String idStr = view.getTxtId().getText();
        if (idStr.trim().isEmpty()) {
            UIMessages.mostrarMensaje(view, "Debe seleccionar una película de la tabla para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Pelicula pelicula = obtenerPeliculaDesdeFormulario();
        if (pelicula == null) return;
        
        // Manejo de excepciones separadas (compatible con Java < 7)
        try {
            int id = Integer.parseInt(idStr);
            pelicula.setId(id);
        } catch (NumberFormatException ex) {
            UIMessages.mostrarMensaje(view, "Error: El ID seleccionado no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IllegalArgumentException ex) {
            UIMessages.mostrarMensaje(view, "Error al asignar ID: " + ex.getMessage(), "Error de ID", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = UIMessages.confirmarOperacion(view, "¿Está seguro de modificar la película: " + pelicula.getTitulo() + "?", "Confirmar Modificación");
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.actualizarPelicula(pelicula)) {
                UIMessages.mostrarMensaje(view, "Película modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.limpiarCampos();
                cargarPeliculas();
            } else {
                UIMessages.mostrarMensaje(view, "Error al intentar modificar la película. El ID podría no existir.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarPelicula() {
        String idStr = view.getTxtId().getText();
        if (idStr.trim().isEmpty()) {
            UIMessages.mostrarMensaje(view, "Debe seleccionar una película de la tabla para eliminar.", "Error de Eliminación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            UIMessages.mostrarMensaje(view, "El ID de la película es inválido.", "Error de ID", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = UIMessages.confirmarOperacion(view, "¿Está seguro de eliminar la película con ID: " + id + "?", "Confirmar Eliminación");
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminarPelicula(id)) {
                UIMessages.mostrarMensaje(view, "Película eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                view.limpiarCampos();
                cargarPeliculas();
            } else {
                UIMessages.mostrarMensaje(view, "Error al intentar eliminar la película. El ID podría no existir.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Pelicula obtenerPeliculaDesdeFormulario() {
        String titulo = view.getTxtTitulo().getText();
        String director = view.getTxtDirector().getText();
        String genero = (String) view.getCmbGenero().getSelectedItem();
        
        try {
            int anio = (int) view.getSpnAnio().getValue();    
            int duracion = (int) view.getSpnDuracion().getValue();    
            
            return new Pelicula(titulo, director, anio, duracion, genero);

        } catch (IllegalArgumentException e) {
            UIMessages.mostrarMensaje(view, e.getMessage(), "Error de Validación de Datos", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}