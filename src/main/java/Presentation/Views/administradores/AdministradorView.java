package Presentation.Views.administradores;

import javax.swing.*;
import java.awt.*;

public class AdministradorView {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton agregarButton;
    private JButton borrarButton;
    private JTable adminsTable;
    private JScrollPane tableScroll;

    public AdministradorView() {
        mainPanel = new JPanel(new BorderLayout(8,8));

        // Form arriba
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.X_AXIS));
        usernameField = new JTextField();
        usernameField.setColumns(10);
        emailField = new JTextField();
        emailField.setColumns(12);
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        agregarButton = new JButton("Agregar");
        borrarButton = new JButton("Borrar seleccionado");

        form.add(new JLabel("Usuario:"));
        form.add(Box.createHorizontalStrut(4));
        form.add(usernameField);
        form.add(Box.createHorizontalStrut(8));
        form.add(new JLabel("Email:"));
        form.add(Box.createHorizontalStrut(4));
        form.add(emailField);
        form.add(Box.createHorizontalStrut(8));
        form.add(new JLabel("Clave:"));
        form.add(Box.createHorizontalStrut(4));
        form.add(passwordField);
        form.add(Box.createHorizontalStrut(8));
        form.add(agregarButton);
        form.add(Box.createHorizontalStrut(4));
        form.add(borrarButton);

        adminsTable = new JTable();
        tableScroll = new JScrollPane(adminsTable);

        mainPanel.add(form, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
    }

    public JPanel getContentPane() { return mainPanel; }
    public JTextField getUsernameField() { return usernameField; }
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getAgregarButton() { return agregarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JTable getAdminsTable() { return adminsTable; }
}
