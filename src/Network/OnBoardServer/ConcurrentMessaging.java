package Network.OnBoardServer;

import Main.Serialize;
import Network.PortHandler;
import Network.RequestToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentMessaging extends Thread {
    Socket socket;
    ServerSocket serverSocket;
    public ConcurrentMessaging (RequestToken requestToken) throws IOException {
        socket = new Socket(PortHandler.serverAddress, requestToken.signature);
        requestToken.response = PortHandler.getClientSignature();
        System.out.println("Sending AUTH acceptance");
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
