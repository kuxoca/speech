package ppzeff.recognize.service;


import ppzeff.recognize.server.yandex.speech.RecognizeYandexService;

public class RecognizeYandex implements Recognize {
    @Override
    public String recognize(byte[] bytes, String lang) {
        String result;
        var service = new RecognizeYandexService();
        result = service.recognize(bytes, lang);
        return result;
    }

    @Override
    public String recognize(byte[] bytes) {
        return recognize(bytes, "ru-Ru");
    }
}
