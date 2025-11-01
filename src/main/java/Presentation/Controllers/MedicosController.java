package Presentation.Controllers;

import Domain.Dtos.medicos.*;
import Presentation.Observable;
import Presentation.Views.medicos.MedicosView;
import Services.MedicoService;
import Utilities.EventType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class MedicosController extends Observable {
    private final MedicosView medicosView;
    private final MedicoService medicoService;

    public MedicosController(MedicosView medicosView, MedicoService medicoService) {
        this.medicosView = medicosView;
        this.medicoService = medicoService;

        addObserver(medicosView.getTableModel());
        loadMedicosAsync();
        addListeners();
    }



    private void loadMedicosAsync() {
        SwingWorker<List<MedicoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MedicoResponseDto> doInBackground() throws Exception {
                // Por ahora se usa usuarioId fijo (ej: administrador 1)
                return medicoService.listMedicoAsync(1L).get();
            }

            @Override
            protected void done() {
                try {
                    List<MedicoResponseDto> medicos = get();
                    medicosView.getTableModel().setMedicos(medicos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        medicosView.getAgregarButton().addActionListener(e -> handleAddMedico());
        medicosView.getActualizarButton().addActionListener(e -> handleUpdateMedico());
        medicosView.getBorrarButton().addActionListener(e -> handleDeleteMedico());
        medicosView.getLimpiarButton().addActionListener(e -> handleClearFields());
        medicosView.getMedicosTable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    // ---------------------------
    // ACTION HANDLERS
    // ---------------------------

    private void handleAddMedico() {
        String id = medicosView.getIdTextField().getText().trim();
        String nombre = medicosView.getNombreTextField().getText().trim();
        String especialidad = medicosView.getEspecialidadTextField().getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID y un nombre.");
            return;
        }

        AddMedicoRequestDto dto = new AddMedicoRequestDto(id, nombre, especialidad);

        SwingWorker<MedicoResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected MedicoResponseDto doInBackground() throws Exception {
                return medicoService.addMedicoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    MedicoResponseDto medico = get();
                    if (medico != null) {
                        notifyObservers(EventType.CREATED, medico);
                        medicosView.clearFields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void handleDeleteMedico() {
        int selectedRow = medicosView.getMedicosTable().getSelectedRow();
        if (selectedRow < 0) return;

        // Obtener el medico seleccionado
        MedicoResponseDto selectedMedico = medicosView.getTableModel().getMedicos().get(selectedRow);
        DeleteMedicoRequestDto dto = new DeleteMedicoRequestDto(selectedMedico.getId());

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Llamada al servicio para eliminar
                return medicoService.deleteMedicoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success) {
                        // Actualizar la tabla eliminando el medico
                        medicosView.getTableModel().removeMedicoById(selectedMedico.getId());
                        // Limpiar los campos del formulario
                        medicosView.clearFields();
                        // Notificar a observadores
                        notifyObservers(EventType.DELETED, selectedMedico.getId());
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


    private void handleUpdateMedico() {
        int selectedRow = medicosView.getMedicosTable().getSelectedRow();
        if (selectedRow < 0) return;

        MedicoResponseDto selectedMedico = medicosView.getTableModel().getMedicos().get(selectedRow);
        String nombre = medicosView.getNombreTextField().getText().trim();
        String especialidad = medicosView.getEspecialidadTextField().getText().trim();

        UpdateMedicoRequestDto dto = new UpdateMedicoRequestDto(selectedMedico.getId(), nombre, especialidad);

        SwingWorker<MedicoResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected MedicoResponseDto doInBackground() throws Exception {
                return medicoService.updateMedicoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    MedicoResponseDto updatedMedico = get();
                    if (updatedMedico != null) {
                        notifyObservers(EventType.UPDATED, updatedMedico);
                        medicosView.clearFields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void handleClearFields() {
        medicosView.clearFields();
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = medicosView.getMedicosTable().getSelectedRow();
            if (row >= 0) {
                MedicoResponseDto med = medicosView.getTableModel().getMedicos().get(row);
                medicosView.populateFields(med);
            }
        }
    }
}
