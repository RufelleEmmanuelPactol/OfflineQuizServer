package onBoard.network.client;
import onBoard.dataClasses.Result;
import onBoard.dataClasses.User;
import onBoard.network.exceptions.CannotReattemptQuizAgain;
import onBoard.network.exceptions.TimeSubmissionElapsedException;
import onBoard.network.networkUtils.*;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.utils.DateBuilder;
import onBoard.quizUtilities.Quiz;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/*=========================================
  =========================================*/
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
            if (token.exception instanceof ConnectException) {
                throw new InterruptedException();
            }

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
                getUserInfo();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Error at ClientHandler constructor with error: " + e);
            System.out.println("Error binding to port " + PortHandler.requestPort());
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }


    }

    /*=========================================
    Gets the user information from the server.
    Returns a User() class.
    =========================================*/

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


    /*=========================================
    Requests the server to terminate the socket.
    This clears up the resources and allocates
    them for other clients.
    =========================================*/
    public void closeSession () throws IOException {
        serverSocket.close();
        receiveSocket.close();
        NetworkUtils.sendRequest(new RequestToken("CLOSE" ,authToken, signature), sendSocket);
    }

    /*=========================================
    Posts a quiz to the database via the server.
    Requirements: currentClass must be initialized.
    To initialize, go to NetworkGlobals.currentClass
    This is for the teacher only.
    =========================================*/

    public void postQuiz(Quiz q) throws IOException, ClassNotFoundException, InterruptedException {
        RequestToken tkn = new RequestToken();
        tkn.requestFor="POSTQUIZ";
        tkn.authentication = NetworkGlobals.currentClass;
        tkn.response = q;
        NetworkUtils.sendRequest(tkn, sendSocket);
        RequestToken response = NetworkUtils.getObject(receiveSocket);
        if (response.exception == null )return;
        Exception e = (Exception) response.exception;
        NetworkGlobals.showMsg(e.toString());
        e.printStackTrace();
    }

    public void postAttempt(Quiz quiz, DateBuilder timeStarted) throws IOException, ClassNotFoundException, InterruptedException {
        Result result = new Result();
        result.quizID = quiz.quizID;
        if (quiz.getTimeClose().isElapsed()) throw new TimeSubmissionElapsedException();
        result.endTime = NetworkGlobals.getTimeNow();
        result.quizBlob = quiz;
        result.startTime = timeStarted;
        result.studentID = NetworkGlobals.session().getUser().userId;
        RequestToken requestToken = new RequestToken();
        requestToken.authentication = result;
        requestToken.requestFor = "POSTATTEMPT";
        NetworkUtils.sendRequest(requestToken, sendSocket);
        var attempt = (RequestToken)NetworkUtils.getObject(receiveSocket);
        if (attempt.exception instanceof CannotReattemptQuizAgain r) throw r;
    }

    public Quiz getQuiz (int id) throws Exception {
        RequestToken tkn = new RequestToken();
        tkn.requestFor = "GETQUIZ";
        tkn.authentication = id;
        NetworkUtils.sendRequest(tkn, sendSocket);
        var quiz = (RequestToken)NetworkUtils.getObject(receiveSocket);
        if (quiz.exception !=null) throw ((RuntimeException)quiz.exception);
        return (Quiz)quiz.response;
    }

    public Result getAttempt(int quizID) throws IOException, ClassNotFoundException, InterruptedException {
        RequestToken tkn = new RequestToken();
        tkn.signature = quizID;
        tkn.authentication = user.userId;
        tkn.requestFor = "GETATTEMPT";
        NetworkUtils.sendRequest(tkn, sendSocket);
        var request = ((RequestToken)NetworkUtils.getObject(receiveSocket));
        if (request.exception != null) throw (RuntimeException)request.exception;
        return (Result)request.response;
    }










}