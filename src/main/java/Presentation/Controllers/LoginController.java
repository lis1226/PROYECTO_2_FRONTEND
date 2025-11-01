package Presentation.Controllers;



import Domain.Dtos.auth.UsuarioResponseDto;
import Presentation.Observable;
import Presentation.Views.RecetaView;
import Presentation.Views.changePassword.ChangePasswordView;
import Presentation.Views.medicamentos.MedicamentosView;
import Presentation.Views.login.LoginView;
import Presentation.Views.main.MainWindow;
import Presentation.Views.medicos.MedicosView;
import Presentation.Views.pacientes.PacientesView;
import Services.*;
import Utilities.EventType;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;


public class LoginController extends Observable {
    private LoginView loginView;
    private final AuthService authService;

    public LoginController(LoginView loginView, AuthService authService) {
        this.loginView = loginView;
        this.authService = authService;

        this.loginView.addLoginListener(e -> handleLogin());
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
                        openMainView();
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


    private void openMainView() {
        MainWindow mainView = new MainWindow();

        String host = "localhost";
        int serverPort = 7000;
        int messagesPort = 7001;


        // Inicializar las vistas que van dentro del main view.

        MedicosView medicosView = new MedicosView(mainView);
        MedicoService medicoService = new MedicoService(host, serverPort);
        new MedicosController(medicosView, medicoService);

        Presentation.Views.farmaceuticos.FarmaceuticosView farmaceuticosView = new Presentation.Views.farmaceuticos.FarmaceuticosView(mainView);
        FarmaceuticoService farmaceuticoService = new FarmaceuticoService(host, serverPort);
        new FarmaceuticosController(farmaceuticosView, farmaceuticoService);

        MedicamentosView  medicamentosView = new MedicamentosView(mainView);
        MedicamentoService medicamentoService = new MedicamentoService(host, serverPort);
        new MedicamentosController(medicamentosView, medicamentoService);

        PacientesView pacientesView = new PacientesView(mainView);
        PacienteService pacienteService = new PacienteService(host, serverPort);
        new PacientesController(pacientesView, pacienteService);

        Presentation.Views.dashboard.DashboardView dashboardView = new Presentation.Views.dashboard.DashboardView();
        Services.DashboardService dashboardService = new Services.DashboardService(host, serverPort);
        new Presentation.Controllers.DashboardController(dashboardView, dashboardService);

       // RecetaView recetaView = new RecetaView(mainView); // se pasa mainView
       // RecetaService recetaService = new RecetaService(host, serverPort);
       // new RecetaController(recetaView);

        Presentation.Views.administradores.AdministradorView adminView = new Presentation.Views.administradores.AdministradorView();
        Services.AdministradorService adminService = new Services.AdministradorService(host, serverPort);
        new Presentation.Controllers.AdministradoresController(adminView, adminService);


        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Medicos", medicosView.getContentPane());
        tabs.put("Farmaceuticos", farmaceuticosView.getContentPane());
        tabs.put("Medicamentos", medicamentosView.getContentPane());
        tabs.put("Pacientes",pacientesView.getContentPane());
        tabs.put("Dashboard", dashboardView.getContentPane());
        tabs.put("Administradores", adminView.getContentPane());

       // tabs.put("Recetas", recetaView.getRecetaPanel());
        // Conectarse al puerto 7001 para escuchar transmisiones del servidor
        mainView.connectToMessages(host, messagesPort);
        mainView.AddTabs(tabs);
        mainView.setVisible(true);
    }

}