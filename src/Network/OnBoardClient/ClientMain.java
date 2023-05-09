package Network.OnBoardClient;

import Network.OnBoardServer.ServerMain;
import Network.RequestToken;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {
       ClientHandler clientHandler = new ClientHandler("user", "pass");
       clientHandler.sendRequest(new RequestToken("QUIZ", 1));
    }

}
