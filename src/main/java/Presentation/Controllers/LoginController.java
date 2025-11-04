package Presentation.Controllers;



import Domain.Dtos.auth.UsuarioResponseDto;
import Presentation.Observable;
import Presentation.Views.recetas.PrescribirView;
import Presentation.Views.changePassword.ChangePasswordView;
import Presentation.Views.medicamentos.MedicamentosView;
import Presentation.Views.login.LoginView;
import Presentation.Views.main.MainWindow;
import Presentation.Views.medicos.MedicosView;
import Presentation.Views.pacientes.PacientesView;
import Services.*;
import Utilities.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;


public class LoginController extends Observable {
    private LoginView loginView;
    private final AuthService authService;

    public LoginController(LoginView loginView, AuthService authService) {
        this.loginView = loginView;
        this.authService = authService;

        this.loginView.addLoginListener(e -> handleLogin());
        this.loginView.addChangePasswordListener(e -> handleOpenChangePassword());

    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Username or password cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //loginView.showLoading(true);

        SwingWorker<UsuarioResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected UsuarioResponseDto doInBackground() throws Exception {
                return authService.login(username, password).get();
            }

            @Override
            protected void done() {
                //loginView.showLoading(false);
                try {
                    UsuarioResponseDto user = get();
                    if (user != null) {
                        loginView.setVisible(false);
                        openMainView(user);
                        notifyObservers(EventType.UPDATED, user);
                    } else {
                        JOptionPane.showMessageDialog(loginView, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(loginView, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleOpenChangePassword() {
        String currentUsername = loginView.getUsername();
        ChangePasswordView changePasswordView = new ChangePasswordView(loginView);

        // Precargar el usuario si ya hay algo escrito
        if (!currentUsername.isEmpty()) {
            changePasswordView.setUserId(currentUsername);
        }

        changePasswordView.setController(this);
        changePasswordView.setVisible(true);
    }

    public void handleChangePassword(String usernameOrEmail, String currentPassword,
                                     String newPassword, String confirmPassword,
                                     ChangePasswordView view) {
        // Validaciones
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            view.mostrarError("Username or email is required");
            return;
        }

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            view.mostrarError("Current password is required");
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            view.mostrarError("New password is required");
            return;
        }

        if (confirmPassword == null || !confirmPassword.equals(newPassword)) {
            view.mostrarError("Passwords do not match");
            return;
        }

        if (newPassword.length() < 4) {
            view.mostrarError("New password must be at least 4 characters long");
            return;
        }

        if (currentPassword.equals(newPassword)) {
            view.mostrarError("New password must be different from current password");
            return;
        }
        //------------


        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authService.changePassword(usernameOrEmail, currentPassword, newPassword).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success != null && success) {
                        view.cerrarVentanaExitoso();
                    } else {
                        view.mostrarError("Failed to change password. Please verify your current password.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    view.mostrarError("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void openMainView(UsuarioResponseDto user) {
        MainWindow mainView = new MainWindow();
        String host = "localhost";
        int serverPort = 7000;
        int messagesPort = 7001;

        // Crear vistas y controladores como ya hacías
        MedicosView medicosView = new MedicosView(mainView);
        MedicoService medicoService = new MedicoService(host, serverPort);
        new MedicosController(medicosView, medicoService);

        Presentation.Views.farmaceuticos.FarmaceuticosView farmaceuticosView = new Presentation.Views.farmaceuticos.FarmaceuticosView(mainView);
        FarmaceuticoService farmaceuticoService = new FarmaceuticoService(host, serverPort);
        new FarmaceuticosController(farmaceuticosView, farmaceuticoService);

        MedicamentosView medicamentosView = new MedicamentosView(mainView);
        MedicamentoService medicamentoService = new MedicamentoService(host, serverPort);
        new MedicamentosController(medicamentosView, medicamentoService);

        PacientesView pacientesView = new PacientesView(mainView);
        PacienteService pacienteService = new PacienteService(host, serverPort);
        new PacientesController(pacientesView, pacienteService);

        Presentation.Views.dashboard.DashboardView dashboardView = new Presentation.Views.dashboard.DashboardView();
        Services.DashboardService dashboardService = new Services.DashboardService(host, serverPort);
        new Presentation.Controllers.DashboardController(dashboardView, dashboardService);


        RecetaService recetaService = new RecetaService(host, serverPort);
        JButton prescribirButton = new JButton("Nueva Prescripción");
        prescribirButton.addActionListener(e -> {
            // TODO: Obtener el ID del médico actual del usuario logueado
            // Por ahora usamos un ID fijo de ejemplo
            String idMedicoActual = "M001"; // En producción esto vendría del login

            PrescribirView prescribirView = new PrescribirView(idMedicoActual);
            new PrescribirController(prescribirView, recetaService, pacienteService, medicamentoService);
            prescribirView.setVisible(true);
        });

        JPanel prescripcionPanel = new JPanel(new BorderLayout(10, 10));
        prescripcionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(prescribirButton);
        prescripcionPanel.add(topPanel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("<html><h2>Prescripción de Recetas</h2>" +
                "<p>Desde aquí puede confeccionar recetas digitales para los pacientes.</p>" +
                "<p>Haga clic en 'Nueva Prescripción' para comenzar.</p></html>");
        prescripcionPanel.add(infoLabel, BorderLayout.CENTER);

        Presentation.Views.administradores.AdministradorView adminView = new Presentation.Views.administradores.AdministradorView();
        Services.AdministradorService adminService = new Services.AdministradorService(host, serverPort);
        new Presentation.Controllers.AdministradoresController(adminView, adminService);

        // --- NUEVO: Mostrar tabs según el rol del usuario ---
        java.util.Dictionary<String, javax.swing.JPanel> tabs = new java.util.Hashtable<>();
        String rol = user.getRol() != null ? user.getRol().trim().toUpperCase() : "";

        System.out.println("[LoginController] Rol detectado: " + rol);

        switch (rol) {
            case "ADMIN":
                tabs.put("Dashboard", dashboardView.getContentPane());
                tabs.put("Administradores", adminView.getContentPane());
                tabs.put("Medicos", medicosView.getContentPane());
                tabs.put("Farmaceuticos", farmaceuticosView.getContentPane());
                tabs.put("Medicamentos", medicamentosView.getContentPane());
                tabs.put("Pacientes", pacientesView.getContentPane());
                tabs.put("Prescripción", prescripcionPanel);

                break;

            case "MEDICO":
                tabs.put("Dashboard", dashboardView.getContentPane());
                tabs.put("Pacientes", pacientesView.getContentPane());
                tabs.put("Prescripción", prescripcionPanel);
                break;

            case "FARMACEUTICO":
                tabs.put("Dashboard", dashboardView.getContentPane());
                tabs.put("Medicamentos", medicamentosView.getContentPane());
                tabs.put("Prescripción", prescripcionPanel);
                break;

            case "PACIENTE":
                tabs.put("Dashboard", dashboardView.getContentPane());
                break;

            default:
                tabs.put("Dashboard", dashboardView.getContentPane());
                JOptionPane.showMessageDialog(null, "Rol desconocido: " + rol);
                break;
        }

        // Conexión a mensajes (si aplica)
        try {
            mainView.connectToMessages(host, messagesPort);
        } catch (Exception e) {
            System.err.println("No se pudo conectar al servicio de mensajes: " + e.getMessage());
        }

        // Añadir las pestañas según el rol
        mainView.AddTabs(tabs);
        mainView.setVisible(true);
    }



}