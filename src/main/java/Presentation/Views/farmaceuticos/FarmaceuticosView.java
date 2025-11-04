package Presentation.Views.farmaceuticos;

import Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto;
import Presentation.Controllers.FarmaceuticosController;
import Presentation.Models.FarmaceuticosTableModel;


import javax.swing.*;
import java.util.List;


public class FarmaceuticosView {
    private JPanel contentPane;
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JScrollPane tableScrollPane;
    private JTable farmaceuticosTable;
    private JTextField BuscarTextFlied;
    private JButton BuscarButton;
    private JButton reporteButton;

    private FarmaceuticosController controller;
    private FarmaceuticosTableModel tableModel;

    public FarmaceuticosView (JFrame parentFrame) {
        tableModel = new FarmaceuticosTableModel();
        farmaceuticosTable.setModel(tableModel);
    }

    public JPanel getContentPane() { return contentPane; }
    public JTextField getIdTextField() { return idTextField; }
    public JTextField getNombreTextField() { return nombreTextField; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getBorrarButton() { return borrarButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getAgregarButton() { return agregarButton; }
    public JTable getTable() { return farmaceuticosTable; }
    public FarmaceuticosTableModel getTableModel() { return tableModel; }
    public JButton getBuscarButton() { return BuscarButton; }
    public JTextField getBuscarTextFlied() { return BuscarTextFlied; }


    public void clearFields() {
        idTextField.setText("");
        nombreTextField.setText("");
        BuscarTextFlied.setText("");
        farmaceuticosTable.clearSelection();
    }

    public void populateFields(FarmaceuticoResponseDto dto) {
        idTextField.setText(dto.getId());
        nombreTextField.setText(dto.getNombre());
    }
}
