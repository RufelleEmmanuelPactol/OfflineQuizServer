package Network.OnBoardServer;

import Main.Serialize;
import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

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
            System.out.println("✅Connected with port address " + serverSocket.getLocalPort());
            while (true) {
                socket = serverSocket.accept();
                var requestToken = (RequestToken) NetworkUtils.getObject(socket);
                System.out.println("There is a request for " + requestToken.requestFor);
                var message = new ConcurrentMessaging(requestToken);
            }
        } catch (IOException e){
            System.out.println("An exception occurred in the ConcurrentServerHandler constructor with error: " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            // ignored
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
