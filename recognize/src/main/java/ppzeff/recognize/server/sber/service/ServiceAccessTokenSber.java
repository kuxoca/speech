package ppzeff.recognize.server.sber.service;

import authentication.v1.Empty;
import authentication.v1.MassageResponse;
import authentication.v1.TokenServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import ppzeff.recognize.server.ServiceAccessToken;
import ppzeff.recognize.server.sber.config.BearerTokenSber;
import ppzeff.shared.Constants;
import ppzeff.shared.ServerType;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
public class ServiceAccessTokenSber implements ServiceAccessToken {

    @Override
    public String getAccessToken() {

        var channel = ManagedChannelBuilder.forAddress(
                        Constants.SERVER_ADDRESS_TOKEN_gRPC,
                        Constants.SERVER_PORT_TOKEN_gRPC)
                .usePlaintext()
                .build();

        var bearerTokenLocal = new BearerTokenSber(getJwt());
        var stub = TokenServiceGrpc.newStub(channel)
                .withCallCredentials(bearerTokenLocal);

        final CompletableFuture<String> response = new CompletableFuture<>();
        stub.getToken(Empty.getDefaultInstance(), new StreamObserver<>() {
            @Override
            public void onNext(MassageResponse value) {
                response.complete(value.getToken());
            }

            @Override
            public void onError(Throwable t) {
                log.trace("StreamObserver: onError");
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.trace("StreamObserver: onCompleted");

            }
        });
        channel.shutdown();
        try {
            return response.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getJwt() {
        return Jwts.builder()
                .setSubject(ServerType.SBER.getServerType())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                .compact();
    }
}
