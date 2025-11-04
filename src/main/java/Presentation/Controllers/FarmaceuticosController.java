package Presentation.Controllers;

import Domain.Dtos.Farmaceuticos.*;
import Presentation.Observable;
import Presentation.Views.farmaceuticos.FarmaceuticosView;
import Services.FarmaceuticoService;
import Utilities.EventType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class FarmaceuticosController extends Observable {
    private final FarmaceuticosView view;
    private final FarmaceuticoService service;

    public FarmaceuticosController(FarmaceuticosView view, FarmaceuticoService service) {
        this.view = view;
        this.service = service;

        addObserver(view.getTableModel());
        loadFarmaceuticosAsync();
        addListeners();
    }


    private void loadFarmaceuticosAsync() {
        SwingWorker<List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> doInBackground() throws Exception {
                return service.listFarmaceuticoAsync(1L).get();
            }

            @Override
            protected void done() {
                try {
                    List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> list = get();
                    view.getTableModel().setFarmaceuticos(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void addListeners() {
        view.getAgregarButton().addActionListener(e -> handleAdd());
        view.getActualizarButton().addActionListener(e -> handleUpdate());
        view.getBorrarButton().addActionListener(e -> handleDelete());
        view.getLimpiarButton().addActionListener(e -> handleClear());
        view.getTable().getSelectionModel().addListSelectionListener(this::handleRowSelect);
    }

    private void handleAdd() {
        String id = view.getIdTextField().getText().trim();
        String nombre = view.getNombreTextField().getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID y un nombre.");
            return;
        }

        AddFarmaceuticoRequestDto dto = new AddFarmaceuticoRequestDto(id, nombre);

        SwingWorker<FarmaceuticoResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected FarmaceuticoResponseDto doInBackground() throws Exception {
                return service.addFarmaceuticoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    FarmaceuticoResponseDto result = get();
                    if (result != null) {
                        notifyObservers(EventType.CREATED, result);
                        view.clearFields();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void handleUpdate() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) return;

        FarmaceuticoResponseDto selected = view.getTableModel().getFarmaceuticos().get(row);
        String nombre = view.getNombreTextField().getText().trim();

        UpdateFarmaceuticoRequestDto dto = new UpdateFarmaceuticoRequestDto(selected.getId(), nombre);

        SwingWorker<FarmaceuticoResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected FarmaceuticoResponseDto doInBackground() throws Exception {
                return service.updateFarmaceuticoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    FarmaceuticoResponseDto updated = get();
                    if (updated != null) {
                        notifyObservers(EventType.UPDATED, updated);
                        view.clearFields();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) return;

        FarmaceuticoResponseDto selected = view.getTableModel().getFarmaceuticos().get(row);
        DeleteFarmaceuticoRequestDto dto = new DeleteFarmaceuticoRequestDto(selected.getId());

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return service.deleteFarmaceuticoAsync(dto, null).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success) {
                        view.getTableModel().removeFarmaceuticoById(selected.getId());
                        view.clearFields();
                        notifyObservers(EventType.DELETED, selected.getId());
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
    }

    private void handleClear() {
        view.clearFields();
    }

    private void handleRowSelect(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = view.getTable().getSelectedRow();
            if (row >= 0) {
                Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto f = view.getTableModel().getFarmaceuticos().get(row);
                view.populateFields(f);
            }
        }
    }
}
