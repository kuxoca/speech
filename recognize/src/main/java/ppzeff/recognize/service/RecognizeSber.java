package ppzeff.recognize.service;


import ppzeff.recognize.server.sber.service.ServiceSberAccessToken;
import ppzeff.recognize.server.sber.speech.ServiceSberRecognitionGRPC;

import javax.net.ssl.SSLException;

public class RecognizeSber implements Recognize {
    @Override
    public String recognize(byte[] bytes, String lang) {
        String result;
        try {
            var service = new ServiceSberRecognitionGRPC(new ServiceSberAccessToken());
            result = service.recognition(bytes);
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
