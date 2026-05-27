package heladeria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import heladeria.model.Producto;
import heladeria.database.ProductoDAO;
import heladeria.structures.ArbolABB;
import heladeria.structures.AlgoritmosOrdenamiento;

/**
 * Ventana Principal interactiva del Sistema Luxe Gelato Heladería.
 * Conecta la Base de Datos con el ABB y los algoritmos de ordenamiento alternativo.
 */
public class MainView extends JFrame {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ArbolABB arbolABB = new ArbolABB();
    private final String usuarioActivo = "Administrador"; 

    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JTable tablaInventario;
    private DefaultTableModel modeloTablaInventario;
    private JTextField txtBuscarInventario;
    private JLabel lblDetalleClave, lblDetalleNombre, lblDetalleExistencia, lblDetalleUbicacion, lblDetallePrecio, lblDetalleFoto;
    private JTextArea txtDetalleDescripcion;

    private JTable tablaVentasCatalogo, tablaCarrito;
    private DefaultTableModel modeloTablaVentas, modeloTablaCarrito;
    private JTextField txtBuscarVentas;
    private JLabel lblSubtotalVal, lblTotalVal;

    private JTable tablaBitacora;
    private DefaultTableModel modeloTablaBitacora;

    public MainView() {
        super("Sistema de Inventario y Ventas - Heladería Luxe Gelato");
        inicializarComponentes();
        cargarDatosDesdeBD(); 
    }

    /**
     *Lee los datos de MySQL, los indexa en la estructura del ABB 
     * y genera la visualización predeterminada mediante recorrido Inorden.
     */
    private void cargarDatosDesdeBD() {
        arbolABB.limpiar(); 
        
        List<Producto> productosBD = productoDAO.listar();
        
        for (Producto p : productosBD) {
            arbolABB.insertar(p);
        }
        
        llenarTablaInventario(arbolABB.getListaInorden());
    }

    /**
     * Encargado de limpiar y repintar la JTable de Inventario con la lista proporcionada.
     */
    private void llenarTablaInventario(List<Producto> lista) {
        modeloTablaInventario.setRowCount(0); 
        for (Producto p : lista) {
            modeloTablaInventario.addRow(new Object[]{
                p.getClave(),
                p.getNombre(),
                p.getExistencia(),
                p.getUbicacion(),
                "$" + p.getPrecio(),
                p.getFoto() != null ? "[Con Imagen]" : "[Sin Imagen]"
            });
        }
        // Sincronizamos también el catálogo de ventas
        if (modeloTablaVentas != null) {
            modeloTablaVentas.setRowCount(0);
            for (Producto p : lista) {
                modeloTablaVentas.addRow(new Object[]{
                    p.getClave(), p.getNombre(), "$" + p.getPrecio(), p.getExistencia()
                });
            }
        }
    }

    private void inicializarComponentes() {
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // BARRA LATERAL (Sidebar)
        JPanel panelSidebar = new JPanel();
        panelSidebar.setBackground(new Color(245, 245, 245));
        panelSidebar.setPreferredSize(new Dimension(220, 750));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel lblLogo = new JLabel("LUXE GELATO", JLabel.CENTER);
        lblLogo.setFont(new Font("Serif", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        JLabel lblSublogo = new JLabel("HELADERÍA", JLabel.CENTER);
        lblSublogo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSublogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSublogo.setBorder(BorderFactory.createEmptyBorder(0, 10, 30, 10));

        JButton btnInicio = crearBotonSidebar("Inicio");
        JButton btnInventario = crearBotonSidebar("Inventario");
        JButton btnVentas = crearBotonSidebar("Ventas");
        JButton btnBitacora = crearBotonSidebar("Bitácora");
        JButton btnCerrarSesion = crearBotonSidebar("Cerrar sesión");

        panelSidebar.add(lblLogo);
        panelSidebar.add(lblSublogo);
        panelSidebar.add(btnInicio);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnInventario);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnVentas);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnBitacora);
        panelSidebar.add(Box.createVerticalGlue());
        panelSidebar.add(btnCerrarSesion);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        panelContenido.add(crearPanelInicio(), "CardInicio");
        panelContenido.add(crearPanelInventario(), "CardInventario");
        panelContenido.add(crearPanelVentas(), "CardVentas");
        panelContenido.add(crearPanelBitacora(), "CardBitacora");

