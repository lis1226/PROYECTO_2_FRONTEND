package Presentation.Views.medicos;

import Domain.Dtos.medicos.MedicoResponseDto;
import Presentation.Controllers.MedicosController;
import Presentation.Models.MedicosTableModel;

import javax.swing.*;
import java.util.List;

public class MedicosView {
    private JPanel contentPane;
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JTextField especialidadTextField;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JScrollPane tableScrollPane;
    private JTable medicosTable;
    private JTextField BuscarTextFlied;
    private JButton BuscarButton;
    private JButton reporteButton;


    private MedicosController controller;
    private MedicosTableModel tableModel;

    public MedicosView(JFrame parentFrame) {
        tableModel = new MedicosTableModel();
        medicosTable.setModel(tableModel);

    }

    public JPanel getContentPane() {
        return contentPane;
    }
    public JTextField getIdTextField() {
        return idTextField;
    }
    public JTextField getNombreTextField() {
        return nombreTextField;
    }
    public JTextField getEspecialidadTextField() {
        return especialidadTextField;
    }
    public JButton getLimpiarButton() {
        return limpiarButton;
    }
    public JButton getBorrarButton() {
        return borrarButton;
    }
    public JButton getActualizarButton() {
        return actualizarButton;
    }
    public JButton getAgregarButton() {
        return agregarButton;
    }
    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }
    public JTable getMedicosTable() {
        return medicosTable;
    }
    public JTextField getBuscarTextFlied() {
        return BuscarTextFlied;
    }
    public JButton getBuscarButton() {
        return BuscarButton;
    }
    public JButton getReporteButton() {
        return reporteButton;
    }
    public MedicosController getController() {
        return controller;
    }
    public MedicosTableModel getTableModel() {
        return tableModel;
    }

    // --- helper methods ---

    public void clearFields() {
        idTextField.setText("");
        nombreTextField.setText("");
        especialidadTextField.setText("");
        BuscarTextFlied.setText("");
        medicosTable.clearSelection();
    }

    public void populateFields(MedicoResponseDto medicoResponseDto) {
        idTextField.setText(String.valueOf(medicoResponseDto.getId()));
        nombreTextField.setText(medicoResponseDto.getNombre());
        especialidadTextField.setText(medicoResponseDto.getEspecialidad());
    }


}
