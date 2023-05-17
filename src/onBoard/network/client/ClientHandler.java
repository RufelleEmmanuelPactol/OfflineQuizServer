package onBoard.network.client;
import onBoard.dataClasses.User;
import onBoard.network.networkUtils.*;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.quizUtilities.Quiz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {
    protected ServerSocket serverSocket;
    protected Socket socket;
    protected User user = null;

    public User getUser() {
        return user;
    }

    public void closeConnections() throws IOException {
        if (socket != null) if (!socket.isClosed()) socket.close();
        if (serverSocket != null) if (!serverSocket.isClosed()) serverSocket.close();
        if (receiveSocket != null) if (receiveSocket.isBound()) receiveSocket.close();
        if (sendSocket != null) if (sendSocket.isBound()) sendSocket.close();
    }

    // The signature determines what port will this client
    // bind to and listen for requests
    protected int signature; // the serverSocket's signature pointing to this user
    protected AuthToken authToken;
    protected Socket sendSocket; //  the room socket where the server is listening for a private connection
    // this is where you will be sending your data
    protected Socket receiveSocket; // the socket where the server sends data to
    // this is where you will be receiving your data
    private boolean isAuthenticatedUser = false;

    public boolean isAuthenticatedUser() {
        return isAuthenticatedUser;
    }

    public ClientHandler(String username, String password) throws IOException, ClassNotFoundException, InterruptedException {
        try {

            // init components
            socket = PortHandler.generateSocket();
            System.out.println("Connected with port address " + socket.getPort());
            authToken = new AuthToken(username, password);
            signature = PortHandler.getClientSignature();
            System.out.println("Attempting to create a server socket using the signature with port: " + signature);
            serverSocket = new ServerSocket(signature);

            // connecting to server for authentication

            if (NetworkGlobals.luckyMode) NetworkUtils.sendRequest(new RequestToken("AUTHLUCKY", authToken, signature), socket, signature);
            else NetworkUtils.sendRequest(new RequestToken("AUTH", authToken, signature), socket, signature);


            receiveSocket = serverSocket.accept();
            RequestToken token = NetworkUtils.getObject(receiveSocket);

            // check if the response is good or not
            if (token.exception != null) {
                var auth = (AuthToken) token.authentication;
                System.out.println("Invalids: " + auth.email + " " + auth.password);
                if (token.exception instanceof InvalidAuthException)
                    System.out.println("⚠️An exception occurred with error: " + token.exception);
            } else {
                var response = (AuthToken) token.authentication;
                this.isAuthenticatedUser = true;
                System.out.println("\uD83D\uDD13Authentication verified.\n\tUser email: " + response.email + "\n\tUser type: " + response.userType);
                sendSocket = new Socket(PortHandler.serverAddress, (int) token.response);
                System.out.println("✅Created a room socket (from server's room) with port address " + token.response);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error at ClientHandler constructor with error: " + e);
            System.out.println("Error binding to port " + PortHandler.requestPort());
            throw e;
        }


    }

    // Binds a session to this client
    public void bindSession(String sessionID) {
        authToken.sessionID = sessionID;
    }



    /*
    Gets user info.
     */
    public User getUserInfo() throws Throwable {
        RequestToken requestToken = new RequestToken();
        requestToken.response = user;
        if (user == null) {
            System.out.println("User was null at one instance");
            requestToken = new RequestToken("USERINFO", authToken, signature);
            NetworkUtils.sendRequest(requestToken, sendSocket, signature);
            this.user = (User) ((RequestToken) NetworkUtils.getObject(receiveSocket)).response;
            requestToken.response = this.user;
            if (requestToken.exception != null) {
                throw (Throwable) requestToken.exception;
            }

        }  return user;
    }


    public void closeSession () throws IOException {

        NetworkUtils.sendRequest(new RequestToken("CLOSE" ,authToken, signature), sendSocket);
    }

    public void postQuiz(Quiz q){
        RequestToken tkn = new RequestToken();
        tkn.requestFor="POST";
        tkn.authentication = authToken;
    }









}