package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.Farmaceuticos.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FarmaceuticoService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public FarmaceuticoService(String host, int port) {
        super(host, port);
    }

    public Future<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> addFarmaceuticoAsync(AddFarmaceuticoRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Farmaceutico", "add", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto.class);
        });
    }

    public Future<List<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto>> listFarmaceuticoAsync(Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Farmaceutico", "list", "", usuarioId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            Domain.Dtos.Farmaceuticos.ListFarmaceuticosResponseDto listResponse = gson.fromJson(response.getData(), Domain.Dtos.Farmaceuticos.ListFarmaceuticosResponseDto.class);
            return listResponse.getFarmaceuticos();
        });
    }

    public Future<Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto> updateFarmaceuticoAsync(Domain.Dtos.Farmaceuticos.UpdateFarmaceuticoRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Farmaceutico", "update", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), Domain.Dtos.Farmaceuticos.FarmaceuticoResponseDto.class);
        });
    }

    public Future<Boolean> deleteFarmaceuticoAsync(Domain.Dtos.Farmaceuticos.DeleteFarmaceuticoRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Farmaceutico", "delete", gson.toJson(dto),
                    usuarioId != null ? usuarioId.toString() : "");
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }
}
