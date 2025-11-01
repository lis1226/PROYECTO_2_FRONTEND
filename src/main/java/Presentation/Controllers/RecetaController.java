package Presentation.Controllers;

import Services.SocketClient;
import Domain.Dtos.receta.*;
import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Presentation.Views.RecetaView;
import Presentation.Models.RecetaTableModel;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecetaController {
    private final RecetaView view;
    private final RecetaTableModel tableModel;
    private final SocketClient socket;
    private final String idMedicoActual;

    private List<DetalleRecetaDto> detallesActuales = new ArrayList<>();
    private String idPacienteActual;

    public RecetaController(RecetaView view, SocketClient socket, String idMedicoActual) {
        this.view = view;
        this.socket = socket;
        this.idMedicoActual = idMedicoActual;
        this.tableModel = new RecetaTableModel();

        inicializarVista();
        configurarEventos();
    }

    private void inicializarVista() {
        view.getDetallesTable().setModel(tableModel);
        view.getFechaSeleccionada().setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    private void configurarEventos() {
        view.getBuscarPacienteButton().addActionListener(e -> buscarPaciente());
        view.getAgregarMedicamentoButton().addActionListener(e -> agregarMedicamento());
        view.getGuardarButton().addActionListener(e -> {
            try {
                guardarReceta();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        view.getCalendarioButton().addActionListener(e -> seleccionarFechaRetiro());
    }

    private void buscarPaciente() {
        String idPaciente = JOptionPane.showInputDialog(view.getRecetaPanel(), "Ingrese el ID del paciente:");
        if (idPaciente == null || idPaciente.trim().isEmpty()) return;

        try {
            RequestDto req = new RequestDto("paciente", "get", new com.google.gson.Gson().toJson(Map.of("id", idPaciente)), null);
            ResponseDto resp = socket.send(req);

            if (resp != null && resp.isSuccess()) {
                idPacienteActual = idPaciente;
                view.getNombrePacienteField().setText("Paciente #" + idPaciente);
            } else {
                JOptionPane.showMessageDialog(view.getRecetaPanel(), "Paciente no encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getRecetaPanel(), "Error al buscar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarMedicamento() {
        String idMedicamento = JOptionPane.showInputDialog(view.getRecetaPanel(), "Código de medicamento:");
        String cantidadStr = JOptionPane.showInputDialog(view.getRecetaPanel(), "Cantidad:");
        String indicaciones = JOptionPane.showInputDialog(view.getRecetaPanel(), "Indicaciones:");
        String duracionStr = JOptionPane.showInputDialog(view.getRecetaPanel(), "Duración (días):");

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            int duracion = Integer.parseInt(duracionStr);
            DetalleRecetaDto detalle = new DetalleRecetaDto(idMedicamento, cantidad, indicaciones, duracion);
            detallesActuales.add(detalle);
            tableModel.agregarFila(new Object[]{idMedicamento, cantidad, indicaciones, duracion});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.getRecetaPanel(), "Error: cantidad o duración inválidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarFechaRetiro() {
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JComponent editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);

        int option = JOptionPane.showConfirmDialog(view.getRecetaPanel(), dateSpinner, "Seleccione la fecha de retiro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Date fecha = (Date) dateSpinner.getValue();
            String fechaTexto = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
            view.getFechaSeleccionada().setText(fechaTexto);
        }
    }

    private void guardarReceta() throws IOException {
        if (idPacienteActual == null || detallesActuales.isEmpty()) {
            JOptionPane.showMessageDialog(view.getRecetaPanel(), "Debe seleccionar paciente y agregar medicamentos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AddRecetaRequestDto dto = new AddRecetaRequestDto();
        dto.setIdPaciente(idPacienteActual);
        dto.setIdMedico(idMedicoActual);
        dto.setEstado("confeccionada");
        dto.setDetalles(detallesActuales);

        RequestDto req = new RequestDto("receta", "add", new com.google.gson.Gson().toJson(dto), null);
        ResponseDto resp = socket.send(req);

        if (resp != null && resp.isSuccess()) {
            JOptionPane.showMessageDialog(view.getRecetaPanel(), "Receta guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            detallesActuales.clear();
            tableModel.limpiar();
        } else {
            JOptionPane.showMessageDialog(view.getRecetaPanel(), "Error al guardar receta: " + (resp != null ? resp.getMessage() : "sin respuesta"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
