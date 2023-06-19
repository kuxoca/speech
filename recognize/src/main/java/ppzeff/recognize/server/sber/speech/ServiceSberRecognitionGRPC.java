package ppzeff.recognize.server.sber.speech;

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ppzeff.protogenerate.recognition.Recognition;
import ppzeff.protogenerate.recognition.SmartSpeechGrpc;
import ppzeff.recognize.server.Recognize;
import ppzeff.recognize.server.sber.config.BearerTokenSber;
import ppzeff.recognize.server.ServiceAccessToken;
import ppzeff.shared.Constants;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ServiceSberRecognitionGRPC implements Recognize {
    private final ServiceAccessToken serviceAccessToken;
    private final Recognition.RecognitionRequest configRequest;
    private final SslContext sslCtx;
    private String message;

    public ServiceSberRecognitionGRPC(ServiceAccessToken serviceAccessToken) throws SSLException {
        this.serviceAccessToken = serviceAccessToken;
        sslCtx = GrpcSslContexts.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        configRequest = Recognition.RecognitionRequest.newBuilder()
                .setOptions(
                        Recognition.RecognitionOptions.newBuilder()
                                .setAudioEncodingValue(
                                        Recognition.RecognitionOptions.AudioEncoding.OPUS_VALUE)
                                .build())
                .build();
    }

    @Override
    public String recognize(byte[] bytes) {
        return recognize(bytes, "RU-ru");
    }

    @Override
    public String recognize(byte[] bytes, String lang) {

        var channel = NettyChannelBuilder.forTarget(Constants.SMARTSPEECH).sslContext(sslCtx).build();

        try {
            sendRequest(bytes, channel);
        } finally {
            channel.shutdown();
            log.info("channel.shutdown()");
        }
        return getMsg();
    }

    private StreamObserver<Recognition.RecognitionResponse> getResponseObserver() {
        log.trace("create streamObserver");

        return new StreamObserver<>() {
            @Override
            public void onNext(Recognition.RecognitionResponse value) {
                log.trace("StreamObserver: onNext");
                List<Recognition.Hypothesis> resultsList = value.getResultsList();
                for (Recognition.Hypothesis hypothesis : resultsList) {
                    setMsg(hypothesis.getNormalizedText());
                }
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
        };
    }

    private void sendRequest(byte[] bytes, Channel channel) {
        try {

            var accessToken = serviceAccessToken.getAccessToken();

            var bearerToken = new BearerTokenSber(accessToken);
            var stub = SmartSpeechGrpc.newStub(channel).withCallCredentials(bearerToken);
            var requestStreamObserver = stub.recognize(getResponseObserver());

            requestStreamObserver.onNext(configRequest);
            requestStreamObserver.onNext(
                    Recognition.RecognitionRequest.newBuilder()
                            .setAudioChunk(ByteString.readFrom(new ByteArrayInputStream(bytes)))
                            .build()
            );
            requestStreamObserver.onCompleted();

        } catch (RuntimeException e) {
//
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void setMsg(String msg) {
        message = msg;
    }

    private synchronized String getMsg() {
        var temp = String.valueOf(message);
        message = null;
        return temp;
    }
}