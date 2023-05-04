package ppzeff.shared;

import io.grpc.Context;
import io.grpc.Metadata;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class Constants {
    // TODO: перенести в переменные в окружение
    public static final String SMARTSPEECH = "smartspeech.sber.ru";
    public static final String SERVER_ADDRESS_TOKEN_gRPC = "172.16.238.4";
    public static final int SERVER_PORT_TOKEN_gRPC = 8190;
    public static final String SERVER_ADDRESS_Rabbitmq = "172.16.238.5";
    public static final int SERVER_PORT_Rabbitmq = 5672;
    public static final String YA_URL_Access_Token = "https://iam.api.cloud.yandex.net/iam/v1/tokens";
    public static final String YA_URL_Recognize = "https://stt.api.cloud.yandex.net/speech/v1/stt:recognize";
    public static final String SBER_URL_Access_Token = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    public static final String JWT_SIGNING_KEY = System.getenv("JWT_SIGNING_KEY");
    public static final String BEARER_TYPE = "Bearer";
    public static final String BASIC_TYPE = "Basic";
    public static final String EXCHANGE_NAME = "recognition";
    public static final String ROUTING_KEY_SBER = ServerType.SBER.getServerType();
    public static final String ROUTING_KEY_YANDEX = ServerType.YANDEX.getServerType();
    public static final String CLIENT_ID_SBER_Access_Token = System.getenv("CLIENT_ID_SBER_Access_Token");
    public static final String CLIENT_Secret_SBER_Access_Token = System.getenv("CLIENT_Secret_SBER_Access_Token");
    public static final String YA_FOLDER_ID = System.getenv("YA_FOLDER_ID");
    public static final String YA_OAuth = System.getenv("YA_OAuth");
    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> SUBJECT = Context.key("subject");
}
