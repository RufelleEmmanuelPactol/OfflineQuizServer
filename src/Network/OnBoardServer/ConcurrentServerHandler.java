package Network.OnBoardServer;

import Network.RequestToken;

import java.net.Socket;
import java.util.Set;

public class ConcurrentServerHandler {
    boolean isDebugMode = false;

    Set<Socket> socket_set;

    public void setDebugMode (boolean mode)
    {
        this.isDebugMode = mode;
    }

    public RequestToken getRequest()
    {
        return (RequestToken) getRequest();
    }

    private <T> T getObject(){
        return null;
    }

}
