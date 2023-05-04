package ppzeff.securing.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TokenServer {
    private final int port;

    public TokenServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        Server server = ServerBuilder.forPort(port)
                .addService(new TokenServiceImp())
                .intercept(new AuthorizationServerInterceptor()).build();
        try {
            server.start();
        } catch (IOException e) {
            log.error("PORT IS BUSY: {}", e.getMessage());
        }
        log.info("gRPC TokenServer started, listening on port: {}", server.getPort());
        server.awaitTermination();
    }
}
