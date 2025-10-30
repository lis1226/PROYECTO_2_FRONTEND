package Presentation.Views.login;

import Domain.Dtos.auth.UsuarioResponseDto;
import Presentation.Controllers.LoginController;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame implements IObserver {
    private JPanel contentPane;
    private JPanel LoginPanel;
    private JPanel PasswordPanel;
    private JLabel PasswordLabel;
    private JLabel UsuarioLabel;
    private JPanel UsserPanel;
    private JPanel ButtonPanel;
    private JButton LogginButton;
    private JButton ChangePasswordButton;
    private JButton CloseButton;
    private JPasswordField PasswordField;
    private JTextField UsserField;

    private LoginController controller;

    public LoginView() {
        setTitle("Login");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //pack();
        setSize(400, 300);
        setLocationRelativeTo(null);  // Centrar en pantalla

    }

    public void addLoginListener(ActionListener listener) {
        LogginButton.addActionListener(listener);
    }

    public String getUsername() {
        return UsserField.getText().trim();
    }

    public String getPassword() {
        return new String(PasswordField.getPassword());
    }


    @Override
    public void update(EventType eventType, Object data) {
        switch (eventType) {
            case CREATED:
                UsuarioResponseDto user = (UsuarioResponseDto) data;
                JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername());
                break;
            case UPDATED:
                break;
            case DELETED:
                JOptionPane.showMessageDialog(this, data.toString(), "Login Info", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

//    private void addListeners() {
//        LogginButton.addActionListener(e -> onLogin());
//        ChangePasswordButton.addActionListener(e -> onChangePassword());
//        CloseButton.addActionListener(e -> System.exit(0));
//
//        // Enter key navigation para mejor UX
//        UsserField.addActionListener(e -> PasswordField.requestFocus());
//        PasswordField.addActionListener(e -> onLogin());
//
//        // ESC key para cerrar aplicación
//        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
//        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
//        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
//            }
//        });
//
//        // Enter key en LogginButton para hacer login
//        LogginButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "login");
//        LogginButton.getActionMap().put("login", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                onLogin();
//            }
//        });
//
//        // Enter key en ChangePasswordButton para abrir cambio de contraseña
//        ChangePasswordButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "changePassword");
//        ChangePasswordButton.getActionMap().put("changePassword", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                onChangePassword();
//            }
//        });
//    }
//
//    public void bind(LoginController controller) {
//        this.controller = controller;
//        UsserField.requestFocus();
//    }
//
//    // Operaciones
//    private void onLogin() {
//        if (controller != null) {
//            String id = UsserField.getText();
//            String clave = new String(PasswordField.getPassword());
//            controller.intentarLogin(id, clave);
//        }
//    }
//
//    private void onChangePassword() {
//        if (controller != null) {
//            String usuarioActual = UsserField.getText().trim();
//            controller.abrirCambioPassword(usuarioActual);
//        }
//    }
//
//    private void mostrarPanelLogin() {
//        LoginPanel.setVisible(true);
//        UsserField.setText("");
//        PasswordField.setText("");
//        UsserField.requestFocus();
//    }
//
//
//    // Métodos para mostrar mensajes
//    public void mostrarError(String mensaje) {
//        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
//    }
//
//    public void mostrarMensaje(String mensaje) {
//        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    public void cerrarVentana() {
//        dispose();
//    }
//
//    // Método para limpiar campos después de un cambio exitoso
//    public void limpiarCampos() {
//        UsserField.setText("");
//        PasswordField.setText("");
//        UsserField.requestFocus();
//    }
//
//
//
//    public JPanel getContentPane() {
//        return contentPane;
//    }
}