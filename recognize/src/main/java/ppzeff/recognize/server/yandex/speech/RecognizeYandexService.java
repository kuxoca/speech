package ppzeff.recognize.server.yandex.speech;

import com.fasterxml.jackson.databind.ObjectMapper;
import ppzeff.recognize.server.Recognize;
import ppzeff.recognize.server.ServiceAccessToken;
import ppzeff.recognize.server.yandex.service.ServiceHttpClient;
import ppzeff.recognize.server.yandex.service.ServiceObjectMapper;
import ppzeff.recognize.server.yandex.service.dto.ResultYandexDto;
import ppzeff.shared.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static ppzeff.shared.Utils.getFormDataAsString;

public class RecognizeYandexService implements Recognize {
    private final ServiceAccessToken serviceAccessToken;

    public RecognizeYandexService(ServiceAccessToken serviceAccessToken) {
        this.serviceAccessToken = serviceAccessToken;
    }

    @Override
    public String recognize(byte[] bytes, String lang) {

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("lang", lang);
        urlParameters.put("folderId", Constants.YA_FOLDER_ID);

        HttpClient client = ServiceHttpClient.getClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.YA_URL_Recognize + "?" + getFormDataAsString(urlParameters)))
                .header("Authorization", String.format("%s %s", Constants.BEARER_TYPE, serviceAccessToken.getAccessToken()))
                .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                .build();

        ObjectMapper om = ServiceObjectMapper.getObjectMapper();

        HttpResponse<String> response;
        String text;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());

            ResultYandexDto resultYandexDto = om.readValue(response.body(), ResultYandexDto.class);
            text = resultYandexDto.getResult();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return text;
    }

    @Override
    public String recognize(byte[] bytes) {
        return recognize(bytes, "RU-ru");
    }


}
