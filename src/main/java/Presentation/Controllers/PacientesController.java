package Presentation.Controllers;

import Domain.Dtos.pacientes.AddPacienteRequestDto;
import Domain.Dtos.pacientes.DeletePacienteRequestDto;
import Domain.Dtos.pacientes.PacienteResponseDto;
import Domain.Dtos.pacientes.UpdatePacienteRequestDto;
import Presentation.Observable;
import Presentation.Views.pacientes.PacientesView;
import Services.PacienteService;
import Utilities.EventType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PacientesController extends Observable {
    private final PacientesView pacientesView;
    private final PacienteService pacienteService;


    public PacientesController(PacientesView pacientesView, PacienteService pacienteService) {
        this.pacientesView = pacientesView;
        this.pacienteService = pacienteService;

        addObserver(pacientesView.getTableModel());
        loadPacientesAsync();
        addListeners();
    }

    private void loadPacientesAsync() {
        SwingWorker<List<PacienteResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PacienteResponseDto> doInBackground() throws Exception {
                // Por ahora se usa usuarioId fijo (ej: administrador 1)
                return pacienteService.listPacienteAsync(1L).get();
            }

            @Override
            protected void done() {
                try {
                    List<PacienteResponseDto> medicos = get();
                    pacientesView.getTableModel().setPacientes(medicos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        pacientesView.getAgregarButton().addActionListener(e -> {
            handleAddPaciente();
        });
        pacientesView.getActualizarButton().addActionListener(e -> {
            try {
                handleUpdatePaciente();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        pacientesView.getBorrarButton().addActionListener(e -> handleDeletePaciente());
        pacientesView.getLimpiarButton().addActionListener(e -> handleClearFields());
        pacientesView.getBuscarButton().addActionListener(e -> handleBuscarPaciente());
        pacientesView.getPacientesTable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    // ---------------------------
    // ACTION HANDLERS
    // ---------------------------

    private void handleAddPaciente() {
        String id = pacientesView.getIdTextField().getText().trim();
        String nombre = pacientesView.getNombreTextField().getText().trim();
        String fechaNacimiento = pacientesView.getFechaNacimientotextField().getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || fechaNacimiento.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar ID, nombre y fecha de nacimiento (formato dd/MM/yyyy).");
            return;
        }

        try {
            Date fecha = parseFecha(fechaNacimiento);
            AddPacienteRequestDto dto = new AddPacienteRequestDto(id, nombre, fecha);

            System.out.println("[PacientesController] Creating paciente: " + id + ", " + nombre + ", " + fechaNacimiento);

            SwingWorker<PacienteResponseDto, Void> worker = new SwingWorker<>() {
                @Override
                protected PacienteResponseDto doInBackground() throws Exception {
                    return pacienteService.addPacienteAsync(dto, null).get();
                }

                @Override
                protected void done() {
                    try {
                        PacienteResponseDto paciente = get();
                        if (paciente != null) {
                            System.out.println("[PacientesController] Paciente created successfully");
                            notifyObservers(EventType.CREATED, paciente);
                            pacientesView.clearFields();
                            JOptionPane.showMessageDialog(null, "Paciente agregado exitosamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al agregar paciente", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date parseFecha(String textoFecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        return sdf.parse(textoFecha);
    }

    private void handleDeletePaciente() {
        int selectedRow = pacientesView.getPacientesTable().getSelectedRow();
        if (selectedRow < 0) return;

        // Obtener el medico seleccionado
        PacienteResponseDto selectedPaciente = pacientesView.getTableModel().getPacientes().get(selectedRow);
        DeletePacienteRequestDto dto = new DeletePacienteRequestDto(selectedPaciente.getId());

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Llamada al servicio para eliminar
                return pacienteService.deletePacienteAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success) {
                        // Actualizar la tabla eliminando el medico
                        pacientesView.getTableModel().removePacienteById(selectedPaciente.getId());
                        // Limpiar los campos del formulario
                        pacientesView.clearFields();
                        // Notificar a observadores
                        notifyObservers(EventType.DELETED, selectedPaciente.getId());
                    } else {
                        // JOptionPane.showMessageDialog(medicosView, "No se pudo eliminar el médico", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //JOptionPane.showMessageDialog(medicosView, "Error al eliminar el médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }


    private void handleUpdatePaciente() throws ParseException {
        int selectedRow = pacientesView.getPacientesTable().getSelectedRow();
        if (selectedRow < 0) return;

        PacienteResponseDto selectedPaciente = pacientesView.getTableModel().getPacientes().get(selectedRow);
        String nombre = pacientesView.getNombreTextField().getText().trim();
        String fechaNacimiento = pacientesView.getFechaNacimientotextField().getText().trim();

        UpdatePacienteRequestDto dto = new UpdatePacienteRequestDto(selectedPaciente.getId(), nombre, parseFecha(fechaNacimiento));

        SwingWorker<PacienteResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected PacienteResponseDto doInBackground() throws Exception {
                return pacienteService.updatePacienteAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    PacienteResponseDto updatedPaciente = get();
                    if (updatedPaciente != null) {
                        notifyObservers(EventType.UPDATED, updatedPaciente);
                        pacientesView.clearFields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void handleClearFields() {
        pacientesView.clearFields();
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = pacientesView.getPacientesTable().getSelectedRow();
            if (row >= 0) {
                PacienteResponseDto med = pacientesView.getTableModel().getPacientes().get(row);
                pacientesView.populateFields(med);
            }
        }
    }
    private void handleBuscarPaciente() {
        String criterio = pacientesView.getBuscarTextFlied().getText().trim();

        if (criterio.isEmpty()) {
            loadPacientesAsync();
            return;
        }

        SwingWorker<List<PacienteResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PacienteResponseDto> doInBackground() throws Exception {
                return pacienteService.buscarPacientesAsync(criterio, 1L).get();
            }

            @Override
            protected void done() {
                try {
                    List<PacienteResponseDto> resultados = get();
                    if (resultados != null) {
                        pacientesView.getTableModel().setPacientes(resultados);
                        if (resultados.isEmpty()) {
                            JOptionPane.showMessageDialog(pacientesView.getContentPane(),
                                    "No se encontraron pacientes con ese criterio",
                                    "Búsqueda",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[PacientesController] Error searching: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

}
