package onBoard.network.server;

import onBoard.network.networkUtils.NetworkUtils;
import onBoard.network.networkUtils.PortHandler;
import onBoard.network.networkUtils.RequestToken;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class ConcurrentServerHandler {
    boolean isDebugMode = false;

    private Set<Socket> socket_set;
    private Socket socket;

    public ConcurrentServerHandler() {
        var stat = "OnBoard::";
        try (var serverSocket = new ServerSocket(PortHandler.requestPort())){
            while (true) {
                /*
                The constructor of the ConcurrentServerHandler is blocking
                and continuously accepts new sockets.
                 */
                System.out.println(stat+serverSocket.getLocalPort() + "> The server is open and is awaiting for connections.");
                socket = serverSocket.accept();

                /*
                This area of the constructor asynchronously handles AUTH requests.
                 */

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestToken requestToken = null;
                        try {
                            requestToken = (RequestToken) NetworkUtils.getObject(socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                      //  System.out.println("There is a request for " + requestToken.requestFor);
                        var message = new ConcurrentMessaging(requestToken);
                        message.start();
                    }
                });

                thread.start();

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
