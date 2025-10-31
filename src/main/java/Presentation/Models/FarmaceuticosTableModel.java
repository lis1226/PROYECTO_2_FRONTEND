package Presentation.Models;

import Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FarmaceuticosTableModel extends AbstractTableModel implements IObserver {
    private final List<FarmaceuticoResponseDto> farmaceuticos = new ArrayList<>();
    private final String[] cols = {"ID", "Nombre"};

    @Override
    public int getRowCount() { return farmaceuticos.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int c) { return cols[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FarmaceuticoResponseDto f = farmaceuticos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> f.getId();
            case 1 -> f.getNombre();
            default -> null;
        };
    }

    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;
        switch (eventType) {
            case CREATED -> {
                FarmaceuticoResponseDto nuevo = (FarmaceuticoResponseDto) data;
                farmaceuticos.add(nuevo);
                fireTableRowsInserted(farmaceuticos.size() - 1, farmaceuticos.size() - 1);
            }
            case UPDATED -> {
                FarmaceuticoResponseDto actualizado = (FarmaceuticoResponseDto) data;
                for (int i = 0; i < farmaceuticos.size(); i++) {
                    if (farmaceuticos.get(i).getId().equals(actualizado.getId())) {
                        farmaceuticos.set(i, actualizado);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                String id = (String) data;
                for (int i = 0; i < farmaceuticos.size(); i++) {
                    if (farmaceuticos.get(i).getId().equals(id)) {
                        farmaceuticos.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    public void setFarmaceuticos(List<FarmaceuticoResponseDto> list) {
        farmaceuticos.clear();
        if (list != null) farmaceuticos.addAll(list);
        fireTableDataChanged();
    }

    public List<FarmaceuticoResponseDto> getFarmaceuticos() {
        return farmaceuticos;
    }

    public void removeFarmaceuticoById(String id) {
        farmaceuticos.removeIf(f -> f.getId().equals(id));
        fireTableDataChanged();
    }
}
