package Domain.Dtos.Farmaceuticos;

public class DeleteFarmaceuticoRequestDto {
    private String id;

    public DeleteFarmaceuticoRequestDto() {}
    public DeleteFarmaceuticoRequestDto(String id) { this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
