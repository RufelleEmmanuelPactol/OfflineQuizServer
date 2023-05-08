package Network.OnBoardServer;

import Network.RequestToken;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            ServeHandler s = new ServeHandler();
            s.start();
            s.join();
        }
    }
}
