package Domain.Dtos.medicos;

import java.util.List;

public class ListMedicosResponseDto {
    private List<MedicoResponseDto> medicos;

    public ListMedicosResponseDto() {
    }

    public ListMedicosResponseDto(List<MedicoResponseDto> medicos) {
        this.medicos = medicos;
    }

    public List<MedicoResponseDto> getMedicos() {
        return medicos;
    }

    public void setMedicos(List<MedicoResponseDto> medicos) {
        this.medicos = medicos;
    }
}
