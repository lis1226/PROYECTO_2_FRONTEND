package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.medicamentos.*;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicamentoService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    private final Gson gson = new Gson();

    public MedicamentoService(String host, int port) {
        super(host, port);
    }

    public Future<MedicamentoResponseDto> addMedicamentoAsync(AddMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Medicamento", "add", gson.toJson(dto), null);
            ResponseDto resp = sendRequest(req);
            if (!resp.isSuccess()) return null;
            return gson.fromJson(resp.getData(), MedicamentoResponseDto.class);
        });
    }

    public Future<List<MedicamentoResponseDto>> listMedicamentoAsync() {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Medicamento", "list", "", null);
            ResponseDto resp = sendRequest(req);
            if (!resp.isSuccess()) return null;
            ListMedicamentosResponseDto listResp = gson.fromJson(resp.getData(), ListMedicamentosResponseDto.class);
            return listResp.getMedicamentos();
        });
    }

    public Future<MedicamentoResponseDto> updateMedicamentoAsync(UpdateMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Medicamento", "update", gson.toJson(dto), null);
            ResponseDto resp = sendRequest(req);
            if (!resp.isSuccess()) return null;
            return gson.fromJson(resp.getData(), MedicamentoResponseDto.class);
        });
    }

    public Future<Boolean> deleteMedicamentoAsync(DeleteMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Medicamento", "delete", gson.toJson(dto), null);
            ResponseDto resp = sendRequest(req);
            return resp != null && resp.isSuccess();
        });
    }


    public Future<List<MedicamentoResponseDto>> buscarMedicamentosAsync(String criterio) {
        return executor.submit(() -> {
            List<MedicamentoResponseDto> todos = listMedicamentoAsync().get();
            if (todos == null || criterio == null || criterio.trim().isEmpty()) {
                return todos;
            }

            String criterioBusqueda = criterio.toLowerCase().trim();
            return todos.stream()
                    .filter(m -> m.getCodigo().toLowerCase().contains(criterioBusqueda) ||
                            m.getDescripcion().toLowerCase().contains(criterioBusqueda) ||
                            (m.getPresentacion() != null && m.getPresentacion().toLowerCase().contains(criterioBusqueda)))
                    .toList();
        });
    }
}
