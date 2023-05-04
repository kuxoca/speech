package ppzeff.securing.autyandex.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ppzeff.securing.ServiceAccessToken;
import ppzeff.securing.autsber.service.ServiceObjectMapper;
import ppzeff.securing.autyandex.entity.ResponseYandexIamToken;
import ppzeff.shared.Constants;
import ppzeff.shared.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Slf4j
public class ServiceAccessTokenYandex implements ServiceAccessToken {
    private static ServiceAccessTokenYandex instance;
    private ResponseYandexIamToken accessToken;
    private final ObjectMapper objectMapper;
    private final Long refreshPeriod = 25 * 60_000L;

    public static synchronized ServiceAccessTokenYandex getInstance() {
        if (instance == null) {
            synchronized (ServiceAccessTokenYandex.class) {
                if (instance == null) {
                    instance = new ServiceAccessTokenYandex(ServiceObjectMapper.getObjectMapper());
                }
            }
        }
        return instance;
    }

    private ServiceAccessTokenYandex(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        refreshAccessToken();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                refreshAccessToken();
            }
        }, refreshPeriod, refreshPeriod);

    }

    @Override
    public String getAccessToken() {
        return String.valueOf(accessToken.getIamToken());
    }

    private HttpRequest createRequest(UUID uuid) {
        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("yandexPassportOauthToken", Constants.YA_OAuth);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.YA_URL_Access_Token + "?" + Utils.getFormDataAsString(urlParameters)))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        return request;
    }

    private void refreshAccessToken() {
        log.info("sending Http request to yandex: refreshAccessToken");
        HttpRequest request = createRequest(UUID.randomUUID());
        try {
            var client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 400 ->
                        log.error("{}", "400: Bad Request. Неправильный или некорректный запрос\ndata from server: " + response.body());
                case 401 -> log.error("{}", "401: Unauthorized. Не авторизован");
                case 500 -> log.error("{}", "500: Internal Server Error. Внутренняя ошибка сервера");
                case 200 -> {
                    log.trace("{}", "200: Ok. Ок");
                    accessToken = objectMapper.readValue(response.body(), ResponseYandexIamToken.class);
                }
                default -> {
                    log.info("{}: {}", response.statusCode(), response.body());
                    throw new RuntimeException();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}