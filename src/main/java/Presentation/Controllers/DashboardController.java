package Presentation.Controllers;

import Domain.Dtos.dashboard.DashboardStatsDto;
import Domain.Dtos.dashboard.MedicamentoResponseDto;
import Presentation.Views.dashboard.DashboardView;
import Presentation.Models.DatosTableModel;
import Services.DashboardService;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DashboardController {
    private final DashboardView view;
    private final DashboardService service;
    private final DatosTableModel tableModel;

    public DashboardController(DashboardView view, DashboardService service) {
        this.view = view;
        this.service = service;
        this.tableModel = new DatosTableModel();

        if (this.view.getDatosTable() != null) {
            this.view.getDatosTable().setModel(this.tableModel);
        }

        initListeners();
        loadDashboardData();
    }

    private void initListeners() {
        if (view.getRefrescarButton() != null) {
            view.getRefrescarButton().addActionListener(e -> loadDashboardData());
        }
    }

    private void loadDashboardData() {
        // Stats
        new SwingWorker<DashboardStatsDto, Void>() {
            @Override
            protected DashboardStatsDto doInBackground() throws Exception {
                return service.getStatsAsync().get();
            }

            @Override
            protected void done() {
                try {
                    DashboardStatsDto stats = get();
                    if (stats != null) {
                        view.updateStats(stats);
                    } else {
                        System.err.println("[DashboardController] stats null");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al obtener estadísticas: " + ex.getMessage(),
                            "Dashboard Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();

        // Medicamentos
        new SwingWorker<List<MedicamentoResponseDto>, Void>() {
            @Override
            protected List<MedicamentoResponseDto> doInBackground() throws Exception {
                return service.getMedicamentosAsync().get();
            }

            @Override
            protected void done() {
                try {
                    List<MedicamentoResponseDto> meds = get();
                    view.updateMedicamentos(meds);

                    // Opcional: si quieres mostrar medicamentos también en la tabla de datos:
                    // construir filas simples (fecha/valor) basadas en la cantidad de medicamentos
                    // (sólo como ejemplo: no cambia datos reales del servidor)
                    // tableModel.setRows(...) si tu DatosTableModel soporta DatoRowDto y get/set apropiado.
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al obtener medicamentos: " + ex.getMessage(),
                            "Dashboard Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
