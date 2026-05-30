package heladeria.view;

import javax.swing.*;
import java.awt.*;
import heladeria.database.UsuarioDAO;


public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JRadioButton rbEmpleado, rbCliente;

    public LoginView() {
        super("Luxe Gelato - Inicio de Sesión");
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo crema estilo Pizzatron
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(253, 246, 227)); 
        panelPrincipal.setLayout(null);
        setContentPane(panelPrincipal);

        // Título
        JLabel lblTitulo = new JLabel("Luxe Gelato", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(30, 58, 138)); // Azul oscuro
        lblTitulo.setBounds(0, 30, 450, 40);
        panelPrincipal.add(lblTitulo);

        JLabel lblSub = new JLabel("Ingresa tus credenciales para continuar.", SwingConstants.CENTER);
        lblSub.setForeground(new Color(85, 85, 85));
        lblSub.setBounds(0, 70, 450, 20);
        panelPrincipal.add(lblSub);

        // Campo Usuario
        JLabel lblUsu = new JLabel("ID de Pingüino/Usuario:");
        lblUsu.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUsu.setBounds(50, 120, 250, 20);
        panelPrincipal.add(lblUsu);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(50, 145, 335, 35);
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2));
        panelPrincipal.add(txtUsuario);

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña Secreta:");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPass.setBounds(50, 195, 200, 20);
        panelPrincipal.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 220, 335, 35);
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2));
        panelPrincipal.add(txtPassword);

        // Selección de Rol
        rbEmpleado = new JRadioButton("Empleado");
        rbEmpleado.setBackground(new Color(253, 246, 227));
        rbEmpleado.setSelected(true);
        rbEmpleado.setBounds(100, 280, 100, 20);
        
        rbCliente = new JRadioButton("Cliente");
        rbCliente.setBackground(new Color(253, 246, 227));
        rbCliente.setBounds(250, 280, 100, 20);

        ButtonGroup grupoRol = new ButtonGroup();
        grupoRol.add(rbEmpleado);
        grupoRol.add(rbCliente);
        
        panelPrincipal.add(rbEmpleado);
        panelPrincipal.add(rbCliente);

        // Botón Verde gigante
        btnIniciarSesion = new JButton("INGRESAR AL SISTEMA");
        btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnIniciarSesion.setBackground(new Color(101, 163, 13)); // Verde chingón
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.setBounds(50, 340, 335, 50);
        
        // Evento de escucha del botón para ejecutar la validación
        btnIniciarSesion.addActionListener(e -> procederAlLogin());
        
        panelPrincipal.add(btnIniciarSesion);
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