package Presentation.Controllers;

//import domain_layer.Administrador;
//import domain_layer.Farmaceutico;
//import domain_layer.Medico;
//import domain_layer.Usuario;
//import presentation_layer.views.ChangePasswordView;
//import presentation_layer.views.MainWindow;
//import service_layer.*;

import Domain.Dtos.auth.UsuarioResponseDto;
import Presentation.Observable;
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


        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Medicos", medicosView.getContentPane());
        tabs.put("Farmaceuticos", farmaceuticosView.getContentPane());
        tabs.put("Medicamentos", medicamentosView.getContentPane());
        tabs.put("Pacientes",pacientesView.getContentPane());
        tabs.put("Dashboard", dashboardView.getContentPane());

        // Conectarse al puerto 7001 para escuchar transmisiones del servidor
        mainView.connectToMessages(host, messagesPort);
        mainView.AddTabs(tabs);
        mainView.setVisible(true);
    }

//    private final UsuarioService usuarioService;
//    private final LoginView view;
//    private final MedicoService medicoService;
//    private final FarmaceuticoService farmaceuticoService;
//    private final AdministradorService administradorService;
//    private final PacienteService pacienteService;
//    private final MedicamentoService medicamentoService;
//    private final RecetaService recetaService;
//
//    public LoginController(UsuarioService usuarioService, LoginView view, MedicoService medicoService, FarmaceuticoService farmaceuticoService, AdministradorService administradorService, PacienteService pacienteService, MedicamentoService medicamentoService, RecetaService recetaService) {
//        this.usuarioService = usuarioService;
//        this.view = view;
//        this.medicoService = medicoService;
//        this.farmaceuticoService = farmaceuticoService;
//        this.administradorService = administradorService;
//        this.pacienteService = pacienteService;
//        this.medicamentoService = medicamentoService;
//        this.recetaService = recetaService;
//    }
//
//    // Login
//    public void intentarLogin(String id, String clave) {
//        if (id == null || id.trim().isEmpty()) {
//            view.mostrarError("El ID es obligatorio.");
//            return;
//        }
//        if (clave == null || clave.trim().isEmpty()) {
//            view.mostrarError("La clave es obligatoria.");
//            return;
//        }
//
//        Usuario usuario = usuarioService.autenticar(id.trim(), clave);
//        if (usuario != null) {
//            view.cerrarVentana();
//            abrirMainWindow(usuario);
//        } else {
//            view.mostrarError("ID o clave incorrectos.");
//        }
//    }
//
//    // Cambio de clave - Implementación completa
//    // Este método funciona con los componentes que agregues en el UI Designer
//    public void cambiarClave(String id, String claveActual, String nuevaClave, String confirmarNueva) {
//        // Validaciones de entrada
//        if (id == null || id.trim().isEmpty()) {
//            view.mostrarError("El ID es obligatorio.");
//            return;
//        }
//        if (claveActual == null || claveActual.trim().isEmpty()) {
//            view.mostrarError("La clave actual es obligatoria.");
//            return;
//        }
//        if (nuevaClave == null || nuevaClave.trim().isEmpty()) {
//            view.mostrarError("La nueva clave es obligatoria.");
//            return;
//        }
//        if (confirmarNueva == null || !confirmarNueva.equals(nuevaClave)) {
//            view.mostrarError("Las nuevas claves no coinciden.");
//            return;
//        }
//        if (nuevaClave.length() < 4) {
//            view.mostrarError("La nueva clave debe tener al menos 4 caracteres.");
//            return;
//        }
//
//        // Validación adicional: la nueva clave no debe ser igual a la actual
//        if (claveActual.equals(nuevaClave)) {
//            view.mostrarError("La nueva clave debe ser diferente a la actual.");
//            return;
//        }
//
//        try {
//            // Verificar que la clave actual sea correcta
//            Usuario usuario = usuarioService.autenticar(id.trim(), claveActual);
//            if (usuario == null) {
//                view.mostrarError("La clave actual es incorrecta.");
//                return;
//            }
//
//            // Intentar cambiar la clave
//            boolean cambioExitoso = usuarioService.cambiarClave(id.trim(), nuevaClave);
//
//            if (cambioExitoso) {
//                view.mostrarMensaje("Contraseña cambiada exitosamente.");
//            } else {
//                view.mostrarError("No se pudo cambiar la contraseña. Inténtelo de nuevo.");
//            }
//
//        } catch (Exception e) {
//            view.mostrarError("Error al cambiar la contraseña: " + e.getMessage());
//        }
//    }
//
//    // Método para validar políticas de contraseña más estrictas (opcional)
//    private boolean validarPoliticaPassword(String password) {
//        if (password.length() < 6) {
//            view.mostrarError("La contraseña debe tener al menos 6 caracteres.");
//            return false;
//        }
//
//        // Opcional: requerir al menos un número
//        if (!password.matches(".*\\d.*")) {
//            view.mostrarError("La contraseña debe contener al menos un número.");
//            return false;
//        }
//
//        // Opcional: requerir al menos una letra
//        if (!password.matches(".*[a-zA-Z].*")) {
//            view.mostrarError("La contraseña debe contener al menos una letra.");
//            return false;
//        }
//
//        return true;
//    }
//
//    // Método para abrir  con rol del usuario
//    private void abrirMainWindow(Usuario usuario) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Inicializar MainWindow con servicios y usuario logueado
//                MainWindow mainWindow = new MainWindow(usuario, medicoService, farmaceuticoService,
//                        administradorService, pacienteService,
//                        medicamentoService, recetaService);
//                mainWindow.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//                view.mostrarError("Error al abrir la ventana principal: " + e.getMessage());
//            }
//        });
//    }
//
//    // Método para abrir la ventana de cambio de contraseña
//    public void abrirCambioPassword(String usuarioActual) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                ChangePasswordView changePasswordView = new ChangePasswordView((JFrame) view);
//                changePasswordView.bind(this);
//
//                // Si hay un usuario ingresado, precargarlo
//                if (usuarioActual != null && !usuarioActual.trim().isEmpty()) {
//                    changePasswordView.setUserId(usuarioActual.trim());
//                }
//
//                changePasswordView.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//                view.mostrarError("Error al abrir la ventana de cambio de contraseña: " + e.getMessage());
//            }
//        });
//    }
//
//    // Método específico para el cambio de contraseña desde la ventana separada
//    public void cambiarClaveDesdeVentana(String id, String claveActual, String nuevaClave,
//                                         String confirmarNueva, ChangePasswordView ventana) {
//        // Validaciones de entrada
//        if (id == null || id.trim().isEmpty()) {
//            ventana.mostrarError("El ID es obligatorio.");
//            return;
//        }
//        if (claveActual == null || claveActual.trim().isEmpty()) {
//            ventana.mostrarError("La clave actual es obligatoria.");
//            return;
//        }
//        if (nuevaClave == null || nuevaClave.trim().isEmpty()) {
//            ventana.mostrarError("La nueva clave es obligatoria.");
//            return;
//        }
//        if (confirmarNueva == null || !confirmarNueva.equals(nuevaClave)) {
//            ventana.mostrarError("Las nuevas claves no coinciden.");
//            return;
//        }
//        if (nuevaClave.length() < 4) {
//            ventana.mostrarError("La nueva clave debe tener al menos 4 caracteres.");
//            return;
//        }
//
//        // Validación adicional: la nueva clave no debe ser igual a la actual
//        if (claveActual.equals(nuevaClave)) {
//            ventana.mostrarError("La nueva clave debe ser diferente a la actual.");
//            return;
//        }
//
//        try {
//            // Verificar que la clave actual sea correcta
//            Usuario usuario = usuarioService.autenticar(id.trim(), claveActual);
//            if (usuario == null) {
//                ventana.mostrarError("La clave actual es incorrecta.");
//                return;
//            }
//
//            // Intentar cambiar la clave
//            boolean cambioExitoso = usuarioService.cambiarClave(id.trim(), nuevaClave);
//
//            if (cambioExitoso) {
//                ventana.cerrarVentanaExitoso();
//            } else {
//                ventana.mostrarError("No se pudo cambiar la contraseña. Inténtelo de nuevo.");
//            }
//
//        } catch (Exception e) {
//            ventana.mostrarError("Error al cambiar la contraseña: " + e.getMessage());
//        }
//    }
//
//    private String determinarRol(Usuario usuario) {
//        if (usuario instanceof Medico) return "Médico";
//        if (usuario instanceof Farmaceutico) return "Farmacéutico";
//        if (usuario instanceof Administrador) return "Administrador";
//        return "Usuario desconocido";
//    }
//
//    // Método adicional para cerrar la aplicación desde el controlador
//    public void cerrarAplicacion() {
//        System.exit(0);
//    }
}