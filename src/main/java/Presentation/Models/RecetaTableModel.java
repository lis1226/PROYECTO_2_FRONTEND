package Presentation.Models;


import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecetaTableModel extends AbstractTableModel {
    private final String[] columnas = {"Medicamento", "Cantidad", "Indicaciones", "Duración (días)"};
    private final List<Object[]> filas = new ArrayList<>();

    @Override
    public int getRowCount() { return filas.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int col) { return columnas[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return filas.get(rowIndex)[columnIndex];
    }

    public void setFilas(List<Object[]> nuevasFilas) {
        filas.clear();
        filas.addAll(nuevasFilas);
        fireTableDataChanged();
    }

    public void agregarFila(Object[] fila) {
        filas.add(fila);
        fireTableRowsInserted(filas.size() - 1, filas.size() - 1);
    }

    public void limpiar() {
        filas.clear();
        fireTableDataChanged();
    }
}

