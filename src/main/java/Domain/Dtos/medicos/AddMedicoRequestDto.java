package Domain.Dtos.medicos;

public class AddMedicoRequestDto {
    private String id;
    private Long usuarioId;
    private String nombre;
    private String especialidad;

    public AddMedicoRequestDto() {}

    public AddMedicoRequestDto(String id, Long usuarioId, String nombre, String especialidad) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public AddMedicoRequestDto(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
