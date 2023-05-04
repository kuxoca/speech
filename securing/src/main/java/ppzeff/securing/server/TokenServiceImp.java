package ppzeff.securing.server;

import authentication.v1.Empty;
import authentication.v1.MassageResponse;
import authentication.v1.TokenServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ppzeff.securing.autsber.service.ServiceAccessTokenSber;
import ppzeff.securing.autyandex.service.ServiceAccessTokenYandex;
import ppzeff.shared.Constants;

@Slf4j
class TokenServiceImp extends TokenServiceGrpc.TokenServiceImplBase {

    public TokenServiceImp() {
    }

    @Override
    public void getToken(Empty request, StreamObserver<MassageResponse> responseObserver) {
        String subject = Constants.SUBJECT.get();
        switch (subject) {
            case "yandex" -> {
                var response = MassageResponse.newBuilder().setToken(
                                ServiceAccessTokenYandex.getInstance().getAccessToken())
                        .build();
                log.info("send yandex token");
                responseObserver.onNext(response);
            }
            case "sber" -> {
                var response = MassageResponse.newBuilder().setToken(
                                ServiceAccessTokenSber.getInstance().getAccessToken())
                        .build();
                log.info("send sber token");
                responseObserver.onNext(response);
            }
            default -> log.info("default");
        }
        responseObserver.onCompleted();
    }
}