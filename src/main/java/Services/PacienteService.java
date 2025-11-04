package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.pacientes.*;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PacienteService extends BaseService {

    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public PacienteService(String host, int port) {
        super(host, port);
        // Configurar Gson con adaptador para Date
        this.gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, context) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return new JsonPrimitive(sdf.format(date));
                })
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type, context) -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        sdf.setLenient(false);
                        return sdf.parse(json.getAsString());
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing date: " + e.getMessage());
                    }
                })
                .create();
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    public Future<PacienteResponseDto> addPacienteAsync(AddPacienteRequestDto paciente, Long usuarioId) {
        return executor.submit(() -> {
            System.out.println("[PacienteService] Sending add request for: " + paciente.getId());
            RequestDto request = new RequestDto("Paciente", "add", gson.toJson(paciente),
                    usuarioId != null ? usuarioId.toString() : "");
            System.out.println("[PacienteService] Request JSON: " + gson.toJson(request));
            ResponseDto response = sendRequest(request);
            System.out.println("[PacienteService] Response: " + response.getMessage());
            if (!response.isSuccess()) {
                System.err.println("[PacienteService] Failed to add paciente: " + response.getMessage());
                return null;
            }
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

    // Método para búsqueda
    public Future<List<PacienteResponseDto>> buscarPacientesAsync(String criterio, Long usuarioId) {
        return executor.submit(() -> {
            List<PacienteResponseDto> todos = listPacienteAsync(usuarioId).get();
            if (todos == null || criterio == null || criterio.trim().isEmpty()) {
                return todos;
            }

            String criterioBusqueda = criterio.toLowerCase().trim();
            return todos.stream()
                    .filter(p -> p.getId().toLowerCase().contains(criterioBusqueda) ||
                            p.getNombre().toLowerCase().contains(criterioBusqueda))
                    .toList();
        });
    }
}
