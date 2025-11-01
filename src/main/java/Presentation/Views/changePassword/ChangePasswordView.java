package Presentation.Views.changePassword;

import Presentation.Controllers.LoginController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangePasswordView extends JDialog {
    private JPanel panel1;
    private JTextField UsserField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordField2;
    private JButton cambiarContraseñaButton;
    private JButton cancelarButton;
    private JLabel UsserLabel;
    private JLabel PasswordLabel1;

    private LoginController controller;

    public ChangePasswordView(JFrame parent) {
        super(parent, "Change Password", true); // Modal dialog

        setContentPane(panel1);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setSize(500, 350);
        setLocationRelativeTo(parent); // Centrar respecto al padre

        addListeners();
        UsserField.requestFocus();
    }

    private void addListeners() {
        cambiarContraseñaButton.addActionListener(e -> onChangePassword());
        cancelarButton.addActionListener(e -> onCancel());

        // Enter para cambiar de field
        UsserField.addActionListener(e -> oldPasswordField.requestFocus());
        oldPasswordField.addActionListener(e -> newPasswordField.requestFocus());
        newPasswordField.addActionListener(e -> newPasswordField2.requestFocus());
        newPasswordField2.addActionListener(e -> onChangePassword());

        // ESC para cancelar
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    // Método para precargar el usuario desde LoginView
    public void setUserId(String userId) {
        UsserField.setText(userId);
        if (!userId.isEmpty()) {
            UsserField.setEditable(false); // No permitir editar si viene precargado
            oldPasswordField.requestFocus();
        }
    }

    private void onChangePassword() {
        if (controller != null) {
            String usernameOrEmail = UsserField.getText();
            String currentPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(newPasswordField2.getPassword());

            controller.handleChangePassword(usernameOrEmail, currentPassword,
                    newPassword, confirmPassword, this);
        }
    }

    private void onCancel() {
        limpiarCampos();
        dispose();
    }

    private void limpiarCampos() {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        newPasswordField2.setText("");
    }

    // Métodos para mostrar mensajes
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cerrarVentanaExitoso() {
        mostrarMensaje("Password changed successfully.");
        limpiarCampos();
        dispose();
    }

    public void resetearFormulario() {
        limpiarCampos();
        if (UsserField.isEditable()) {
            UsserField.setText("");
            UsserField.requestFocus();
        } else {
            oldPasswordField.requestFocus();
        }
    }
}