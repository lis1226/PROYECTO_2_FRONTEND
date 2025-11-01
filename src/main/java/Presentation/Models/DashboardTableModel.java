package Presentation.Models;



import Domain.Dtos.medicamentos.MedicamentoResponseDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DashboardTableModel extends AbstractTableModel {
    private final String[] cols = {"Código", "Descripción", "Presentación"};
    private final List<MedicamentoResponseDto> rows = new ArrayList<>();

    public void setRows(List<MedicamentoResponseDto> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public MedicamentoResponseDto getAt(int r) {
        return (r >= 0 && r < rows.size()) ? rows.get(r) : null;
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Object getValueAt(int r, int c) {
        MedicamentoResponseDto m = rows.get(r);
        switch (c) {
            case 0: return m.getCodigo();
            case 1: return m.getDescripcion();
            case 2: return m.getPresentacion();
            default: return null;
        }
    }
}

