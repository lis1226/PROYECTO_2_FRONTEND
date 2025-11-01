package Presentation.Models;

import Domain.Dtos.pacientes.PacienteResponseDto;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PacientesTableModel extends AbstractTableModel implements IObserver {
    private final List<PacienteResponseDto> pacientes = new ArrayList<>();
    private final String[] cols = {"ID", "Nombre", "Fecha de Nacimiento"};

    @Override
    public int getRowCount() {
        return pacientes.size();
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
        PacienteResponseDto paciente = pacientes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return paciente.getId();
            case 1:
                return paciente.getNombre();
            case 2:
                return paciente.getFechaNacimiento();
            default:
                return null;
        }
    }

    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;

        switch (eventType) {
            case CREATED -> {
                PacienteResponseDto newCar = (PacienteResponseDto) data;
                pacientes.add(newCar);
                fireTableRowsInserted(pacientes.size() - 1, pacientes.size() - 1);
            }
            case UPDATED -> {
                PacienteResponseDto updatedCar = (PacienteResponseDto) data;
                for (int i = 0; i < pacientes.size(); i++) {
                    if (pacientes.get(i).getId().equals(updatedCar.getId())) {
                        pacientes.set(i, updatedCar);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Long deletedId = (Long) data;
                for (int i = 0; i < pacientes.size(); i++) {
                    if (pacientes.get(i).getId().equals(deletedId)) {
                        pacientes.remove(i);
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
    public List<PacienteResponseDto> getPacientes() {
        return new ArrayList<>(pacientes);
    }

    public void setPacientes(List<PacienteResponseDto> newCars) {
        pacientes.clear();
        if (newCars != null) pacientes.addAll(newCars);
        fireTableDataChanged();
    }

    public void removePacienteById(String pacienteId) {
        if (pacienteId == null) return;

        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId().equals(pacienteId)) {
                pacientes.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }
}
