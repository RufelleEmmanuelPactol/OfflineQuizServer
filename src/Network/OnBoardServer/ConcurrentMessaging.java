package Network.OnBoardServer;

import Main.SQLConnector;
import Main.Serialize;
import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ConcurrentMessaging extends Thread {
    Socket socket;
    ServerSocket serverSocket;
    public ConcurrentMessaging (RequestToken requestToken) throws IOException, SQLException, ClassNotFoundException {
        socket = new Socket(PortHandler.serverAddress, requestToken.signature);
        System.out.println("\uD83D\uDD12Sending AUTH acceptance with room signature " + requestToken.response);
        NetworkUtils.sqlconnector().verifyUser(requestToken);
        serverSocket = new ServerSocket((int)requestToken.response);
        socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
    }

    @Override
    public void run (){
        try {
            attendToRequests();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void attendToRequests() throws IOException {
        var inputStream = socket.getInputStream();
        var outputStream = socket.getOutputStream();
        ServerSocket sample = new ServerSocket(12);

        while (socket.isConnected()) {

        }
    }

}
