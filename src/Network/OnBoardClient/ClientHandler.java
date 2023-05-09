package Network.OnBoardClient;
import Main.Serialize;
import Network.AuthToken;
import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClientHandler {
    protected ServerSocket serverSocket;
    protected Socket socket;

    // The signature determines what port will this client
    // bind to and listen for requests
    protected int signature;
    protected AuthToken authToken;


    public ClientHandler (String username, String password) throws Exception {
        try {
            socket = PortHandler.generateSocket();
            System.out.println("Connected with port address " + socket.getPort());
            authToken = new AuthToken(username, password);
            signature = PortHandler.getClientSignature();
            serverSocket = new ServerSocket(signature);
            sendRequest(new RequestToken("AUTH", authToken, signature));
            var authSocket = serverSocket.accept();
            RequestToken token = NetworkUtils.getObject(authSocket);
            System.out.println(token.response);
            // receiveRequest over here
        } catch (IOException e) {
            System.out.println("Error at ClientHandler constructor with error: " + e);
            System.out.println("Error binding to port " + PortHandler.requestPort());
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

    public void sendRequest(RequestToken requestToken) throws Exception {
        requestToken.signature = signature;
        try {
            socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
        } catch (IOException e){
            System.out.println("An exception occurred with the sendRequest method with error: " + e);
            e.printStackTrace();
            throw e;
        }
    }






}