package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.administrador.*;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdministradorService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    private final Gson gson = new Gson();

    public AdministradorService(String host, int port) {
        super(host, port);
    }

    public Future<AdministradorResponseDto> addAdministradorAsync(AddAdministradorRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Administrador", "add", gson.toJson(dto), usuarioId != null ? usuarioId.toString() : "");
            ResponseDto resp = sendRequest(req);
            if (resp == null || !resp.isSuccess()) return null;
            return gson.fromJson(resp.getData(), AdministradorResponseDto.class);
        });
    }

    public Future<List<AdministradorResponseDto>> listAdministradoresAsync(Long usuarioId) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Administrador", "list", "", usuarioId != null ? usuarioId.toString() : "");
            ResponseDto resp = sendRequest(req);
            if (resp == null || !resp.isSuccess()) return null;
            ListAdministradoresResponseDto list = gson.fromJson(resp.getData(), ListAdministradoresResponseDto.class);
            return list.getAdministradores();
        });
    }

    public Future<Boolean> deleteAdministradorAsync(DeleteAdministradorRequestDto dto, Long usuarioId) {
        return executor.submit(() -> {
            RequestDto req = new RequestDto("Administrador", "delete", gson.toJson(dto), usuarioId != null ? usuarioId.toString() : "");
            ResponseDto resp = sendRequest(req);
            return resp != null && resp.isSuccess();
        });
    }
}
