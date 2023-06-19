package ppzeff.recognize;

import ppzeff.recognize.server.RecognizeServer;

public class Main {
    public static void main(String[] args) throws Exception {
        RecognizeServer recognizeServer = new RecognizeServer();
        recognizeServer.start(args);
    }
}