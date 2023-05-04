package ppzeff.recognize.server.sber.service;

import authentication.v1.Empty;
import authentication.v1.TokenServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import ppzeff.recognize.server.sber.config.BearerTokenSber;
import ppzeff.shared.Constants;
import ppzeff.shared.ServerType;

import java.util.Date;


@Slf4j
public class ServiceSberAccessToken implements ServiceAccessToken {

    @Override
    public String getAccessToken() {

        var channel = ManagedChannelBuilder.forAddress(
                Constants.SERVER_ADDRESS_TOKEN_gRPC,
                Constants.SERVER_PORT_TOKEN_gRPC).usePlaintext().build();

        var bearerTokenLocal = new BearerTokenSber(getJwt());
        var stub = TokenServiceGrpc.newBlockingStub(channel)
                .withCallCredentials(bearerTokenLocal);

        var response = stub.getToken(Empty.getDefaultInstance());
        channel.shutdown();
        return response.getToken();
    }

    private static String getJwt() {
        return Jwts.builder()
                .setSubject(ServerType.SBER.getServerType())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                .compact();
    }
}
