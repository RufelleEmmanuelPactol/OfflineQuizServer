package Network;

import Network.OnBoardClient.ClientHandler;

import java.io.Serializable;

public class AuthToken implements Serializable {

    /*
    The AuthToken class is a serializable class sent over a network to handle user
    authentications. The AuthToken class is sent by the client and is verified by
    the server using the ServeHandler class.
     */
    public String username;
    public String password;
    public String sessionID;
    public String userType;

    public AuthToken(String username, String password, String sessionID){
        this.username = username;
        this.password = password;
        this.sessionID = sessionID;
    }

    public AuthToken (String username, String password){
        this.username = username;
        this.password = password;
    }

}
