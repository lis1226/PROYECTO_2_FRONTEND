package Services;

import com.google.gson.Gson;
import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;


public class SocketClient implements AutoCloseable {
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final Gson gson = new Gson();

    // timeouts (puedes ajustar)
    private final int connectTimeoutMs = 3000;
    private final int readTimeoutMs = 15000;

    // executor para llamadas asincrónicas
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Intentar conectar (no vuelve a lanzar si ya conectado)
    public synchronized void connect() throws IOException {
        if (isConnected()) return;
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), connectTimeoutMs);
        socket.setSoTimeout(readTimeoutMs);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public synchronized boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    private synchronized void ensureConnected() throws IOException {
        if (!isConnected()) connect();
    }

    /**
     * Enviar RequestDto y recibir ResponseDto (bloqueante).
     * Usa la firma de RequestDto con 4 parámetros (controller, request, data, token).
     */
    public synchronized ResponseDto send(RequestDto request) throws IOException {
        if (request == null) throw new IllegalArgumentException("Request null");

        ensureConnected();

        String jsonReq = gson.toJson(request);
        // Enviar una línea
        writer.write(jsonReq);
        writer.newLine();
        writer.flush();

        // Leer respuesta (una línea)
        String jsonResp;
        try {
            jsonResp = reader.readLine();
        } catch (SocketTimeoutException ste) {
            // intentar cerrar para forzar reconexión la próxima vez
            close();
            throw new IOException("Timeout al leer respuesta del servidor", ste);
        }

        if (jsonResp == null) {
            // servidor cerró la conexión inesperadamente
            close();
            throw new IOException("Conexión cerrada por el servidor");
        }

        return gson.fromJson(jsonResp, ResponseDto.class);
    }

    /**
     * Envío no bloqueante devuelve Future<ResponseDto>.
     */
    public Future<ResponseDto> sendAsync(RequestDto request) {
        return executor.submit(() -> {
            try {
                return send(request);
            } catch (IOException e) {
                throw new ExecutionException(e);
            }
        });
    }

    @Override
    public synchronized void close() {
        try { if (reader != null) reader.close(); } catch (IOException ignored) {}
        try { if (writer != null) writer.close(); } catch (IOException ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        reader = null;
        writer = null;
        socket = null;
    }

    /**
     * Llamar al cerrar la aplicación para apagar el executor y cerrar socket.
     */
    public void shutdown() {
        close();
        executor.shutdownNow();
    }
}
