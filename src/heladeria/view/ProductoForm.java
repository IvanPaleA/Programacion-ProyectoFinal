package heladeria.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import heladeria.model.Producto;
import heladeria.database.ProductoDAO;

/**
 * Formulario emergente para la inserción y edición de productos.
 * Cumple con los requerimientos de validación de clave e imágenes relacionales.
 */
public class ProductoForm extends JDialog {

    private JTextField txtClave, txtNombre, txtExistencia, txtPrecio;
    private JComboBox<String> cmbUbicacion;
    private JTextArea txtDescripcion;
    private JLabel lblFotoEstado;
    private byte[] bytesFoto = null;
    private boolean guardadoExitoso = false;
    private boolean modoEdicion = false;

    public ProductoForm(Frame padre, boolean modal) {
        super(padre, "Agregar / Editar Producto", modal);
        inicializarComponentes();
    }
    
    // Constructor secundario para MODO EDICIÓN
    public ProductoForm(Frame padre, boolean modal, Producto p) {
        super(padre, "Editar Producto", modal);
        this.modoEdicion = true;
        inicializarComponentes();
        
        // Llenar los campos con los datos del producto seleccionado
        txtClave.setText(p.getClave());
        txtClave.setEditable(false); // Bloqueamos la clave para que no la rompan
        txtClave.setBackground(new Color(230, 230, 230));
        
        txtNombre.setText(p.getNombre());
        txtExistencia.setText(String.valueOf(p.getExistencia()));
        cmbUbicacion.setSelectedItem(p.getUbicacion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        
        // Conservar la foto original si existe
        if (p.getFoto() != null) {
            this.bytesFoto = p.getFoto();
        }
    }

    private void inicializarComponentes() {
        setSize(500, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        //Clave (9 dígitos)
        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Clave (9 dígitos):"), gbc);
        txtClave = new JTextField();
        txtClave.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelCampos.add(txtClave, gbc);

        //Nombre del producto
        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre del producto:"), gbc);
        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelCampos.add(txtNombre, gbc);

        //Existencia
        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Existencia:"), gbc);
        txtExistencia = new JTextField();
        txtExistencia.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelCampos.add(txtExistencia, gbc);

        //Ubicación
        // 4. Campo: Ubicación con menú desplegable
        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Ubicación exacta:"), gbc);
        String[] opcionesUbicacion = {"Estante", "Almacén",};
        cmbUbicacion = new JComboBox<>(opcionesUbicacion);
        cmbUbicacion.setPreferredSize(new Dimension(200, 30));
        cmbUbicacion.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panelCampos.add(cmbUbicacion, gbc);

        //Precio de venta
        gbc.gridx = 0; gbc.gridy = 4;
        panelCampos.add(new JLabel("Precio de venta:"), gbc);
        txtPrecio = new JTextField();
        txtPrecio.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelCampos.add(txtPrecio, gbc);

        //Foto del producto
        gbc.gridx = 0; gbc.gridy = 5;
        panelCampos.add(new JLabel("Foto del producto:"), gbc);
        JButton btnCargarFoto = new JButton("Seleccionar Imagen");
        gbc.gridx = 1;
        panelCampos.add(btnCargarFoto, gbc);

        lblFotoEstado = new JLabel("No se ha seleccionado archivo", JLabel.CENTER);
        lblFotoEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFotoEstado.setForeground(Color.GRAY);
        gbc.gridy = 6;
        panelCampos.add(lblFotoEstado, gbc);

        //Descripción
        gbc.gridx = 0; gbc.gridy = 7;
        panelCampos.add(new JLabel("Descripción (opcional):"), gbc);
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        panelCampos.add(scrollDesc, gbc);

        //Guardar / Cancelar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(33, 33, 33));
        btnGuardar.setForeground(Color.WHITE);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnCargarFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            int resultado = selector.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(archivo)) {
                    bytesFoto = new byte[(int) archivo.length()];
                    fis.read(bytesFoto);
                    lblFotoEstado.setText("Imagen cargada: " + archivo.getName());
                    lblFotoEstado.setForeground(new Color(40, 167, 69));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al procesar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> ejecutarGuardado());
    }

    private void ejecutarGuardado() {
        String clave = txtClave.getText().trim();
        String nombre = txtNombre.getText().trim();
        String existenciaStr = txtExistencia.getText().trim();
        String ubicacion = cmbUbicacion.getSelectedItem().toString();
        String precioStr = txtPrecio.getText().trim();

        if (!clave.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "La clave debe contener exactamente 9 dígitos numéricos.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombre.isEmpty() || existenciaStr.isEmpty() || ubicacion.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int existencia = Integer.parseInt(existenciaStr);
            double precio = Double.parseDouble(precioStr);

            // Crear el objeto e invocar el DAO
            Producto nuevoProducto = new Producto(clave, nombre, existencia, ubicacion, precio, bytesFoto);
            ProductoDAO dao = new ProductoDAO();
            
            boolean operacionExitosa;
            if (modoEdicion) {
                operacionExitosa = dao.actualizar(nuevoProducto);
            } else {
                operacionExitosa = dao.insertar(nuevoProducto);
            }

            if (operacionExitosa) {
                JOptionPane.showMessageDialog(this, "¡Operación realizada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                guardadoExitoso = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error de persistencia en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Existencia y Precio deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}