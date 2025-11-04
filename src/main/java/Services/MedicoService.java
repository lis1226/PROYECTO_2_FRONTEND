package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.medicos.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicoService extends BaseService {
    // Ejecutor para hilos virtuales
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public MedicoService(String host, int port) {
        super(host, port);
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    public Future<MedicoResponseDto> addMedicoAsync(AddMedicoRequestDto medico, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Medico", "add", gson.toJson(medico),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicoResponseDto.class);
        });
    }

    // -----------------------------
    // READ (LIST)
    // -----------------------------
    public Future<List<MedicoResponseDto>> listMedicoAsync(Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Medico", "list", "", usuarioId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListMedicosResponseDto listResponse = gson.fromJson(response.getData(), ListMedicosResponseDto.class);
            return listResponse.getMedicos();
        });
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    public Future<MedicoResponseDto> updateMedicoAsync(UpdateMedicoRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Medico", "update", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicoResponseDto.class);
        });
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    public Future<Boolean> deleteMedicoAsync(DeleteMedicoRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Medico", "delete", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }


    public Future<List<MedicoResponseDto>> buscarMedicosAsync(String criterio, Long usuarioId) {
        return executor.submit(() -> {
            List<MedicoResponseDto> todos = listMedicoAsync(usuarioId).get();
            if (todos == null || criterio == null || criterio.trim().isEmpty()) {
                return todos;
            }

            String criterioBusqueda = criterio.toLowerCase().trim();
            return todos.stream()
                    .filter(m -> m.getId().toLowerCase().contains(criterioBusqueda) ||
                            m.getNombre().toLowerCase().contains(criterioBusqueda) ||
                            (m.getEspecialidad() != null && m.getEspecialidad().toLowerCase().contains(criterioBusqueda)))
                    .toList();
        });
    }
}
