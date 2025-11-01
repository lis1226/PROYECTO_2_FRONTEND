package Presentation.Models;

import Domain.Dtos.administrador.AdministradorResponseDto;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AdministradoresTableModel extends AbstractTableModel {
    private final List<AdministradorResponseDto> rows = new ArrayList<>();
    private final String[] cols = {"ID", "Usuario", "Email", "Rol", "Creado"};

    @Override
    public int getRowCount() { return rows.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AdministradorResponseDto a = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> a.getId();
            case 1 -> a.getUsername();
            case 2 -> a.getEmail();
            case 3 -> a.getRol();
            case 4 -> a.getCreatedAt();
            default -> null;
        };
    }

    public void setAdministradores(List<AdministradorResponseDto> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public void addAdministrador(AdministradorResponseDto dto) {
        rows.add(dto);
        fireTableRowsInserted(rows.size()-1, rows.size()-1);
    }

    public AdministradorResponseDto getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    public void removeAt(int row) {
        if (row >=0 && row < rows.size()) {
            rows.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
