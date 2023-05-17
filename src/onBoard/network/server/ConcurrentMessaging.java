package onBoard.network.server;

import onBoard.connectivity.SQLConnector;
import onBoard.connectivity.Serialize;
import onBoard.dataClasses.User;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.exceptions.InvalidRequestForHeader;
import onBoard.network.networkUtils.*;
import onBoard.quizUtilities.Quiz;

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


    public ConcurrentMessaging (RequestToken requestToken)  {
        this.requestToken = requestToken;

        // special use case for lucky
        if (requestToken.requestFor.equals("AUTHLUCKY")) NetworkGlobals.luckyMode = true;
        else System.out.println(requestToken.requestFor);
    }

    public void close (boolean close) {this.close = close;}


    public void verifyUser() throws IOException, SQLException {


        sendingSocket = new Socket(PortHandler.serverAddress, requestToken.signature);
        // check for SQL Database Connectivity
        SQLConnector.checkConnection();

           System.out.println("\uD83D\uDD12Sending AUTH acceptance with room signature " + (requestToken.signature));
            NetworkUtils.sqlconnector().verifyUser(requestToken);
        if(requestToken.exception == null) {
            System.out.println("\uD83D\uDD13Server Verified Account with username: " + ((AuthToken) requestToken.authentication).email);
            System.out.println("Created a new ServerSocket room with port " + (int) requestToken.response);
            roomSocket = new ServerSocket((int) requestToken.response);
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
        receivingSocket = roomSocket.accept();
        System.out.println("Accepted client's socket with room " + roomSocket.getLocalPort());
        while (sendingSocket.isConnected() && !close) {
            RequestToken tkn = NetworkUtils.getObject(receivingSocket);
            if (tkn.requestFor.equals("USERINFO")) {
                System.out.println("There is a USERINFO request");
                User user = NetworkUtils.sqlconnector().getUserData(tkn);
                if (user == null) {
                    tkn.exception = new InvalidAuthException();
                }
                tkn.response = user;
                System.out.println("While handling requests, we sent a message to port " + sendingSocket.getPort());
                NetworkUtils.sendRequest(tkn, sendingSocket);
            } else if (tkn.requestFor.equals("CLOSE")) {
                break;
            } else if (tkn.requestFor.equals("POSTQUIZ")){
                var quiz = (Quiz)tkn.response;
                System.out.println("There is a POST QUIZ request.");
                NetworkUtils.sqlconnector().postQuiz(quiz, (User)tkn.authentication); // implement quiz
            } else if (tkn.requestFor.equals("GETQUIZZES")){

            } else if (tkn.requestFor.equals("ADDCLASS")){

            } else if (tkn.requestFor.equals("POSTATTEMPT")){

            }
            else {
                System.out.println("Invalid request made with requestFor header: " + tkn.requestFor);
                tkn.exception = new InvalidRequestForHeader();
            }
        }  if (sendingSocket.isClosed()) sendingSocket.close();
        System.out.println("Socket " + receivingSocket.getLocalPort() + " has been closed.");


    }

}
