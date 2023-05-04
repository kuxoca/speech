package ppzeff.recognize.server.sber.config;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import ppzeff.shared.Constants;

import java.util.concurrent.Executor;

public class BearerTokenSber extends CallCredentials {
    private final String token;

    public BearerTokenSber(String token) {
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                Metadata headers = new Metadata();
                headers.put(Constants.AUTHORIZATION_METADATA_KEY, String.format("%s %s", Constants.BEARER_TYPE, token));
                metadataApplier.apply(headers);
            } catch (Throwable e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {
        // noop
    }
}