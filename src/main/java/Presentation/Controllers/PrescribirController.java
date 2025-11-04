package Presentation.Controllers;

import Domain.Dtos.medicamentos.MedicamentoResponseDto;
import Domain.Dtos.pacientes.PacienteResponseDto;
import Domain.Dtos.receta.AddRecetaRequestDto;
import Domain.Dtos.receta.DetalleRecetaDto;
import Domain.Dtos.receta.RecetaResponseDto;
import Presentation.Models.DetalleRecetaTableModel;
import Presentation.Views.recetas.PrescribirView;
import Services.MedicamentoService;
import Services.PacienteService;
import Services.RecetaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class PrescribirController {
    private final PrescribirView view;
    private final RecetaService recetaService;
    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final DetalleRecetaTableModel tableModel;

    private MedicamentoResponseDto medicamentoSeleccionado = null;

    public PrescribirController(PrescribirView view, RecetaService recetaService,
                                PacienteService pacienteService, MedicamentoService medicamentoService) {
        this.view = view;
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.tableModel = new DetalleRecetaTableModel();

        view.getDetallesTable().setModel(tableModel);
        addListeners();
    }

    private void addListeners() {
        view.getBuscarPacienteButton().addActionListener(e -> buscarPaciente());
        view.getBuscarMedicamentoButton().addActionListener(e -> buscarMedicamento());
        view.getAgregarMedicamentoButton().addActionListener(e -> agregarMedicamento());
        view.getModificarDetalleButton().addActionListener(e -> modificarDetalle());
        view.getEliminarDetalleButton().addActionListener(e -> eliminarDetalle());
        view.getGuardarRecetaButton().addActionListener(e -> guardarReceta());
        view.getCancelarButton().addActionListener(e -> view.dispose());

        // Enter para buscar
        view.getPacienteIdField().addActionListener(e -> buscarPaciente());
        view.getMedicamentoCodigoField().addActionListener(e -> buscarMedicamento());
    }

    private void buscarPaciente() {
        String criterio = view.getPacienteIdField().getText().trim();
        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ingrese ID o nombre del paciente",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<List<PacienteResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PacienteResponseDto> doInBackground() throws Exception {
                return pacienteService.listPacienteAsync(1L).get();
            }

            @Override
            protected void done() {
                try {
                    List<PacienteResponseDto> pacientes = get();
                    if (pacientes == null || pacientes.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "No se encontraron pacientes",
                                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    // Búsqueda aproximada por ID o nombre
                    List<PacienteResponseDto> coincidencias = pacientes.stream()
                            .filter(p -> p.getId().toLowerCase().contains(criterio.toLowerCase()) ||
                                    p.getNombre().toLowerCase().contains(criterio.toLowerCase()))
                            .toList();

                    if (coincidencias.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "No se encontraron pacientes con ese criterio",
                                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (coincidencias.size() == 1) {
                        seleccionarPaciente(coincidencias.get(0));
                    } else {
                        mostrarDialogoSeleccionPaciente(coincidencias);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Error al buscar paciente: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void mostrarDialogoSeleccionPaciente(List<PacienteResponseDto> pacientes) {
        String[] opciones = pacientes.stream()
                .map(p -> p.getId() + " - " + p.getNombre())
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(view,
                "Seleccione el paciente:",
                "Múltiples coincidencias",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            String idSeleccionado = seleccion.split(" - ")[0];
            PacienteResponseDto paciente = pacientes.stream()
                    .filter(p -> p.getId().equals(idSeleccionado))
                    .findFirst()
                    .orElse(null);
            if (paciente != null) {
                seleccionarPaciente(paciente);
            }
        }
    }

    private void seleccionarPaciente(PacienteResponseDto paciente) {
        view.setIdPacienteSeleccionado(paciente.getId());
        view.getPacienteNombreField().setText(paciente.getNombre());
        view.getPacienteIdField().setText(paciente.getId());
        actualizarEstadoBotonGuardar();
        JOptionPane.showMessageDialog(view, "Paciente seleccionado correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarMedicamento() {
        String criterio = view.getMedicamentoCodigoField().getText().trim();
        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ingrese código o descripción del medicamento",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<List<MedicamentoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MedicamentoResponseDto> doInBackground() throws Exception {
                return medicamentoService.listMedicamentoAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<MedicamentoResponseDto> medicamentos = get();
                    if (medicamentos == null || medicamentos.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "No se encontraron medicamentos",
                                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    // Búsqueda aproximada por código o descripción
                    List<MedicamentoResponseDto> coincidencias = medicamentos.stream()
                            .filter(m -> m.getCodigo().toLowerCase().contains(criterio.toLowerCase()) ||
                                    m.getDescripcion().toLowerCase().contains(criterio.toLowerCase()))
                            .toList();

                    if (coincidencias.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "No se encontraron medicamentos con ese criterio",
                                "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if (coincidencias.size() == 1) {
                        seleccionarMedicamento(coincidencias.get(0));
                    } else {
                        mostrarDialogoSeleccionMedicamento(coincidencias);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Error al buscar medicamento: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void mostrarDialogoSeleccionMedicamento(List<MedicamentoResponseDto> medicamentos) {
        String[] opciones = medicamentos.stream()
                .map(m -> m.getCodigo() + " - " + m.getDescripcion() + " (" + m.getPresentacion() + ")")
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(view,
                "Seleccione el medicamento:",
                "Múltiples coincidencias",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            String codigoSeleccionado = seleccion.split(" - ")[0];
            MedicamentoResponseDto medicamento = medicamentos.stream()
                    .filter(m -> m.getCodigo().equals(codigoSeleccionado))
                    .findFirst()
                    .orElse(null);
            if (medicamento != null) {
                seleccionarMedicamento(medicamento);
            }
        }
    }

    private void seleccionarMedicamento(MedicamentoResponseDto medicamento) {
        this.medicamentoSeleccionado = medicamento;
        view.getMedicamentoCodigoField().setText(medicamento.getCodigo() + " - " + medicamento.getDescripcion());
        view.getAgregarMedicamentoButton().setEnabled(true);
    }

    private void agregarMedicamento() {
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(view, "Debe buscar y seleccionar un medicamento primero",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si ya está en la receta
        for (DetalleRecetaDto d : tableModel.getAllDetalles()) {
            if (d.getIdMedicamento().equals(medicamentoSeleccionado.getCodigo())) {
                JOptionPane.showMessageDialog(view, "Este medicamento ya está en la receta. Use 'Modificar' para cambiar sus datos.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Dialog para ingresar datos
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        JTextField indicacionesField = new JTextField();
        JSpinner duracionSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 365, 1));

        panel.add(new JLabel("Cantidad:"));
        panel.add(cantidadSpinner);
        panel.add(new JLabel("Indicaciones:"));
        panel.add(indicacionesField);
        panel.add(new JLabel("Duración (días):"));
        panel.add(duracionSpinner);

        int result = JOptionPane.showConfirmDialog(view, panel,
                "Datos del medicamento: " + medicamentoSeleccionado.getDescripcion(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int cantidad = (int) cantidadSpinner.getValue();
            String indicaciones = indicacionesField.getText().trim();
            int duracion = (int) duracionSpinner.getValue();

            if (indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Las indicaciones son obligatorias",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DetalleRecetaDto detalle = new DetalleRecetaDto(
                    medicamentoSeleccionado.getCodigo(),
                    cantidad,
                    indicaciones,
                    duracion
            );

            tableModel.agregarDetalle(detalle);
            view.getMedicamentoCodigoField().setText("");
            view.getAgregarMedicamentoButton().setEnabled(false);
            medicamentoSeleccionado = null;
            actualizarEstadoBotonGuardar();
        }
    }

    private void modificarDetalle() {
        int selectedRow = view.getDetallesTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un medicamento de la tabla",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleRecetaDto detalleActual = tableModel.getDetalleAt(selectedRow);
        if (detalleActual == null) return;

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(detalleActual.getCantidad(), 1, 999, 1));
        JTextField indicacionesField = new JTextField(detalleActual.getIndicaciones());
        JSpinner duracionSpinner = new JSpinner(new SpinnerNumberModel(detalleActual.getDuracionDias(), 1, 365, 1));

        panel.add(new JLabel("Cantidad:"));
        panel.add(cantidadSpinner);
        panel.add(new JLabel("Indicaciones:"));
        panel.add(indicacionesField);
        panel.add(new JLabel("Duración (días):"));
        panel.add(duracionSpinner);

        int result = JOptionPane.showConfirmDialog(view, panel,
                "Modificar: " + detalleActual.getIdMedicamento(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int cantidad = (int) cantidadSpinner.getValue();
            String indicaciones = indicacionesField.getText().trim();
            int duracion = (int) duracionSpinner.getValue();

            if (indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Las indicaciones son obligatorias",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DetalleRecetaDto detalleModificado = new DetalleRecetaDto(
                    detalleActual.getIdMedicamento(),
                    cantidad,
                    indicaciones,
                    duracion
            );

            tableModel.actualizarDetalle(selectedRow, detalleModificado);
        }
    }

    private void eliminarDetalle() {
        int selectedRow = view.getDetallesTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un medicamento de la tabla",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleRecetaDto detalle = tableModel.getDetalleAt(selectedRow);
        if (detalle == null) return;

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Eliminar " + detalle.getIdMedicamento() + " de la receta?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.eliminarDetalle(selectedRow);
            actualizarEstadoBotonGuardar();
        }
    }

    private void guardarReceta() {
        if (view.getIdPacienteSeleccionado() == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un paciente",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tableModel.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe agregar al menos un medicamento",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idReceta = UUID.randomUUID().toString();
        AddRecetaRequestDto dto = new AddRecetaRequestDto(
                idReceta,
                view.getIdPacienteSeleccionado(),
                view.getIdMedicoActual(),
                "confeccionada",
                tableModel.getAllDetalles()
        );

        SwingWorker<RecetaResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected RecetaResponseDto doInBackground() throws Exception {
                return recetaService.addRecetaAsync(dto).get();
            }

            @Override
            protected void done() {
                try {
                    RecetaResponseDto receta = get();
                    if (receta != null) {
                        JOptionPane.showMessageDialog(view,
                                "Receta guardada exitosamente\nID: " + receta.getId(),
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        view.dispose();
                    } else {
                        JOptionPane.showMessageDialog(view,
                                "Error al guardar la receta",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view,
                            "Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void actualizarEstadoBotonGuardar() {
        boolean habilitado = view.getIdPacienteSeleccionado() != null && !tableModel.isEmpty();
        view.getGuardarRecetaButton().setEnabled(habilitado);
    }
}