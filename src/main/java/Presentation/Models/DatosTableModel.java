package Presentation.Models;


import Domain.Dtos.dashboard.DatoRowDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DatosTableModel extends AbstractTableModel {
    private final String[] cols = {"Fecha", "Valor"};
    private final List<DatoRowDto> rows = new ArrayList<>();

    public void setRows(List<DatoRowDto> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return rows.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Object getValueAt(int r, int c) {
        DatoRowDto d = rows.get(r);
        switch (c) {
            case 0: return d.getFecha();
            case 1: return d.getValor();
            default: return null;
        }
    }
}
