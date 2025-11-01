package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.dashboard.DashboardStatsDto;
import Domain.Dtos.dashboard.MedicamentoResponseDto;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class DashboardService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
    private final Gson gson = new Gson();

    public DashboardService(String host, int port) {
        super(host, port);
    }

    public Future<DashboardStatsDto> getStatsAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Dashboard", "getStats", "", null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) return null;
            return gson.fromJson(response.getData(), DashboardStatsDto.class);
        });
    }

    public Future<List<MedicamentoResponseDto>> getMedicamentosAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Dashboard", "listMedicamentos", "", null);
            ResponseDto response = sendRequest(request);
            if (response == null || !response.isSuccess()) return List.of();
            MedicamentoResponseDto[] meds = gson.fromJson(response.getData(), MedicamentoResponseDto[].class);
            return Arrays.asList(meds);
        });
    }
}
