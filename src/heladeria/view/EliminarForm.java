package heladeria.view;

import javax.swing.*;
import java.awt.*;
import heladeria.model.Producto;
import heladeria.database.ProductoDAO;

public class EliminarForm extends JDialog {

    private boolean eliminadoExitoso = false;
    private Producto productoAEliminar;

    public EliminarForm(Frame padre, boolean modal, Producto producto) {
        super(padre, "Confirmar Eliminación", modal);
        this.productoAEliminar = producto;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Redujimos el tamaño de la ventana al quitar el cuadro de texto
        setSize(400, 250); 
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título directo y sin subtítulos de bitácora
        JLabel lblTitulo = new JLabel("¿Desea eliminar este producto definitivamente?");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 15, 0);
        panelCentral.add(lblTitulo, gbc);

        // Panel de información del producto
        JPanel panelInfo = new JPanel(new GridLayout(4, 1));
        panelInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelInfo.setBackground(new Color(250, 250, 250));
        panelInfo.add(new JLabel("  Clave: " + productoAEliminar.getClave()));
        panelInfo.add(new JLabel("  Producto: " + productoAEliminar.getNombre()));
        panelInfo.add(new JLabel("  Existencia: " + productoAEliminar.getExistencia()));
        panelInfo.add(new JLabel("  Precio: $" + productoAEliminar.getPrecio()));

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0);
        panelCentral.add(panelInfo, gbc);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69)); // Color rojo de advertencia
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnEliminar);

        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnCancelar.addActionListener(e -> dispose());
        
        btnEliminar.addActionListener(e -> {
            ProductoDAO dao = new ProductoDAO();
            // Mandamos una razón automática ("Eliminación directa") para que la BD no marque error
            if (dao.eliminarConBitacora(productoAEliminar, "Eliminación directa", "Administrador")) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                eliminadoExitoso = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isEliminadoExitoso() {
        return eliminadoExitoso;
    }
}