package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.pacientes.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PacienteService extends BaseService {

    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public PacienteService(String host, int port) {
        super(host,port);
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    public Future<PacienteResponseDto> addPacienteAsync(AddPacienteRequestDto paciente, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Paciente", "add", gson.toJson(paciente),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), PacienteResponseDto.class);
        });
    }

    // -----------------------------
    // READ (LIST)
    // -----------------------------
    public Future<List<PacienteResponseDto>> listPacienteAsync(Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Paciente", "list", "", usuarioId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListPacientesResponseDto listResponse = gson.fromJson(response.getData(), ListPacientesResponseDto.class);
            return listResponse.getPacientes();
        });
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    public Future<PacienteResponseDto> updatePacienteAsync(UpdatePacienteRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Paciente", "update", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), PacienteResponseDto.class);
        });
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    public Future<Boolean> deletePacienteAsync(DeletePacienteRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Paciente", "delete", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }
}
