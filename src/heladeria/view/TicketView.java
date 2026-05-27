package heladeria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Ventana emergente que genera y muestra el comprobante de compra (Ticket).
 */
public class TicketView extends JDialog {

    private final DefaultTableModel carrito;
    private final double total;
    private final String vendedor;

    public TicketView(Frame padre, boolean modal, DefaultTableModel carrito, double total, String vendedor) {
        super(padre, "Comprobante de Venta", modal);
        this.carrito = carrito;
        this.total = total;
        this.vendedor = vendedor;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(350, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Área de texto con fuente de ticket (monospaced)
        JTextArea txtTicket = new JTextArea();
        txtTicket.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtTicket.setEditable(false);
        txtTicket.setBackground(new Color(253, 253, 245)); // Color papel ticket
        txtTicket.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Construcción del texto del ticket
        String separador = "----------------------------------\n";
        StringBuilder sb = new StringBuilder();
        
        sb.append("==================================\n");
        sb.append("           LUXE GELATO            \n");
        sb.append("==================================\n");
        
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        sb.append("Fecha: ").append(fechaHora).append("\n");
        sb.append("Le atendió: ").append(vendedor).append("\n");
        sb.append(separador);
        sb.append("CANT | PRODUCTO       | IMPORTE   \n");
        sb.append(separador);

        // Recorrer los productos vendidos
        for (int i = 0; i < carrito.getRowCount(); i++) {
            String cant = carrito.getValueAt(i, 2).toString();
            String prod = carrito.getValueAt(i, 1).toString();
            String importe = carrito.getValueAt(i, 3).toString();
            
            // Recortar nombres muy largos para que no deformen el ticket
            if (prod.length() > 14) prod = prod.substring(0, 14);
            
            sb.append(String.format("%-4s | %-14s | %s\n", cant, prod, importe));
        }

        sb.append(separador);
        sb.append(String.format("TOTAL A PAGAR:        $%.2f\n", total));
        sb.append("==================================\n");
        sb.append("    ¡Gracias por su compra!       \n");

        txtTicket.setText(sb.toString());

        // Botón para cerrar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(Color.WHITE);
        JButton btnCerrar = new JButton("Cerrar Comprobante");
        btnCerrar.setBackground(new Color(33, 33, 33));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        panelBoton.add(btnCerrar);

        add(new JScrollPane(txtTicket), BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        // Opcional: Guardar el ticket físicamente en un archivo .txt
        guardarTicketFisico(sb.toString());
    }

    private void guardarTicketFisico(String contenido) {
        String nombreArchivo = "Ticket_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(contenido);
        } catch (Exception e) {
            System.err.println("No se pudo guardar el archivo físico del ticket.");
        }
    }
}