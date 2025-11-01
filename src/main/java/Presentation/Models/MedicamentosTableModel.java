package Presentation.Models;

import Domain.Dtos.medicamentos.MedicamentoResponseDto;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MedicamentosTableModel extends AbstractTableModel implements IObserver {
    private final List<MedicamentoResponseDto> medicamentos = new ArrayList<>();
    private final String[] cols = {"Codigo", "Descripcion", "Presentacion"};

    @Override
    public int getRowCount() { return medicamentos.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int c) { return cols[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MedicamentoResponseDto m = medicamentos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getCodigo();
            case 1 -> m.getDescripcion();
            case 2 -> m.getPresentacion();
            default -> null;
        };
    }

    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;
        switch (eventType) {
            case CREATED -> {
                MedicamentoResponseDto created = (MedicamentoResponseDto) data;
                medicamentos.add(created);
                fireTableRowsInserted(medicamentos.size()-1, medicamentos.size()-1);
            }
            case UPDATED -> {
                MedicamentoResponseDto updated = (MedicamentoResponseDto) data;
                for (int i=0;i<medicamentos.size();i++){
                    if (medicamentos.get(i).getCodigo().equals(updated.getCodigo())){
                        medicamentos.set(i, updated);
                        fireTableRowsUpdated(i,i);
                        break;
                    }
                }
            }
            case DELETED -> {
                String codigo = (String) data;
                for (int i=0;i<medicamentos.size();i++){
                    if (medicamentos.get(i).getCodigo().equals(codigo)){
                        medicamentos.remove(i);
                        fireTableRowsDeleted(i,i);
                        break;
                    }
                }
            }
        }
    }

    public List<MedicamentoResponseDto> getMedicamentos() { return new ArrayList<>(medicamentos); }

    public void setMedicamentos(List<MedicamentoResponseDto> nuevos) {
        medicamentos.clear();
        if (nuevos != null) medicamentos.addAll(nuevos);
        fireTableDataChanged();
    }

    public void removeMedicamentoByCodigo(String codigo) {
        if (codigo == null) return;
        for (int i=0;i<medicamentos.size();i++){
            if (medicamentos.get(i).getCodigo().equals(codigo)){
                medicamentos.remove(i);
                fireTableRowsDeleted(i,i);
                break;
            }
        }
    }
}
