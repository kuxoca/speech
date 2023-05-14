package ppzeff.recognize.service;


import ppzeff.recognize.server.sber.service.ServiceAccessTokenSber;
import ppzeff.recognize.server.sber.speech.ServiceSberRecognitionGRPC;

import javax.net.ssl.SSLException;

public class RecognizeServiceSber implements RecognizeService {
    @Override
    public String recognize(byte[] bytes, String lang) {
        String result;
        try {
            var service = new ServiceSberRecognitionGRPC(new ServiceAccessTokenSber());
            result = service.recognize(bytes, lang);
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public String recognize(byte[] bytes) {
        return recognize(bytes, "ru-Ru");
    }
}
