package Presentation.Views.pacientes;

import Domain.Dtos.pacientes.PacienteResponseDto;
import Presentation.Controllers.PacientesController;
import Presentation.Models.PacientesTableModel;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PacientesView {
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JButton actualizarButton;
    private JButton agregarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField fechaNacimientotextField;
    private JTextField BuscarTextFlied;
    private JButton BuscarButton;
    private JButton reporteButton;
    private JScrollPane tableScrollPane;
    private JTable pacientesTable;
    private JPanel contentPane;

    private PacientesController controller;
    private PacientesTableModel tableModel;

    public PacientesView(JFrame parentFrame) {
        tableModel = new PacientesTableModel();
        pacientesTable.setModel(tableModel);
    }

    public JTextField getIdTextField() {
        return idTextField;
    }
    public JTextField getNombreTextField() {
        return nombreTextField;
    }
    public JButton getActualizarButton() {
        return actualizarButton;
    }
    public JButton getAgregarButton() {
        return agregarButton;
    }
    public JButton getLimpiarButton() {
        return limpiarButton;
    }
    public JButton getBorrarButton() {
        return borrarButton;
    }
    public JTextField getFechaNacimientotextField() {
        return fechaNacimientotextField;
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
    public JScrollPane getTableScrollPane() {
        return tableScrollPane;
    }
    public JTable getPacientesTable() {
        return pacientesTable;
    }
    public JPanel getContentPane() {
        return contentPane;
    }
    public PacientesController getController() {
        return controller;
    }
    public PacientesTableModel getTableModel() {
        return tableModel;
    }
//    private void onAgregar() {
//        try {
//            requireBound();
//            DatosForm d = readForm();
//            Date fecha = parseFecha(d.fechaNacimiento);
//            controller.agregar(d.id, d.nombre, fecha, d.telefono);
//            onLimpiar();
//        } catch (Exception ex) {
//            showError("Error al agregar: " + ex.getMessage(), ex);
//        }
//    }



    public void clearFields() {
        pacientesTable.clearSelection();
        idTextField.setText("");
        nombreTextField.setText("");
        fechaNacimientotextField.setText("");
        idTextField.requestFocus();
    }

    private String formatFecha(Date fecha) {
        if (fecha == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }


    private Date parseFecha(String textoFecha) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        return sdf.parse(textoFecha);
    }

    public void populateFields(PacienteResponseDto pacienteResponseDto) {
        idTextField.setText(pacienteResponseDto.getId());
        nombreTextField.setText(pacienteResponseDto.getNombre());
        fechaNacimientotextField.setText(formatFecha(pacienteResponseDto.getFechaNacimiento()));
    }

}
