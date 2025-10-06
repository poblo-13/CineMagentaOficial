package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import model.Pelicula;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;

public class VentanaPrincipal extends JFrame {

    // CRUD
    private JTextField txtId, txtTitulo, txtDirector;
    private JSpinner spnAnio, spnDuracion;    
    private JComboBox<String> cmbGenero;        
    private JButton btnAgregar, btnModificar, btnEliminar, btnListar, btnLimpiar;
    
    // componentes tabla
    private JTable tablaPeliculas;
    private DefaultTableModel modeloTabla;
    
    // componentes de los filtros
    private JTextField txtBusquedaTitulo; 
    private JComboBox<String> cmbBusquedaGenero; 
    private JSpinner spnBusquedaAnioMin; 
    private JSpinner spnBusquedaAnioMax; 
    private JButton btnBuscar;    

    private static final String CUALQUIERA = "Cualquiera";
    private static final String[] GENEROS_CRUD = {"Acción", "Comedia", "Drama", "Sci-Fi", "Terror", "Aventura", "Musical"};
    
    // array para el filtro de genero incluyendo cualquiera
    private static final String[] GENEROS_FILTRO;
    static {
        GENEROS_FILTRO = new String[GENEROS_CRUD.length + 1];
        GENEROS_FILTRO[0] = CUALQUIERA;
        System.arraycopy(GENEROS_CRUD, 0, GENEROS_FILTRO, 1, GENEROS_CRUD.length);
    }

    public VentanaPrincipal() {
        setTitle("Administrador de Películas - CineMagenta");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // panel CRUD
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Película (CRUD)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.3; txtId = new JTextField(10); txtId.setEditable(false); panelFormulario.add(txtId, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.7; txtTitulo = new JTextField(20); panelFormulario.add(txtTitulo, gbc);

        // fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Director:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 1.0; txtDirector = new JTextField(20); panelFormulario.add(txtDirector, gbc); 
        gbc.gridwidth = 1;

        // fila 2
        int anioActual = Year.now().getValue();
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.3;    
        spnAnio = new JSpinner(new SpinnerNumberModel(anioActual, 1888, anioActual, 1)); 
        panelFormulario.add(spnAnio, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.7;    
        spnDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 400, 1)); 
        panelFormulario.add(spnDuracion, gbc);

        // fila 3 y botones CRUD
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0; panelFormulario.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.3;    
        cmbGenero = new JComboBox<>(GENEROS_CRUD);
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

        // panel central tabla y filtros
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda y Listado"));

        panelFiltros.add(new JLabel("Título Contiene:"));
        txtBusquedaTitulo = new JTextField(15);
        panelFiltros.add(txtBusquedaTitulo);

        panelFiltros.add(new JLabel("Género:"));
        cmbBusquedaGenero = new JComboBox<>(GENEROS_FILTRO); 
        panelFiltros.add(cmbBusquedaGenero);

        panelFiltros.add(new JLabel("Año Mínimo:"));
        spnBusquedaAnioMin = new JSpinner(new SpinnerNumberModel(0, 0, anioActual, 1)); 
        panelFiltros.add(spnBusquedaAnioMin);
        
        panelFiltros.add(new JLabel("Año Máximo:"));
        spnBusquedaAnioMax = new JSpinner(new SpinnerNumberModel(anioActual, 0, anioActual, 1)); 
        panelFiltros.add(spnBusquedaAnioMax);

        btnBuscar = new JButton("Buscar/Filtrar");
        panelFiltros.add(btnBuscar);
        
        panelCentral.add(panelFiltros, BorderLayout.NORTH);

        // tabla
        String[] columnas = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaPeliculas = new JTable(modeloTabla);
        tablaPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPeliculas.setAutoCreateRowSorter(true); 
        panelCentral.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // MouseListener para cargar datos al formulario
        tablaPeliculas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int viewRow = tablaPeliculas.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = tablaPeliculas.convertRowIndexToModel(viewRow); 
                    
                    txtId.setText(modeloTabla.getValueAt(modelRow, 0).toString());
                    txtTitulo.setText(modeloTabla.getValueAt(modelRow, 1).toString());
                    txtDirector.setText(modeloTabla.getValueAt(modelRow, 2).toString());
                    
                    spnAnio.setValue(Integer.parseInt(modeloTabla.getValueAt(modelRow, 3).toString()));
                    spnDuracion.setValue(Integer.parseInt(modeloTabla.getValueAt(modelRow, 4).toString()));
                    cmbGenero.setSelectedItem(modeloTabla.getValueAt(modelRow, 5).toString());
                }
            }
        });
    }

    // metodos utilitarios
    public void limpiarCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        
        spnAnio.setValue(Year.now().getValue()); 
        spnDuracion.setValue(90);    
        cmbGenero.setSelectedIndex(0);

        // limpiar filtros y dejarlos en estado inicial
        txtBusquedaTitulo.setText("");
        cmbBusquedaGenero.setSelectedIndex(0); 
        spnBusquedaAnioMin.setValue(0);
        spnBusquedaAnioMax.setValue(Year.now().getValue());
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

    // getters publicos para el controlador
    
    // filtros
    public JTextField getTxtBusquedaTitulo() { return txtBusquedaTitulo; }
    public JComboBox<String> getCmbBusquedaGenero() { return cmbBusquedaGenero; }
    public JSpinner getSpnBusquedaAnioMin() { return spnBusquedaAnioMin; }
    public JSpinner getSpnBusquedaAnioMax() { return spnBusquedaAnioMax; }
    
    // CRUD
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtDirector() { return txtDirector; }
    public JSpinner getSpnAnio() { return spnAnio; }
    public JSpinner getSpnDuracion() { return spnDuracion; }
    public JComboBox<String> getCmbGenero() { return cmbGenero; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnModificar() { return btnModificar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
}