        // Enlaces de navegación
        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "CardInicio"));
        btnInventario.addActionListener(e -> {
            cargarDatosDesdeBD(); 
            cardLayout.show(panelContenido, "CardInventario");
        });
        btnVentas.addActionListener(e -> cardLayout.show(panelContenido, "CardVentas"));
        btnBitacora.addActionListener(e -> cardLayout.show(panelContenido, "CardBitacora"));
        btnCerrarSesion.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "¿Desea cerrar la sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) this.dispose();
        });

        // BARRA  INFERIOR (Footer)
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setBackground(new Color(240, 240, 240));
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        panelFooter.setPreferredSize(new Dimension(1200, 30));

        JLabel lblUsuario = new JLabel("  Usuario: " + usuarioActivo);
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JLabel lblFechaHora = new JLabel("Fecha: 12/05/2026  |  Hora: 11:30 AM  ");
        lblFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 11));

        panelFooter.add(lblUsuario, BorderLayout.WEST);
        panelFooter.add(lblFechaHora, BorderLayout.EAST);

        add(panelSidebar, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        return btn;
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblBienvenido = new JLabel("Bienvenido al sistema", JLabel.CENTER);
        lblBienvenido.setFont(new Font("Serif", Font.BOLD, 26));
        JLabel lblSeleccione = new JLabel("Seleccione el módulo que desea utilizar", JLabel.CENTER);
        lblSeleccione.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSeleccione.setForeground(Color.GRAY);

        JPanel cardInv = new JPanel(new BorderLayout());
        cardInv.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        cardInv.setPreferredSize(new Dimension(250, 200));
        cardInv.setBackground(new Color(252, 252, 252));
        JButton btnGoInv = new JButton("INVENTARIO");
        btnGoInv.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGoInv.setFocusPainted(false);
        btnGoInv.addActionListener(e -> cardLayout.show(panelContenido, "CardInventario"));
        JTextArea txtDescInv = new JTextArea("Administra los productos,\nexistencias y el inventario\nde la heladería.");
        txtDescInv.setEditable(false);
        txtDescInv.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDescInv.setBackground(new Color(252, 252, 252));
        cardInv.add(btnGoInv, BorderLayout.NORTH);
        cardInv.add(txtDescInv, BorderLayout.CENTER);

        JPanel cardVen = new JPanel(new BorderLayout());
        cardVen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        cardVen.setPreferredSize(new Dimension(250, 200));
        cardVen.setBackground(new Color(252, 252, 252));
        JButton btnGoVen = new JButton("VENTAS");
        btnGoVen.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGoVen.setFocusPainted(false);
        btnGoVen.addActionListener(e -> cardLayout.show(panelContenido, "CardVentas"));
        JTextArea txtDescVen = new JTextArea("Realiza ventas de productos\ny genera comprobantes\nde compra.");
        txtDescVen.setEditable(false);
        txtDescVen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDescVen.setBackground(new Color(252, 252, 252));
        cardVen.add(btnGoVen, BorderLayout.NORTH);
        cardVen.add(txtDescVen, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(lblBienvenido, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 40, 0);
        panel.add(lblSeleccione, gbc);
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 20, 0, 20);
        panel.add(cardInv, gbc);
        gbc.gridx = 1;
        panel.add(cardVen, gbc);

        return panel;
    }

    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelTituloText = new JPanel(new GridLayout(2, 1));
        panelTituloText.setBackground(Color.WHITE);
        JLabel title = new JLabel("Inventario de Productos");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Administra los productos de la heladería");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        panelTituloText.add(title);
        panelTituloText.add(subtitle);

        txtBuscarInventario = new JTextField(20);
        txtBuscarInventario.setText("");
        txtBuscarInventario.setBorder(BorderFactory.createTitledBorder("Buscar producto por clave o nombre:"));

        /**
         * LÓGICA DE BÚSQUEDA INTERACTIVA MEDIANTE ABB
         */
        txtBuscarInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String criterio = txtBuscarInventario.getText().trim();
                if (criterio.isEmpty()) {
                    llenarTablaInventario(arbolABB.getListaInorden()); 
                } else {
                    List<Producto> resultados = new ArrayList<>();
                    if (criterio.matches("\\d+")) { 
                        Producto p = arbolABB.buscarPorClave(criterio);
                        if (p != null) resultados.add(p);
                    } else { 
                        resultados = arbolABB.buscarPorNombre(criterio);
                    }
                    llenarTablaInventario(resultados);
                }
            }
        });

        panelHeader.add(panelTituloText, BorderLayout.WEST);
        panelHeader.add(txtBuscarInventario, BorderLayout.EAST);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelAcciones.setBackground(Color.WHITE);

        JButton btnAgregar = new JButton("+ Agregar");
        btnAgregar.addActionListener(e -> {
            ProductoForm formulario = new ProductoForm(this, true);
            formulario.setVisible(true);
            if (formulario.isGuardadoExitoso()) {
                cargarDatosDesdeBD(); 
            }
        });
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            int filaSel = tablaInventario.getSelectedRow();
            if (filaSel == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Sacar la clave de la tabla y buscar el objeto completo en el Árbol
            String clave = tablaInventario.getValueAt(filaSel, 0).toString();
            Producto productoSeleccionado = arbolABB.buscarPorClave(clave);

            if (productoSeleccionado != null) {
                EliminarForm formEliminar = new EliminarForm(this, true, productoSeleccionado);
                formEliminar.setVisible(true);
                
                // Si sí lo borró, recargamos la tabla desde la BD
                if (formEliminar.isEliminadoExitoso()) {
                    cargarDatosDesdeBD();
                }
            }
        });
        JButton btnQuickSort = new JButton("Ordenar por nombre (QuickSort)");
        JButton btnMergeSort = new JButton("Ordenar por precio (MergeSort)");

        /**
         * ENLACE DE ALGORITMOS DE ORDENAMIENTO USANDO LISTAS
         */
        btnQuickSort.addActionListener(e -> {
            List<Producto> lista = arbolABB.getListaInorden();
            AlgoritmosOrdenamiento.ordenarPorNombre(lista); 
            llenarTablaInventario(lista);
        });

        btnMergeSort.addActionListener(e -> {
            List<Producto> lista = arbolABB.getListaInorden();
            AlgoritmosOrdenamiento.ordenarPorPrecio(lista); 
            llenarTablaInventario(lista);
        });

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnQuickSort);
        panelAcciones.add(btnMergeSort);

        String[] columnas = {"Clave (9 dígitos)", "Producto", "Existencia", "Ubicación", "Precio", "Foto"};
        modeloTablaInventario = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaInventario = new JTable(modeloTablaInventario);
        JScrollPane scrollTabla = new JScrollPane(tablaInventario);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de productos (Orden por defecto: Inorden del ABB)"));

        /**
         * EVENTO DE SELECCIÓN DE FILA: Carga dinámicamente el panel de detalle derecho
         */
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaInventario.getSelectedRow();
                if (fila != -1) {
                    String clave = tablaInventario.getValueAt(fila, 0).toString();
                    Producto p = arbolABB.buscarPorClave(clave); 
                    if (p != null) {
                        lblDetalleClave.setText("Clave: " + p.getClave());
                        lblDetalleNombre.setText("Producto: " + p.getNombre());
                        lblDetalleExistencia.setText("Existencia: " + p.getExistencia());
                        lblDetalleUbicacion.setText("Ubicación: " + p.getUbicacion());
                        lblDetallePrecio.setText("Precio: $" + p.getPrecio());
                        txtDetalleDescripcion.setText("Sabor artesanal premium elaborado con ingredientes 100% naturales.");
                        if (p.getFoto() != null && p.getFoto().length > 0) {
                            ImageIcon iconoOriginal = new ImageIcon(p.getFoto());
                            Image imagenMatriz = iconoOriginal.getImage();

                            Image imagenEscalada = imagenMatriz.getScaledInstance(200, 150, Image.SCALE_SMOOTH);

                            lblDetalleFoto.setIcon(new ImageIcon(imagenEscalada));
                            lblDetalleFoto.setText(""); 
                        } else {
                            lblDetalleFoto.setIcon(null);
                            lblDetalleFoto.setText("[Sin Imagen]");
                        }
                    }
                }
            }
        });

        // Panel lateral derecho para inspección detallada
        JPanel panelDetalle = new JPanel();
        panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));
        panelDetalle.setPreferredSize(new Dimension(280, 500));
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Detalle del producto"));
        panelDetalle.setBackground(Color.WHITE);

        lblDetalleFoto = new JLabel("[Sin Imagen]", JLabel.CENTER);
        lblDetalleFoto.setPreferredSize(new Dimension(200, 150));
        lblDetalleFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblDetalleFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDetalleClave = new JLabel("Clave: -");
        lblDetalleNombre = new JLabel("Producto: -");
        lblDetalleExistencia = new JLabel("Existencia: -");
        lblDetalleUbicacion = new JLabel("Ubicación: -");
        lblDetallePrecio = new JLabel("Precio: -");
        txtDetalleDescripcion = new JTextArea(4, 20);
        txtDetalleDescripcion.setEditable(false);
        txtDetalleDescripcion.setLineWrap(true);
        txtDetalleDescripcion.setBorder(BorderFactory.createTitledBorder("Descripción:"));

        panelDetalle.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDetalle.add(lblDetalleFoto);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 15)));
        panelDetalle.add(lblDetalleClave);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 8)));
        panelDetalle.add(lblDetalleNombre);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 8)));
        panelDetalle.add(lblDetalleExistencia);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 8)));
        panelDetalle.add(lblDetalleUbicacion);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 8)));
        panelDetalle.add(lblDetallePrecio);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDetalle.add(new JScrollPane(txtDetalleDescripcion));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelCentral.add(panelDetalle, BorderLayout.EAST);

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(panelAcciones, BorderLayout.CENTER);
        panel.add(panelCentral, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel title = new JLabel("Módulo de Ventas");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel sub = new JLabel("Busque productos, agréguelos al carrito y realice la venta.");
        header.add(title);
        header.add(sub);

        JPanel panelGrid = new JPanel(new GridLayout(1, 2, 15, 0));
        panelGrid.setBackground(Color.WHITE);

        // --- SECCIÓN 1: CATÁLOGO ---
        JPanel panelCatalogo = new JPanel(new BorderLayout());
        panelCatalogo.setBorder(BorderFactory.createTitledBorder("1. Buscar productos (Doble clic para agregar)"));
        txtBuscarVentas = new JTextField();
        txtBuscarVentas.setBorder(BorderFactory.createTitledBorder("Buscar por clave o nombre:"));
        
        String[] colCat = {"Clave", "Producto", "Precio", "Stock"}; 
        // Hacemos que la tabla del catálogo no sea editable
        modeloTablaVentas = new DefaultTableModel(colCat, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaVentasCatalogo = new JTable(modeloTablaVentas);
        panelCatalogo.add(txtBuscarVentas, BorderLayout.NORTH);
        panelCatalogo.add(new JScrollPane(tablaVentasCatalogo), BorderLayout.CENTER);

        // --- SECCIÓN 2: CARRITO ---
        JPanel panelCarritoContenedor = new JPanel(new BorderLayout());
        panelCarritoContenedor.setBorder(BorderFactory.createTitledBorder("2. Carrito de compra"));
        panelCarritoContenedor.setBackground(Color.WHITE);

        String[] colCart = {"Clave", "Producto", "Cantidad", "Importe"}; 
        // Hacemos que la tabla del carrito tampoco sea editable
        modeloTablaCarrito = new DefaultTableModel(colCart, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);

        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        lblSubtotalVal = new JLabel("Subtotal: $0.00"); 
        lblTotalVal = new JLabel("Total a pagar: $0.00"); 
        lblTotalVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton btnCobrar = new JButton("Realizar venta"); 
        btnCobrar.setBackground(new Color(40, 167, 69));
        btnCobrar.setForeground(Color.WHITE);
        JButton btnCancelar = new JButton("Vaciar carrito"); 

        c.gridx = 0; c.gridy = 0; c.insets = new Insets(5, 5, 5, 5); c.anchor = GridBagConstraints.WEST;
        panelTotales.add(lblSubtotalVal, c);
        c.gridy = 1;
        panelTotales.add(lblTotalVal, c);
        c.gridx = 1; c.gridy = 0; c.gridheight = 2; c.fill = GridBagConstraints.VERTICAL;
        panelTotales.add(btnCobrar, c);
        c.gridx = 2;
        panelTotales.add(btnCancelar, c);

        panelCarritoContenedor.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panelCarritoContenedor.add(panelTotales, BorderLayout.SOUTH);

        panelGrid.add(panelCatalogo);
        panelGrid.add(panelCarritoContenedor);
        panel.add(header, BorderLayout.NORTH);
        panel.add(panelGrid, BorderLayout.CENTER);

        // --- EVENTOS DE INTERACTIVIDAD ---

        // 1. Doble clic en el catálogo para pedir cantidad y pasar al carrito
        tablaVentasCatalogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Si hace doble clic
                    int fila = tablaVentasCatalogo.getSelectedRow();
                    if (fila != -1) {
                        String clave = modeloTablaVentas.getValueAt(fila, 0).toString();
                        String nombre = modeloTablaVentas.getValueAt(fila, 1).toString();
                        double precio = Double.parseDouble(modeloTablaVentas.getValueAt(fila, 2).toString().replace("$", ""));
                        int stockDisp = Integer.parseInt(modeloTablaVentas.getValueAt(fila, 3).toString());

                        if (stockDisp <= 0) {
                            JOptionPane.showMessageDialog(panel, "Producto agotado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String cantStr = JOptionPane.showInputDialog(panel, "¿Cuántos helados de '" + nombre + "' desea agregar?", "Cantidad", JOptionPane.QUESTION_MESSAGE);
                        if (cantStr != null && !cantStr.trim().isEmpty()) {
                            try {
                                int cantidad = Integer.parseInt(cantStr);
                                if (cantidad > 0 && cantidad <= stockDisp) {
                                    double importe = precio * cantidad;
                                    modeloTablaCarrito.addRow(new Object[]{clave, nombre, cantidad, "$" + importe});
                                    actualizarTotalesCarrito();
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Cantidad no válida o supera la existencia.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(panel, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        // 2. Botón para vaciar el carrito
        btnCancelar.addActionListener(e -> {
            modeloTablaCarrito.setRowCount(0);
            actualizarTotalesCarrito();
        });
        
        // 3. Botón para procesar la venta
        btnCobrar.addActionListener(e -> {
            if (modeloTablaCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "El carrito está vacío. Agregue productos primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Extraemos el total a cobrar limpiando el texto del label
            double totalCobro = Double.parseDouble(lblTotalVal.getText().replace("Total a pagar: $", ""));
            
            // Instanciamos nuestro nuevo DAO
            heladeria.database.VentaDAO ventaDAO = new heladeria.database.VentaDAO();
            
            // Mandamos a guardar la venta con el usuario logueado
            if (ventaDAO.registrarVentaCompleta(totalCobro, usuarioActivo, modeloTablaCarrito)) {
                JOptionPane.showMessageDialog(panel, "¡Venta realizada con éxito!\nTotal cobrado: $" + String.format("%.2f", totalCobro), "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                
                // Vaciamos el carrito
                modeloTablaCarrito.setRowCount(0);
                actualizarTotalesCarrito();
                
                // Recargamos los datos desde la BD para que el catálogo de ventas y el inventario reflejen el nuevo stock
                cargarDatosDesdeBD();
            } else {
                JOptionPane.showMessageDialog(panel, "Ocurrió un error al procesar la venta.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelBitacora() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Bitácora de Eliminaciones");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Registro de los productos eliminados del sistema.");

        JPanel panelHeader = new JPanel(new GridLayout(2, 1));
        panelHeader.setBackground(Color.WHITE);
        panelHeader.add(title);
        panelHeader.add(subtitle);

        String[] colBitacora = {"Clave (9 dígitos)", "Producto", "Razón de eliminación", "Fecha de eliminación", "Usuario que eliminó"};
        modeloTablaBitacora = new DefaultTableModel(colBitacora, 0);
        tablaBitacora = new JTable(modeloTablaBitacora);
        JScrollPane scroll = new JScrollPane(tablaBitacora);
        scroll.setBorder(BorderFactory.createTitledBorder("Productos eliminados"));

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
    
    // Método para calcular el subtotal y total del carrito automáticamente
    private void actualizarTotalesCarrito() {
        double total = 0.0;
        for (int i = 0; i < modeloTablaCarrito.getRowCount(); i++) {
            // Obtenemos el importe de la columna 3 (que tiene el símbolo $) y lo limpiamos
            String importeStr = modeloTablaCarrito.getValueAt(i, 3).toString().replace("$", "");
            total += Double.parseDouble(importeStr);
        }
        lblSubtotalVal.setText("Subtotal: $" + String.format("%.2f", total));
        lblTotalVal.setText("Total a pagar: $" + String.format("%.2f", total));
    }
}