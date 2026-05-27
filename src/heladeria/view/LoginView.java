package heladeria.view;

import javax.swing.*;
import java.awt.*;
import heladeria.database.UsuarioDAO;

/**
 * Ventana de inicio de sesión para la Heladería Luxe Gelato.
 * Diseñada por código puro para garantizar compatibilidad total en NetBeans.
 */
public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;

    public LoginView() {
        super("Luxe Gelato - Inicio de Sesión");
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Tarjeta contenedor central para el formulario
        JPanel panelCard = new JPanel(new GridBagLayout());
        panelCard.setBackground(Color.WHITE);
        panelCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Logo Principal del Prototipo
        JLabel lblLogo = new JLabel("LUXE GELATO", JLabel.CENTER);
        lblLogo.setFont(new Font("Serif", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0;
        panelCard.add(lblLogo, gbc);

        // Subtítulo institucional
        JLabel lblSublogo = new JLabel("HELADERÍA", JLabel.CENTER);
        lblSublogo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSublogo.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panelCard.add(lblSublogo, gbc);

        // Espaciador estético
        gbc.gridy = 2;
        panelCard.add(Box.createRigidArea(new Dimension(0, 15)), gbc);

        // Encabezado de la sección de login
        JLabel lblInicioSesion = new JLabel("INICIO DE SESIÓN", JLabel.CENTER);
        lblInicioSesion.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblInicioSesion.setForeground(new Color(50, 50, 50));
        gbc.gridy = 3;
        panelCard.add(lblInicioSesion, gbc);

        // Etiqueta de Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 4; gbc.insets = new Insets(12, 0, 2, 0);
        panelCard.add(lblUsuario, gbc);

        // Campo de Texto para Usuario
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 8, 0);
        panelCard.add(txtUsuario, gbc);

        // Etiqueta de Contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 6; gbc.insets = new Insets(8, 0, 2, 0);
        panelCard.add(lblPassword, gbc);

        // Campo de Texto Oculto para Contraseña
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 20, 0);
        panelCard.add(txtPassword, gbc);

        // Botón de Iniciar Sesión
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciarSesion.setBackground(new Color(33, 33, 33)); // Tono oscuro industrial
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 8; gbc.insets = new Insets(5, 0, 10, 0);
        panelCard.add(btnIniciarSesion, gbc);

        // Eslogan al pie de la tarjeta
        JLabel lblSlogan = new JLabel("Gestión eficiente para una experiencia única", JLabel.CENTER);
        lblSlogan.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblSlogan.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 9; gbc.insets = new Insets(25, 0, 0, 0);
        panelCard.add(lblSlogan, gbc);

        // Evento de escucha del botón para ejecutar la validación
        btnIniciarSesion.addActionListener(e -> procederAlLogin());

        add(panelCard, BorderLayout.CENTER);
    }

    /**
     * Valida las credenciales ingresadas con la persistencia en base de datos.
     */
    private void procederAlLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        // Validación directa contra las tablas de MySQL
        if (usuarioDAO.validarUsuario(usuario, password)) {
            JOptionPane.showMessageDialog(this, "¡Acceso concedido! Bienvenido al sistema.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            MainView sistemaPrincipal = new MainView();
            sistemaPrincipal.setVisible(true);
            
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}