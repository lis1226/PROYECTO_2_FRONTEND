package Domain.Dtos.pacientes;

import java.util.Date;

public class AddPacienteRequestDto {
    private String id;
    private Long usuarioId;
    private String nombre;
    private Date fechaNacimiento;

    public AddPacienteRequestDto(String id, String nombre, Date date) {
    }



    public String getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
}
