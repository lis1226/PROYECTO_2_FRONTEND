package Presentation.Views.medicamentos;

import Domain.Dtos.medicamentos.MedicamentoResponseDto;
import Presentation.Models.MedicamentosTableModel;

import javax.swing.*;


public class MedicamentosView {
    private JTextField codigoTextField;
    private JTextField descripcionTextField;
    private JTextField presentacionTextField;
    private JButton agregarButton;
    private JButton limpiarButton;
    private JButton actualizarButton;
    private JButton borrarButton;
    private JTextField BuscarTextFlied;
    private JButton BuscarButton;
    private JButton reporteButton;
    private JScrollPane tableScrollPane;
    private JTable medicamentosTable;
    private JPanel contentPane;

   // private MedicamentosController controller;
    private MedicamentosTableModel tableModel;

    public MedicamentosView(JFrame parentFrame) {
        tableModel = new MedicamentosTableModel();
        medicamentosTable.setModel(tableModel);


    }

    public JPanel getContentPane() { return contentPane; }
    public JTextField getCodigoTextField() { return codigoTextField; }
    public JTextField getDescripcionTextField() { return descripcionTextField; }
    public JTextField getPresentacionTextField() { return presentacionTextField; }
    public JButton getAgregarButton() { return agregarButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JTextField getBuscarTextFlied() { return BuscarTextFlied; }
    public JButton getBuscarButton() { return BuscarButton; }
    public JTable getMedicamentosTable() { return medicamentosTable; }
    public MedicamentosTableModel getTableModel() { return tableModel; }

    public void clearFields() {
        codigoTextField.setText("");
        descripcionTextField.setText("");
        presentacionTextField.setText("");
        BuscarTextFlied.setText("");
        medicamentosTable.clearSelection();
    }

    public void populateFields(MedicamentoResponseDto m) {
        if (m == null) return;
        codigoTextField.setText(m.getCodigo());
        descripcionTextField.setText(m.getDescripcion());
        presentacionTextField.setText(m.getPresentacion());
    }

}
