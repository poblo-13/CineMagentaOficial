package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import model.Pelicula;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

public class VentanaPrincipal extends JFrame {

    // componentes para el formulario CRUD
    public JTextField txtId, txtTitulo, txtDirector;
    public JSpinner spnAnio, spnDuracion; 
    public JComboBox<String> cmbGenero;    
    
    public JButton btnAgregar, btnModificar, btnEliminar, btnListar, btnLimpiar;
    
    // componentes para la tabla y el modelo
    public JTable tablaPeliculas;
    public DefaultTableModel modeloTabla;
    
    // componentes para los filtros de busqueda
    public JTextField txtBusqueda; 
    public JButton btnBuscar; 
    public JTextField txtBusquedaGenero;
    public JTextField txtBusquedaAnioMin;
    public JTextField txtBusquedaAnioMax;

    // opciones fijas para el JComboBox de genero
    private static final String[] GENEROS = {"Acción", "Comedia", "Drama", "Sci-Fi", "Terror", "Aventura", "Musical"};

    public VentanaPrincipal() {
        setTitle("Administrador de Películas - CineMagenta (Mejorado)");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Película (CRUD)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // fila 0 ID y titulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.3; txtId = new JTextField(10); txtId.setEditable(false); panelFormulario.add(txtId, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.7; txtTitulo = new JTextField(10); panelFormulario.add(txtTitulo, gbc);

        // fila 1 director
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Director:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 1.0; txtDirector = new JTextField(3); panelFormulario.add(txtDirector, gbc);
        gbc.gridwidth = 1;

        // fila 2 año y duracion 
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.3; 
        spnAnio = new JSpinner(new SpinnerNumberModel(anioActual, 1888, anioActual, 1)); // rango logico 1888 a hoy
        panelFormulario.add(spnAnio, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.7; 
        spnDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 400, 1)); // duracion positiva max 400
        panelFormulario.add(spnDuracion, gbc);

        // fila 3 genero y botones CRUD
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.3; 
        cmbGenero = new JComboBox<>(GENEROS);
        panelFormulario.add(cmbGenero, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnListar = new JButton("Listar Todo"); 
        btnLimpiar = new JButton("Limpiar Campos"); 
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnLimpiar);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda y Listado"));

        panelFiltros.add(new JLabel("Título:"));
        txtBusqueda = new JTextField(15);
        panelFiltros.add(txtBusqueda);

        panelFiltros.add(new JLabel("Género:"));
        txtBusquedaGenero = new JTextField(15);
        panelFiltros.add(txtBusquedaGenero);

        panelFiltros.add(new JLabel("Años (Min/Max):"));
        txtBusquedaAnioMin = new JTextField(5);
        panelFiltros.add(txtBusquedaAnioMin);
        
        panelFiltros.add(new JLabel("/"));
        txtBusquedaAnioMax = new JTextField(5);
        panelFiltros.add(txtBusquedaAnioMax);

        btnBuscar = new JButton("Buscar/Filtrar");
        panelFiltros.add(btnBuscar);
        
        panelCentral.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPeliculas = new JTable(modeloTabla);
        tablaPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCentral.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // MouseListener 
        tablaPeliculas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaPeliculas.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                    txtTitulo.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                    txtDirector.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                    
                    // cargar valores en JSpinner y JComboBox
                    spnAnio.setValue(Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 3).toString()));
                    spnDuracion.setValue(Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 4).toString()));
                    cmbGenero.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
                }
            }
        });
    }

    // metodos utilitarios
    public void limpiarCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        
        // resetear a valores por defecto o iniciales
        spnAnio.setValue(Calendar.getInstance().get(Calendar.YEAR));
        spnDuracion.setValue(90); 
        cmbGenero.setSelectedIndex(0);

        // limpiar filtros
        txtBusqueda.setText("");
        txtBusquedaGenero.setText("");
        txtBusquedaAnioMin.setText("");
        txtBusquedaAnioMax.setText("");
    }

    public void actualizarTabla(List<Pelicula> peliculas) {
        modeloTabla.setRowCount(0); 
        for (Pelicula p : peliculas) {
            Object[] fila = {
                p.getId(), p.getTitulo(), p.getDirector(),
                p.getAnio(), p.getDuracion(), p.getGenero()
            };
            modeloTabla.addRow(fila);
        }
    }

    // Getters para el controlador
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtDirector() { return txtDirector; }
    
    public JSpinner getSpnAnio() { return spnAnio; }
    public JSpinner getSpnDuracion() { return spnDuracion; }
    public JComboBox<String> getCmbGenero() { return cmbGenero; }
    
    // Getters para los filtros
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JTextField getTxtBusquedaGenero() { return txtBusquedaGenero; }
    public JTextField getTxtBusquedaAnioMin() { return txtBusquedaAnioMin; }
    public JTextField getTxtBusquedaAnioMax() { return txtBusquedaAnioMax; }
    
    // Getters de Botones
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
}