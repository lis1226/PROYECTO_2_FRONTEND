package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.receta.*;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecetaService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    private final Gson gson = new Gson();

    public RecetaService(String host, int port) {
        super(host, port);
    }

    public Future<RecetaResponseDto> addRecetaAsync(AddRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "add", gson.toJson(dto), null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) {
                System.err.println("[RecetaService] Error adding receta: " + (response != null ? response.getMessage() : "null response"));
                return null;
            }
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<List<RecetaResponseDto>> listRecetasAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "list", "", null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) return null;
            RecetaResponseDto[] array = gson.fromJson(response.getData(), RecetaResponseDto[].class);
            return Arrays.asList(array);
        });
    }

    public Future<RecetaResponseDto> getRecetaByIdAsync(String id) {
        return executor.submit(() -> {
            DeleteRecetaRequestDto dto = new DeleteRecetaRequestDto(id);
            RequestDto request = new RequestDto("Receta", "get", gson.toJson(dto), null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<RecetaResponseDto> updateRecetaAsync(UpdateRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "update", gson.toJson(dto), null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<Boolean> deleteRecetaAsync(DeleteRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "delete", gson.toJson(dto), null);
            ResponseDto response = sendRequest(request);
            return response != null && response.isSuccess();
        });
    }
}