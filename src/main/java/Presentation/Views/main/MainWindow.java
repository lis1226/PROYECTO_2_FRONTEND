package Presentation.Views.main;

//import domain_layer.Administrador;
//import domain_layer.Farmaceutico;
//import domain_layer.Medico;
//import domain_layer.Usuario;
//import presentation_layer.controllers.MainController;
//import service_layer.*;

import Services.MessageService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

public class MainWindow extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JTextArea MessageTextArea;
    private MessageService messageService;

    public MainWindow() {
        setTitle("Sistema con Tabs");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Connect to the message service and start listening for broadcasts
     */
    public void connectToMessages(String host, int port) {
        messageService = new MessageService(host, port, message -> {
            // This callback is called whenever a message is received
            SwingUtilities.invokeLater(() -> {
                appendMessageToTextArea(message);
            });
        });

        try {
            messageService.connect();
            System.out.println("[MainView] Connected to message service");
        } catch (Exception e) {
            System.err.println("[MainView] Failed to connect to message service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Append a message to the text area with timestamp
     */
    private void appendMessageToTextArea(String messageJson) {
        try {
            // Parse the message (it comes as a plain string from the server)
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String formattedMessage = "[" + timestamp + "] " + messageJson + "\n";

            MessageTextArea.append(formattedMessage);
            MessageTextArea.setCaretPosition(MessageTextArea.getDocument().getLength());

            System.out.println("[MainView] Message displayed: " + messageJson);
        } catch (Exception e) {
            System.err.println("[MainView] Error displaying message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Agrega pestañas al tab panel.
     * @param tabs Diccionario con título y contenido del tab.
     */
    public void AddTabs(Dictionary<String, JPanel> tabs) {
        Enumeration<String> keys = tabs.keys();
        while (keys.hasMoreElements()) {
            String titulo = keys.nextElement();
            JPanel contenido = tabs.get(titulo);
            tabbedPane.addTab(titulo, contenido);
        }
    }

    public void closeMessageService() {
        if (messageService != null) {
            messageService.close();
        }
    }
//    private final Usuario usuario;
//    private final Map<String, JPanel> tabs;
//
//    public MainWindow(Usuario usuario, MedicoService medicoService, FarmaceuticoService farmaceuticoService, AdministradorService administradorService, PacienteService pacienteService, MedicamentoService medicamentoService, RecetaService recetaService) {
//        this.usuario = usuario;
//        this.tabs = new HashMap<>();
//
//        // Configurar ventana
//        setTitle("Sistema de Prescripción y Despacho de Recetas - " + determinarRol(usuario));
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1000, 600);
//        setLocationRelativeTo(null);
//        setResizable(true);
//
//        // Inicializar componentes
//        contentPane = new JPanel(new BorderLayout());
//        tabbedPane = new JTabbedPane();
//        contentPane.add(tabbedPane, BorderLayout.CENTER);
//        setContentPane(contentPane);
//
//        // Configurar tabs con MainController
//        new MainController(this, usuario, medicoService, farmaceuticoService, administradorService, pacienteService, medicamentoService, recetaService);
//    }
//
//    public void addTab(String nombre, JPanel panel) {
//        tabs.put(nombre, panel);
//        tabbedPane.addTab(nombre, panel);
//    }
//
//    private String determinarRol(Usuario usuario) {
//        if (usuario instanceof Medico) return "Médico";
//        if (usuario instanceof Farmaceutico) return "Farmacéutico";
//        if (usuario instanceof Administrador) return "Administrador";
//        return "Usuario desconocido";
//    }
}
