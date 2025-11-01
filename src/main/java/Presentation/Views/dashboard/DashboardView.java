package Presentation.Views.dashboard;

import Domain.Dtos.dashboard.DashboardStatsDto;
import Domain.Dtos.dashboard.MedicamentoResponseDto;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

public class DashboardView {
    private JPanel contentPane;
    private JPanel DatosPane;
    private JPanel MedicamentosPane;
    private JPanel RecetasPane;
    private JComboBox<String> DesdeAnocomboBox;
    private JComboBox<String> DesdeFechacomboBox;
    private JComboBox<String> HastaAnocomboBox;
    private JComboBox<String> DesdefechacomboBox;
    private JButton actualizarTablasButton;
    private JButton Checkbutton;
    private JButton MenosButton;
    private JButton borrarTodoButton;
    private JTable DatosTable;
    private JComboBox<String> MedicamentosComboBox;
    private JScrollPane DatosScrollPane;

    // Labels para totales (añadidos para mostrar estadísticas)
    private JLabel lblTotalMedicos;
    private JLabel lblTotalFarmaceuticos;
    private JLabel lblTotalPacientes;
    private JLabel lblTotalMedicamentos;
    private JLabel lblTotalRecetas;

    public DashboardView() {
        // Si usas GUI form, los componentes se inicializan en contentPane por form designer.
        // Aquí solo aseguramos que existan getters.
    }

    // --- getters usados por Controller ---
    public JPanel getContentPane() { return contentPane; }
    public JButton getRefrescarButton() { return actualizarTablasButton; }
    public JComboBox<String> getMedicamentosComboBox() { return MedicamentosComboBox; }
    public JTable getDatosTable() { return DatosTable; }

    // --- Actualización de UI desde Controller ---
    public void updateStats(DashboardStatsDto stats) {
        if (stats == null) return;
        if (lblTotalMedicos != null) lblTotalMedicos.setText(String.valueOf(stats.getTotalMedicos()));
        if (lblTotalFarmaceuticos != null) lblTotalFarmaceuticos.setText(String.valueOf(stats.getTotalFarmaceuticos()));
        if (lblTotalPacientes != null) lblTotalPacientes.setText(String.valueOf(stats.getTotalPacientes()));
        if (lblTotalMedicamentos != null) lblTotalMedicamentos.setText(String.valueOf(stats.getTotalMedicamentos()));
        if (lblTotalRecetas != null) lblTotalRecetas.setText(String.valueOf(stats.getTotalRecetas()));
    }

    public void updateMedicamentos(List<MedicamentoResponseDto> medicamentos) {
        MedicamentosComboBox.removeAllItems();
        if (medicamentos == null || medicamentos.isEmpty()) return;
        for (MedicamentoResponseDto m : medicamentos) {
            // Mostrar descripción en combo, pero puedes usar codigo si prefieres
            MedicamentosComboBox.addItem(m.getDescripcion() + " (" + m.getCodigo() + ")");
        }
    }

    // Si tu form designer creó componentes, estos getters retornarán esos mismos objetos
    // y el controller podrá enlazarlos (ver MedicosController como ejemplo).
}
