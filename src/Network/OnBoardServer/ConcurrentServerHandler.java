package Network.OnBoardServer;

import Main.Serialize;
import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ConcurrentServerHandler {
    boolean isDebugMode = false;

    private Set<Socket> socket_set;
    private Socket socket;

    public ConcurrentServerHandler() {
        try (var serverSocket = new ServerSocket(PortHandler.requestPort())){
            while (true) {
                System.out.println("The server is open and is awaiting for connections.");
                socket = serverSocket.accept();
                System.out.println("✅Connected with port address " + serverSocket.getLocalPort());
                var requestToken = (RequestToken) NetworkUtils.getObject(socket);
                System.out.println("There is a request for " + requestToken.requestFor);
                var message = new ConcurrentMessaging(requestToken);
            }
        } catch (Exception e){
            System.out.println("Exception occurred at ServerHandler object: " + e);
        }
    }

    public void setDebugMode (boolean mode)
    {
        this.isDebugMode = mode;
    }

    public RequestToken getRequest()
    {
        return (RequestToken) getRequest();
    }



}
