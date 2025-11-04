package Domain.Dtos.Farmaceuticos;

public class FarmaceuticoResponseDto {
    private String id;
    private Long usuarioId;  //
    private String nombre;

    public FarmaceuticoResponseDto() {}

    public FarmaceuticoResponseDto(String id, Long usuarioId, String nombre) {  // ✅ MODIFICADO
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {  // ✅ AGREGADO
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}