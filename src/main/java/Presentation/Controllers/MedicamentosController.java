package Presentation.Controllers;

import Domain.Dtos.medicamentos.*;
import Presentation.Views.medicamentos.MedicamentosView;
import Services.MedicamentoService;
import Utilities.EventType;
import Presentation.Observable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class MedicamentosController extends Observable {
    private final MedicamentosView view;
    private final MedicamentoService service;

    public MedicamentosController(MedicamentosView view, MedicamentoService service) {
        this.view = view;
        this.service = service;

        addObserver(view.getTableModel());
        loadMedicamentosAsync();
        addListeners();
    }



    private void loadMedicamentosAsync() {
        SwingWorker<List<MedicamentoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MedicamentoResponseDto> doInBackground() throws Exception {
                return service.listMedicamentoAsync().get();
            }
            @Override
            protected void done() {
                try {
                    List<MedicamentoResponseDto> meds = get();
                    view.getTableModel().setMedicamentos(meds);
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        view.getAgregarButton().addActionListener(e -> handleAdd());
        view.getActualizarButton().addActionListener(e -> handleUpdate());
        view.getBorrarButton().addActionListener(e -> handleDelete());
        view.getLimpiarButton().addActionListener(e -> view.clearFields());
        view.getBuscarButton().addActionListener(e -> handleBuscar());
        view.getMedicamentosTable().getSelectionModel().addListSelectionListener(this::handleRowSelection);
    }

    private void handleAdd() {
        String codigo = view.getCodigoTextField().getText().trim();
        String descripcion = view.getDescripcionTextField().getText().trim();
        String presentacion = view.getPresentacionTextField().getText().trim();

        if (codigo.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Código y descripción son obligatorios");
            return;
        }

        AddMedicamentoRequestDto dto = new AddMedicamentoRequestDto(codigo, descripcion, presentacion);
        SwingWorker<MedicamentoResponseDto, Void> w = new SwingWorker<>() {
            @Override
            protected MedicamentoResponseDto doInBackground() throws Exception {
                return service.addMedicamentoAsync(dto).get();
            }
            @Override
            protected void done() {
                try {
                    MedicamentoResponseDto m = get();
                    if (m != null) {
                        notifyObservers(EventType.CREATED, m);
                        view.clearFields();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        w.execute();
    }

    private void handleUpdate() {
        int row = view.getMedicamentosTable().getSelectedRow();
        if (row < 0) return;
        String codigo = view.getCodigoTextField().getText().trim();
        String descripcion = view.getDescripcionTextField().getText().trim();
        String presentacion = view.getPresentacionTextField().getText().trim();

        UpdateMedicamentoRequestDto dto = new UpdateMedicamentoRequestDto(codigo, descripcion, presentacion);
        SwingWorker<MedicamentoResponseDto, Void> w = new SwingWorker<>() {
            @Override
            protected MedicamentoResponseDto doInBackground() throws Exception {
                return service.updateMedicamentoAsync(dto).get();
            }
            @Override
            protected void done() {
                try {
                    MedicamentoResponseDto updated = get();
                    if (updated != null) {
                        notifyObservers(EventType.UPDATED, updated);
                        view.clearFields();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        w.execute();
    }

    private void handleDelete() {
        int row = view.getMedicamentosTable().getSelectedRow();
        if (row < 0) return;
        MedicamentoResponseDto sel = view.getTableModel().getMedicamentos().get(row);
        DeleteMedicamentoRequestDto dto = new DeleteMedicamentoRequestDto(sel.getCodigo());

        SwingWorker<Boolean, Void> w = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return service.deleteMedicamentoAsync(dto).get();
            }
            @Override
            protected void done() {
                try {
                    Boolean ok = get();
                    if (ok) {
                        view.getTableModel().removeMedicamentoByCodigo(sel.getCodigo());
                        view.clearFields();
                        notifyObservers(EventType.DELETED, sel.getCodigo());
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        w.execute();
    }

    private void handleBuscar() {
        String criterio = view.getBuscarTextFlied().getText().trim();
        // Puedes implementar búsqueda local en tableModel o pedir lista filtrada al servidor.
        // Aquí haré una búsqueda simple local:
        List<MedicamentoResponseDto> all = view.getTableModel().getMedicamentos();
        // filtrado simple (implementación opcional según prefieras)
    }

    private void handleRowSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = view.getMedicamentosTable().getSelectedRow();
            if (row >= 0) {
                MedicamentoResponseDto m = view.getTableModel().getMedicamentos().get(row);
                view.populateFields(m);
            }
        }
    }
}
