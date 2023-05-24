package onBoard.network.server;

import onBoard.connectivity.SQLConnector;
import onBoard.connectivity.Serialize;
import onBoard.dataClasses.ClassData;
import onBoard.dataClasses.Result;
import onBoard.dataClasses.User;
import onBoard.network.exceptions.CannotReattemptQuizAgain;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.exceptions.InvalidRequestForHeader;
import onBoard.network.networkUtils.*;
import onBoard.quizUtilities.Quiz;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ConcurrentMessaging extends Thread {
    Socket sendingSocket;
    Socket receivingSocket;
    RequestToken requestToken;
    private boolean close = false;
    ServerSocket roomSocket;
    private static String stat = "OnBoard::";


    public ConcurrentMessaging (RequestToken requestToken)  {
        this.requestToken = requestToken;

        // special use case for lucky
        if (requestToken.requestFor.equals("AUTHLUCKY")) NetworkGlobals.luckyMode = true;
    }

    public void close (boolean close) {this.close = close;}


    public void verifyUser() throws IOException, SQLException {


        sendingSocket = new Socket(PortHandler.serverAddress, requestToken.signature);
        if (!SQLConnector.checkConnection()) {
            requestToken.exception = new ConnectException();
            NetworkUtils.sendRequest(requestToken, sendingSocket);
        }

           System.out.println(stat + sendingSocket.getLocalPort() + "> Sending AUTH acceptance with room signature " + (requestToken.signature));
            NetworkUtils.sqlconnector().verifyUser(requestToken);
        if(requestToken.exception == null) {

            System.out.println(stat + sendingSocket.getLocalPort() +"> Server Verified Account with username: " + ((AuthToken) requestToken.authentication).email);
            roomSocket = new ServerSocket((int) requestToken.response);
            System.out.println(stat + roomSocket.getLocalPort() +"> Created a new ServerSocket room with port " + (int) requestToken.response);
        }
        
        sendingSocket.getOutputStream().write(Serialize.writeToBytes(requestToken));
    }
    @Override
    public void run () {
        try {
            verifyUser();
            attendToRequests();
        }
        catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    void attendToRequests() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        try {
            receivingSocket = roomSocket.accept();
            System.out.println(stat + roomSocket.getLocalPort() + "> Accepted client's socket with room " + roomSocket.getLocalPort());
            while (sendingSocket.isConnected() && !close) {
                RequestToken tkn = NetworkUtils.getObject(receivingSocket);
                if (tkn.requestFor.equals("USERINFO")) {
                    System.out.println(stat + roomSocket.getLocalPort() + "> There is a USERINFO request");
                    User user = NetworkUtils.sqlconnector().getUserData(tkn);
                    if (user == null) {
                        tkn.exception = new InvalidAuthException();
                    }
                    tkn.response = user;
                    System.out.println(stat + roomSocket.getLocalPort() + "> While handling requests, we sent a message to port " + sendingSocket.getPort());
                    NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("CLOSE")) {
                    break;
                } else if (tkn.requestFor.equals("POST QUIZ")) {
                    var quiz = (Quiz) tkn.response;
                    System.out.println(stat + roomSocket.getLocalPort() + "> There is a POST QUIZ request.");
                    try {
                        NetworkUtils.sqlconnector().postQuiz(quiz, (ClassData) tkn.authentication); // implement quiz
                        NetworkUtils.sendRequest(new RequestToken(), sendingSocket);
                    } catch (Exception e) {
                        System.err.println(stat + "> There was an error during posting quiz, reason: " + e.getMessage());
                        var req = new RequestToken();
                        req.exception = e;
                        NetworkUtils.sendRequest(req, sendingSocket);
                    }

                } else if (tkn.requestFor.equals("GET QUIZZES")) {

                } else if (tkn.requestFor.equals("ADD CLASS")) {

                } else if (tkn.requestFor.equals("POST ATTEMPT")) {
                    System.out.println(stat + "> There is a request to POST ATTEMPT");
                    var attempt = (Result)tkn.authentication;
                    attempt.quizBlob.isAttempted = true;
                    try {
                        NetworkUtils.sqlconnector().postAttempt(attempt);
                        NetworkUtils.sendRequest(tkn, sendingSocket);
                    } catch (CannotReattemptQuizAgain e){
                        tkn.exception = e;
                        NetworkUtils.sendRequest(tkn, sendingSocket);

                    }
                } else if (tkn.requestFor.equals("GET QUIZ")) {
                    try {
                        System.out.println(stat + "> THERE is a request to get QUIZ");
                        var id = (int) tkn.authentication;
                        var quiz = NetworkUtils.sqlconnector().getQuiz(id);
                        tkn.response = quiz;
                        NetworkUtils.sendRequest(tkn, sendingSocket);
                    } catch (Exception e){
                        tkn.exception = e;
                        NetworkUtils.sendRequest(tkn, sendingSocket);
                    }
                } else if (tkn.requestFor.equals("GET ATTEMPT")){
                    System.out.println(stat + "> THERE is a request to get GET ATTEMPT");
                    var id = (int)tkn.authentication;
                    var quizID = tkn.signature;
                    var result = NetworkUtils.sqlconnector().getAttempt(quizID, id);
                    tkn.response = result;
                    NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET ONGOING QUIZZES")){
                    System.out.println(stat + "> Fetching ongoing quizzes!");
                    try {
                        var result = NetworkUtils.sqlconnector().getOngoingQuizzes((int)tkn.authentication);
                        tkn.response = result;
                        NetworkUtils.sendRequest(tkn, sendingSocket);
                        System.out.println(stat + "> Finished fetching quizzes");
                    } catch (RuntimeException e){
                        tkn.exception = e;
                        System.out.println(stat + "> Encountered an error during fetch.");
                        tkn.exception = e;
                        NetworkUtils.sendRequest(tkn, sendingSocket);
                    }

                } else if (tkn.requestFor.equals("VALID CODE")){
                    System.out.println(stat + "> Checking if join code is valid");
                    try {
                        tkn.response = NetworkUtils.sqlconnector().isValidClassCode((String) tkn.authentication);
                    } catch (SQLException e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("SEND REQUEST")){
                    System.out.println(stat + "> Sending a request to server.");
                    try {
                        NetworkUtils.sqlconnector().sendRequest( tkn.signature, (String)tkn.authentication);
                        System.out.println(stat + "> Succeeded in sending a request to server.");
                    } catch (Exception e){
                        tkn.exception = e;
                    }
                    NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET REQUESTS")) {
                    System.out.println(stat + "> Retrieving all requests.");
                    try {
                        var x = NetworkUtils.sqlconnector().getAllRequests((int) tkn.authentication);
                        tkn.response = x;
                    } catch (Exception e) {
                        tkn.exception = e;
                    }
                    NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("APPROVE REQUEST")){
                    try {
                        NetworkUtils.sqlconnector().ApproveRequests((int)tkn.authentication, tkn.signature);
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET USER CLASSES")){
                    try {
                        var respo =NetworkUtils.sqlconnector().getUserClasses((int)tkn.authentication);
                        tkn.response = respo;
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET PROCTOR CLASSES")){
                    try {
                        var respo =NetworkUtils.sqlconnector().getProctorClasses((int)tkn.authentication);
                        tkn.response = respo;
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET QUIZZES PER CLASS")){
                    try {
                        var respo =NetworkUtils.sqlconnector().getQuizzesPerClass((int)tkn.authentication);
                        tkn.response = respo;
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("UPDATE QUIZ")){
                    try {
                        NetworkUtils.sqlconnector().updateQuiz((Quiz)tkn.authentication);
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("GET ALL ATTEMPTS")){
                    try {
                        var req = NetworkUtils.sqlconnector().getAllAttempts((int)tkn.authentication);
                        tkn.response = req;
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("CREATE CLASS")){
                    try {
                        NetworkUtils.sqlconnector().createClass((ClassData)tkn.authentication);
                    } catch (Exception e){
                        tkn.exception = e;
                    } NetworkUtils.sendRequest(tkn, sendingSocket);
                } else if (tkn.requestFor.equals("CREATE USER")){
                    try {
                        NetworkUtils.sqlconnector().createUser((User) tkn.authentication, (String) tkn.response);
                    } catch (Exception e){
                        tkn.exception = e;
                    }NetworkUtils.sendRequest(tkn, sendingSocket);
                }
                else {
                    System.out.println(stat + roomSocket.getLocalPort() + "> Invalid request made with requestFor header: " + tkn.requestFor);
                    tkn.exception = new InvalidRequestForHeader();
                    NetworkUtils.sendRequest(tkn, sendingSocket);
                }
            }
            sendingSocket.close();
            receivingSocket.close();
            roomSocket.close();
            System.out.println(stat + roomSocket.getLocalPort() + "> Socket " + receivingSocket.getLocalPort() + " has been closed.");
        } catch (EOFException e){
            System.out.println(stat + roomSocket.getLocalPort() + "> Socket " + receivingSocket.getLocalPort() + " has been closed.");
        }

    }

}
