package Presentation.Controllers;

import Domain.Dtos.administrador.*;
import Presentation.Views.administradores.AdministradorView;
import Presentation.Models.AdministradoresTableModel;
import Services.AdministradorService;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdministradoresController {
    private final AdministradorView view;
    private final AdministradorService service;
    private final AdministradoresTableModel tableModel;

    public AdministradoresController(AdministradorView view, AdministradorService service) {
        this.view = view;
        this.service = service;
        this.tableModel = new AdministradoresTableModel();
        this.view.getAdminsTable().setModel(tableModel);

        addListeners();
        loadAdmins();
    }

    private void addListeners() {
        view.getAgregarButton().addActionListener(e -> agregarAdmin());
        view.getBorrarButton().addActionListener(e -> borrarSeleccionado());
    }

    private void loadAdmins() {
        SwingWorker<List<AdministradorResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<AdministradorResponseDto> doInBackground() throws Exception {
                return service.listAdministradoresAsync(null).get();
            }
            @Override
            protected void done() {
                try {
                    List<AdministradorResponseDto> list = get();
                    tableModel.setAdministradores(list);
                } catch (ExecutionException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void agregarAdmin() {
        String username = view.getUsernameField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String password = new String(view.getPasswordField().getPassword()).trim();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view.getContentPane(), "Complete usuario, email y password");
            return;
        }

        AddAdministradorRequestDto dto = new AddAdministradorRequestDto(username, email, password, "ADMIN");
        SwingWorker<AdministradorResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected AdministradorResponseDto doInBackground() throws Exception {
                return service.addAdministradorAsync(dto, null).get();
            }
            @Override
            protected void done() {
                try {
                    AdministradorResponseDto added = get();
                    if (added != null) {
                        tableModel.addAdministrador(added);
                        view.getUsernameField().setText("");
                        view.getEmailField().setText("");
                        view.getPasswordField().setText("");
                    } else {
                        JOptionPane.showMessageDialog(view.getContentPane(), "Error al agregar administrador");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view.getContentPane(), "Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void borrarSeleccionado() {
        int row = view.getAdminsTable().getSelectedRow();
        if (row < 0) return;
        AdministradorResponseDto sel = tableModel.getAt(row);
        if (sel == null) return;

        int confirm = JOptionPane.showConfirmDialog(view.getContentPane(), "Eliminar administrador " + sel.getUsername() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        DeleteAdministradorRequestDto dto = new DeleteAdministradorRequestDto(sel.getId());
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return service.deleteAdministradorAsync(dto, null).get();
            }
            @Override
            protected void done() {
                try {
                    Boolean ok = get();
                    if (ok != null && ok) {
                        tableModel.removeAt(row);
                    } else {
                        JOptionPane.showMessageDialog(view.getContentPane(), "No se pudo eliminar");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
