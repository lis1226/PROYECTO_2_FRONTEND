package Presentation.Views;

import javax.swing.*;
import java.awt.*;

public class RecetaView {
    private JPanel recetaPanel;
    private JTextField nombrePacienteField;
    private JTextField fechaSeleccionada;
    private JButton buscarPacienteButton;
    private JButton calendarioButton;
    private JTable detallesTable;
    private JButton agregarMedicamentoButton;
    private JButton guardarButton;

    public RecetaView() {
        inicializarComponentes();
        configurarLayout();
    }

    private void inicializarComponentes() {
        recetaPanel = new JPanel();
        recetaPanel.setLayout(new BorderLayout(10, 10));

        // --- Panel superior (paciente + fecha) ---
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));

        // Línea 1: Paciente
        JPanel panelPaciente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelPaciente.add(new JLabel("Paciente:"));
        nombrePacienteField = new JTextField(20);
        nombrePacienteField.setEditable(false);
        panelPaciente.add(nombrePacienteField);
        buscarPacienteButton = new JButton("Buscar Paciente");
        panelPaciente.add(buscarPacienteButton);

        // Línea 2: Fecha
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFecha.add(new JLabel("Fecha de retiro:"));
        fechaSeleccionada = new JTextField(10);
        fechaSeleccionada.setEditable(false);
        panelFecha.add(fechaSeleccionada);
        calendarioButton = new JButton("Seleccionar Fecha");
        panelFecha.add(calendarioButton);

        panelSuperior.add(panelPaciente);
        panelSuperior.add(panelFecha);

        recetaPanel.add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla central ---
        detallesTable = new JTable();
        JScrollPane scroll = new JScrollPane(detallesTable);
        recetaPanel.add(scroll, BorderLayout.CENTER);

        // --- Panel inferior (botones) ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        agregarMedicamentoButton = new JButton("Agregar Medicamento");
        guardarButton = new JButton("Guardar Receta");
        panelInferior.add(agregarMedicamentoButton);
        panelInferior.add(guardarButton);

        recetaPanel.add(panelInferior, BorderLayout.SOUTH);
    }

    private void configurarLayout() {
        recetaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // --- Getters para el controlador ---
    public JPanel getRecetaPanel() { return recetaPanel; }
    public JTextField getNombrePacienteField() { return nombrePacienteField; }
    public JTextField getFechaSeleccionada() { return fechaSeleccionada; }
    public JButton getBuscarPacienteButton() { return buscarPacienteButton; }
    public JButton getCalendarioButton() { return calendarioButton; }
    public JTable getDetallesTable() { return detallesTable; }
    public JButton getAgregarMedicamentoButton() { return agregarMedicamentoButton; }
    public JButton getGuardarButton() { return guardarButton; }
}
