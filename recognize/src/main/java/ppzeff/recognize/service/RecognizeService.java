package ppzeff.recognize.service;

public interface RecognizeService {
    String recognize(byte[] bytes, String lang);

    String recognize(byte[] bytes);
}
