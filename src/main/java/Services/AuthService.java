package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.auth.LoginRequestDto;
import Domain.Dtos.auth.UsuarioResponseDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AuthService extends BaseService{

    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public AuthService(String host, int port) {
        super(host, port);
    }

    public Future<UsuarioResponseDto> login(String usernameOrEmail, String password) {
        return executor.submit(() -> {
            LoginRequestDto loginDto = new LoginRequestDto(usernameOrEmail, password);
            RequestDto request = new RequestDto(
                    "Auth",
                    "login",
                    gson.toJson(loginDto),
                    null
            );

            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                return null;
            }

            return gson.fromJson(response.getData(), UsuarioResponseDto.class);
        });
    }



}
