package Domain.Dtos.Farmaceuticos;

import java.util.List;

public class ListFarmaceuticosResponseDto {
    private List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> farmaceuticos;

    public ListFarmaceuticosResponseDto() {}
    public ListFarmaceuticosResponseDto(List<FarmaceuticoResponseDto> farmaceuticos) {
        this.farmaceuticos = farmaceuticos;
    }

    public List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> getFarmaceuticos() { return farmaceuticos; }
    public void setFarmaceuticos(List<FarmaceuticoResponseDto> farmaceuticos) { this.farmaceuticos = farmaceuticos; }
}
