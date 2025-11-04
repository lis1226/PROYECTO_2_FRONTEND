package Domain.Dtos.Farmaceuticos;

public class AddFarmaceuticoRequestDto {
    private String id;
    private Long usuarioId;  //
    private String nombre;

    public AddFarmaceuticoRequestDto() {}

    public AddFarmaceuticoRequestDto(String id, String nombre) {
        this.id = id;
        this.usuarioId = null;  //
        this.nombre = nombre;
    }

    // Constructor completo por si lo necesitas después
    public AddFarmaceuticoRequestDto(String id, Long usuarioId, String nombre) {
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

    public Long getUsuarioId() {  //
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