package Presentation.Models;

import Domain.Dtos.medicos.MedicoResponseDto;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicosTableModel extends AbstractTableModel implements IObserver {
    private final List<MedicoResponseDto> medicos = new ArrayList<>();
    private final String[] cols = {"ID", "Nombre", "Especialidad"};

    //private final Class<?>[] types = {String.class, String.class, String.class};
   // private final List<Medico> allRows = new ArrayList<>();
    // private final List<Medico> rows = new ArrayList<>();

//    public void setRows(List<Medico> data) {
//        rows.clear();
//        if (data != null) rows.addAll(data);
//        fireTableDataChanged();
//    }

//    public Medico getAt(int row) {
//        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
//    }

    // -------------------------
    // AbstractTableModel methods
    // -------------------------
    @Override
    public int getRowCount() {
        return medicos.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int c) {
        return cols[c];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MedicoResponseDto medico = medicos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return medico.getId();
            case 1:
                return medico.getNombre();
            case 2:
                return medico.getEspecialidad();
            default:
                return null;
        }
    }

    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;

        switch (eventType) {
            case CREATED -> {
                MedicoResponseDto newCar = (MedicoResponseDto) data;
                medicos.add(newCar);
                fireTableRowsInserted(medicos.size() - 1, medicos.size() - 1);
            }
            case UPDATED -> {
                MedicoResponseDto updatedCar = (MedicoResponseDto) data;
                for (int i = 0; i < medicos.size(); i++) {
                    if (medicos.get(i).getId().equals(updatedCar.getId())) {
                        medicos.set(i, updatedCar);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Long deletedId = (Long) data;
                for (int i = 0; i < medicos.size(); i++) {
                    if (medicos.get(i).getId().equals(deletedId)) {
                        medicos.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    // -------------------------
    // Utility methods
    // -------------------------
    public List<MedicoResponseDto> getMedicos() {
        return new ArrayList<>(medicos);
    }

    public void setMedicos(List<MedicoResponseDto> newCars) {
        medicos.clear();
        if (newCars != null) medicos.addAll(newCars);
        fireTableDataChanged();
    }

    public void removeMedicoById(String medicoId) {
        if (medicoId == null) return;

        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId().equals(medicoId)) {
                medicos.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }

}
