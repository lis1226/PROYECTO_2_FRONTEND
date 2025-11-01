package Presentation.Controllers;

import Domain.Dtos.dashboard.DashboardStatsDto;
import Domain.Dtos.dashboard.MedicamentoResponseDto;
import Presentation.Views.dashboard.DashboardView;
import Services.DashboardService;
import javax.swing.*;
import java.util.List;

public class DashboardController {
    private final DashboardView view;
    private final DashboardService service;

    public DashboardController(DashboardView view, DashboardService service) {
        this.view = view;
        this.service = service;
        addListeners();
        loadAllAsync();
    }

    private void addListeners() {
        view.getRefrescarButton().addActionListener(e -> loadAllAsync());
    }

    private void loadAllAsync() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                DashboardStatsDto stats = service.getStatsAsync().get();
                List<MedicamentoResponseDto> meds = service.getMedicamentosAsync().get();
                SwingUtilities.invokeLater(() -> {
                    view.updateStats(stats);
                    view.updateMedicamentos(meds);
                });
                return null;
            }
        };
        worker.execute();
    }
}
