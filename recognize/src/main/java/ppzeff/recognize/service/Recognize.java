package ppzeff.recognize.service;

public interface Recognize {
    String recognize(byte[] bytes, String lang);

    String recognize(byte[] bytes);
}
