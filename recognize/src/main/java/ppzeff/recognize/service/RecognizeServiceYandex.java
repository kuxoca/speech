package ppzeff.recognize.service;


import ppzeff.recognize.server.yandex.service.ServiceAccessTokenYandex;
import ppzeff.recognize.server.yandex.speech.RecognizeYandexService;

public class RecognizeServiceYandex implements RecognizeService {
    @Override
    public String recognize(byte[] bytes, String lang) {
        String result;
        var service = new RecognizeYandexService(new ServiceAccessTokenYandex());
        result = service.recognize(bytes, lang);
        return result;
    }

    @Override
    public String recognize(byte[] bytes) {
        return recognize(bytes, "ru-Ru");
    }
}
