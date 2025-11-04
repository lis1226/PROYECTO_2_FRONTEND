package Presentation.Views.recetas;

import Domain.Dtos.receta.DetalleRecetaDto;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrescribirView extends JFrame {
    private JPanel contentPane;
    private JTextField pacienteIdField;
    private JTextField pacienteNombreField;
    private JButton buscarPacienteButton;
    private JLabel fechaConfeccionLabel;
    private JSpinner fechaRetiroSpinner;
    private JTable detallesTable;
    private JButton agregarMedicamentoButton;
    private JButton modificarDetalleButton;
    private JButton eliminarDetalleButton;
    private JButton guardarRecetaButton;
    private JButton cancelarButton;
    private JTextField medicamentoCodigoField;
    private JButton buscarMedicamentoButton;

    private String idPacienteSeleccionado = null;
    private String idMedicoActual = null;

    public PrescribirView(String idMedico) {
        this.idMedicoActual = idMedico;
        setTitle("Prescribir Receta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Panel superior: Paciente
        JPanel panelPaciente = crearPanelPaciente();
        contentPane.add(panelPaciente, BorderLayout.NORTH);

        // Panel central: Tabla de detalles
        JPanel panelCentral = crearPanelCentral();
        contentPane.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelInferior = crearPanelInferior();
        contentPane.add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelPaciente() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Información del Paciente"));

        // Línea 1: Buscar paciente
        JPanel linea1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        linea1.add(new JLabel("ID Paciente:"));
        pacienteIdField = new JTextField(15);
        linea1.add(pacienteIdField);
        buscarPacienteButton = new JButton("Buscar");
        linea1.add(buscarPacienteButton);
        pacienteNombreField = new JTextField(25);
        pacienteNombreField.setEditable(false);
        pacienteNombreField.setBackground(Color.WHITE);
        linea1.add(new JLabel("Nombre:"));
        linea1.add(pacienteNombreField);

        // Línea 2: Fechas
        JPanel linea2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        linea2.add(new JLabel("Fecha Confección:"));
        fechaConfeccionLabel = new JLabel(sdf.format(new Date()));
        fechaConfeccionLabel.setFont(fechaConfeccionLabel.getFont().deriveFont(Font.BOLD));
        linea2.add(fechaConfeccionLabel);

        linea2.add(Box.createHorizontalStrut(30));
        linea2.add(new JLabel("Fecha Retiro:"));

        // Fecha retiro por defecto: mañana
        Date manana = new Date(System.currentTimeMillis() + 86400000);
        SpinnerDateModel dateModel = new SpinnerDateModel(manana, null, null, java.util.Calendar.DAY_OF_MONTH);
        fechaRetiroSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(fechaRetiroSpinner, "dd/MM/yyyy");
        fechaRetiroSpinner.setEditor(editor);
        linea2.add(fechaRetiroSpinner);

        panel.add(linea1);
        panel.add(Box.createVerticalStrut(5));
        panel.add(linea2);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Medicamentos Prescritos"));

        // Toolbar para agregar medicamento
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.add(new JLabel("Código Medicamento:"));
        medicamentoCodigoField = new JTextField(15);
        toolbar.add(medicamentoCodigoField);
        buscarMedicamentoButton = new JButton("Buscar");
        toolbar.add(buscarMedicamentoButton);
        agregarMedicamentoButton = new JButton("Agregar a Receta");
        agregarMedicamentoButton.setEnabled(false);
        toolbar.add(agregarMedicamentoButton);

        // Tabla
        detallesTable = new JTable();
        detallesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(detallesTable);

        // Botones de tabla
        JPanel botonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        modificarDetalleButton = new JButton("Modificar Seleccionado");
        eliminarDetalleButton = new JButton("Eliminar Seleccionado");
        botonesTabla.add(modificarDetalleButton);
        botonesTabla.add(eliminarDetalleButton);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botonesTabla, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        guardarRecetaButton = new JButton("Guardar Receta");
        guardarRecetaButton.setFont(guardarRecetaButton.getFont().deriveFont(Font.BOLD, 14f));
        guardarRecetaButton.setEnabled(false);
        cancelarButton = new JButton("Cancelar");
        panel.add(cancelarButton);
        panel.add(guardarRecetaButton);
        return panel;
    }

    // Getters
    public JTextField getPacienteIdField() { return pacienteIdField; }
    public JTextField getPacienteNombreField() { return pacienteNombreField; }
    public JButton getBuscarPacienteButton() { return buscarPacienteButton; }
    public JLabel getFechaConfeccionLabel() { return fechaConfeccionLabel; }
    public JSpinner getFechaRetiroSpinner() { return fechaRetiroSpinner; }
    public JTable getDetallesTable() { return detallesTable; }
    public JButton getAgregarMedicamentoButton() { return agregarMedicamentoButton; }
    public JButton getModificarDetalleButton() { return modificarDetalleButton; }
    public JButton getEliminarDetalleButton() { return eliminarDetalleButton; }
    public JButton getGuardarRecetaButton() { return guardarRecetaButton; }
    public JButton getCancelarButton() { return cancelarButton; }
    public JTextField getMedicamentoCodigoField() { return medicamentoCodigoField; }
    public JButton getBuscarMedicamentoButton() { return buscarMedicamentoButton; }

    public String getIdPacienteSeleccionado() { return idPacienteSeleccionado; }
    public void setIdPacienteSeleccionado(String id) { this.idPacienteSeleccionado = id; }

    public String getIdMedicoActual() { return idMedicoActual; }

    public Date getFechaRetiro() {
        return (Date) fechaRetiroSpinner.getValue();
    }
}