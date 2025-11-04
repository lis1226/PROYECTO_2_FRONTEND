package Presentation.Models;

import Domain.Dtos.receta.DetalleRecetaDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaTableModel extends AbstractTableModel {
    private final String[] columnas = {"Medicamento", "Cantidad", "Indicaciones", "Duración (días)"};
    private final List<DetalleRecetaDto> detalles = new ArrayList<>();

    @Override
    public int getRowCount() {
        return detalles.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetalleRecetaDto d = detalles.get(rowIndex);
        switch (columnIndex) {
            case 0: return d.getIdMedicamento();
            case 1: return d.getCantidad();
            case 2: return d.getIndicaciones();
            case 3: return d.getDuracionDias();
            default: return null;
        }
    }

    public void agregarDetalle(DetalleRecetaDto detalle) {
        detalles.add(detalle);
        fireTableRowsInserted(detalles.size() - 1, detalles.size() - 1);
    }

    public void actualizarDetalle(int index, DetalleRecetaDto detalle) {
        if (index >= 0 && index < detalles.size()) {
            detalles.set(index, detalle);
            fireTableRowsUpdated(index, index);
        }
    }

    public void eliminarDetalle(int index) {
        if (index >= 0 && index < detalles.size()) {
            detalles.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public DetalleRecetaDto getDetalleAt(int index) {
        return (index >= 0 && index < detalles.size()) ? detalles.get(index) : null;
    }

    public List<DetalleRecetaDto> getAllDetalles() {
        return new ArrayList<>(detalles);
    }

    public void limpiar() {
        detalles.clear();
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return detalles.isEmpty();
    }

    public void agregarFila(Object[] objects) {
    }
}