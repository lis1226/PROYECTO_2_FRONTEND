package Presentation.Views.login;

import Domain.Dtos.auth.UsuarioResponseDto;
import Presentation.Controllers.LoginController;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.*;
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
        setSize(400, 300);
        setLocationRelativeTo(null);  // Centrar en pantalla

        addKeyListeners();
    }

    private void addKeyListeners() {
        // Enter para navegar entre campos
        UsserField.addActionListener(e -> PasswordField.requestFocus());
        PasswordField.addActionListener(e -> LogginButton.doClick());

        // Listener para el botón de cerrar
        CloseButton.addActionListener(e -> System.exit(0));
    }

    public void addLoginListener(ActionListener listener) {
        LogginButton.addActionListener(listener);
    }

    public void addChangePasswordListener(ActionListener listener) {
        ChangePasswordButton.addActionListener(listener);
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
}