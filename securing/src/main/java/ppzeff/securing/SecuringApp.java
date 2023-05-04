package ppzeff.securing;

import ppzeff.securing.server.TokenServer;
import ppzeff.shared.Constants;

public class SecuringApp {
    public static void main(String[] args) throws InterruptedException {
        TokenServer tokenServer = new TokenServer(
                Constants.SERVER_PORT_TOKEN_gRPC);
        tokenServer.start();
    }
}
