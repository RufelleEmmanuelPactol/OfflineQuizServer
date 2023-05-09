package Network.OnBoardClient;
import Main.Serialize;
import Network.AuthToken;
import Network.Exceptions.InvalidAuthException;
import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

import javax.sound.sampled.Port;
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
    private boolean isAuthenticatedUser = false;


    public ClientHandler (String username, String password)  {
        try {

            // init components
            socket = PortHandler.generateSocket();
            System.out.println("Connected with port address " + socket.getPort());
            authToken = new AuthToken(username, password);
            signature = PortHandler.getClientSignature();
            serverSocket = new ServerSocket(signature);

            // connecting to server for authentication
            System.out.println("\uD83D\uDD12Sending AUTH request with port address " + signature);
            sendRequest(new RequestToken("AUTH", authToken, signature));
            var authSocket = serverSocket.accept();
            RequestToken token = NetworkUtils.getObject(authSocket);

            // check if the response is good or not
            if (token.exception != null)  {
                if (token.exception instanceof InvalidAuthException) System.out.println("Invalid credentials.");
            } else {
                var response = (AuthToken) token.authentication;
                System.out.println("\uD83D\uDD13Authentication verified.\n\tUser email: " + response.username + "\n\tUser type: " + response.userType);
                socket = new Socket(PortHandler.serverAddress, (int) token.response);
                System.out.println("✅Created a listening socket with port address " + token.response);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error at ClientHandler constructor with error: " + e);
            System.out.println("Error binding to port " + PortHandler.requestPort());
            e.printStackTrace();
        }

    }

    // Binds a session to this client
    public void bindSession (String sessionID){
        authToken.sessionID = sessionID;
    }


    public void sendRequest(RequestToken requestToken) throws IOException{
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