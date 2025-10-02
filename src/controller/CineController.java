package controller;

import view.VentanaPrincipal;
import persistence.PeliculaDAO;
import model.Pelicula;
import util.UIMessages;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import java.time.Year;
import java.util.Calendar;

public class CineController implements ActionListener {

    private VentanaPrincipal view;
    private PeliculaDAO dao;

    public CineController(VentanaPrincipal view, PeliculaDAO dao) {
        this.view = view;
        this.dao = dao;
        // 1 inicializar la vista mostrarla
        this.view.setVisible(true);
        // 2 cargar datos iniciales
        cargarPeliculas(); 
        // 3 asignar Listeners a los botones
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
            // conectar la accion de listar todo y buscar filtrar
            cargarPeliculas(); 
        } else if (source == view.getBtnLimpiar()) {
            view.limpiarCampos();
        }
    }
    
    private void cargarPeliculas() {
        String titulo = view.getTxtBusqueda().getText();
        String genero = view.getTxtBusquedaGenero().getText();
        String anioMinStr = view.getTxtBusquedaAnioMin().getText();
        String anioMaxStr = view.getTxtBusquedaAnioMax().getText();
        
        int anioMin = 0;
        int anioMax = 0;
        
        try {
            if (!anioMinStr.trim().isEmpty()) anioMin = Integer.parseInt(anioMinStr.trim());
            if (!anioMaxStr.trim().isEmpty()) anioMax = Integer.parseInt(anioMaxStr.trim());
        } catch (NumberFormatException e) {
            UIMessages.mostrarMensaje(view, "El año de búsqueda debe ser un número entero válido.", "Error de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Pelicula> peliculas = dao.buscarPeliculas(titulo, genero, anioMin, anioMax);
        view.actualizarTabla(peliculas);
        
        if (peliculas.isEmpty() && (!titulo.isEmpty() || !genero.isEmpty() || anioMin > 0 || anioMax > 0)) {
             UIMessages.mostrarMensaje(view, "No se encontraron películas con los filtros especificados.", "Búsqueda Vacía", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void agregarPelicula() {
        // pedir confirmacion
        int confirm = UIMessages.confirmarOperacion(view, "¿Desea agregar esta película?", "Confirmar Agregado");
        if (confirm != JOptionPane.YES_OPTION) return;

        // obtener pelicula
        Pelicula pelicula = obtenerPeliculaDesdeFormulario();
        if (pelicula == null) return; // si la validacion falla sale
        
        if (dao.agregarPelicula(pelicula)) {
            UIMessages.mostrarMensaje(view, "Película agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
            cargarPeliculas();
        } else {
            UIMessages.mostrarMensaje(view, "Error al intentar agregar la película. El título y año podrían estar duplicados.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarPelicula() {
        // 1 obtener ID y validar que este presente
        String idStr = view.getTxtId().getText();
        if (idStr.trim().isEmpty()) {
            UIMessages.mostrarMensaje(view, "Debe seleccionar una película de la tabla para modificar.", "Error de Modificación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2 obtener el objeto pelicula
        Pelicula pelicula = obtenerPeliculaDesdeFormulario();
        if (pelicula == null) return; // si la validacion falla sale
        
        // 3 asignar el ID
        try {
            pelicula.setId(Integer.parseInt(idStr));
        } catch (NumberFormatException ex) {
            UIMessages.mostrarMensaje(view, "El ID de la película es inválido.", "Error de ID", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4 confirmacion y ejecución de la mod
        int confirm = UIMessages.confirmarOperacion(view, "¿Está seguro de modificar esta película?", "Confirmar Modificación");
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
        // 1 obtener ID y validar que este presente
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

        // 2 confirmacion y ejecucion de la eliminacion
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
        // 1 obtener datos de la Vista
        String titulo = view.getTxtTitulo().getText().trim();
        String director = view.getTxtDirector().getText().trim();
        String genero = (String) view.getCmbGenero().getSelectedItem();
        
        // 2 intentar crear el objeto dejando que el constructor del modelo maneje la validacion
        try {
            int anio = (int) view.getSpnAnio().getValue(); 
            int duracion = (int) view.getSpnDuracion().getValue(); 
            
            // la linea siguiente lanza IllegalArgumentException si algun dato falla la validacion
            return new Pelicula(titulo, director, anio, duracion, genero);

        } catch (IllegalArgumentException e) {
            // 3 captura la excepcion de validacion y la muestra al usuario
            UIMessages.mostrarMensaje(view, e.getMessage(), "Error de Validación de Datos", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}