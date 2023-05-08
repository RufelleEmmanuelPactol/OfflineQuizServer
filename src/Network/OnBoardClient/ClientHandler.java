package Network.OnBoardClient;
import Main.Serialize;
import Network.AuthToken;
import Network.PortHandler;
import Network.RequestToken;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {
    protected ServerSocket serverSocket;
    protected Socket socket;

    // The signature determines what port will this client
    // bind to and listen for requests
    protected int signature;
    protected AuthToken authToken;


    public ClientHandler (String username, String password) {
        try {
            socket = new Socket("localhost", PortHandler.requestPort());
            authToken = new AuthToken(username, password);
            signature = PortHandler.clientGetSignature();
            serverSocket = new ServerSocket(signature);
            sendRequest(new RequestToken("AUTH", authToken, signature));
            // receiveRequest over here
        } catch (IOException e) {
            System.out.println("Error at ClientHandler constructor with error: " + e);
            e.printStackTrace();
        }

    }

    // Binds a session to this client
    public void bindSession (String sessionID){
        authToken.sessionID = sessionID;
    }

    public RequestToken receiveRequest () {
        return null;
        // implement this later
    }

    public void sendRequest(RequestToken requestToken) {
        requestToken.signature = signature;
        try {
            socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
        } catch (IOException e){
            System.out.println("An exception occurred with the sendRequest method with error: " + e);
            e.printStackTrace();
        }
    }






}