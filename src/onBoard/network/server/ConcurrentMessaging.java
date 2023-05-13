package onBoard.network.server;

import onBoard.connectivity.Serialize;
import onBoard.dataClasses.User;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.exceptions.InvalidRequestForHeader;
import onBoard.network.networkUtils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ConcurrentMessaging extends Thread {
    Socket sendingSocket;
    Socket receivingSocket;
    RequestToken requestToken;
    public ConcurrentMessaging (RequestToken requestToken)  {
        this.requestToken = requestToken;
    }
    ServerSocket roomSocket;


    public void verifyUser() throws IOException {
        sendingSocket = new Socket(PortHandler.serverAddress, requestToken.signature);
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
    public void run (){
        try {
            verifyUser();
            attendToRequests();
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    void attendToRequests() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        receivingSocket = roomSocket.accept();
        System.out.println("Accepted client's socket with room " + roomSocket.getLocalPort());
        while (sendingSocket.isConnected()) {
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
            } else {
                System.out.println("Invalid request made with requestFor header: " + tkn.requestFor);
                tkn.exception = new InvalidRequestForHeader();
            }
        }  if (sendingSocket.isClosed()) sendingSocket.close();

    }

}
