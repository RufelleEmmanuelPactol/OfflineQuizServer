package onBoard.network.networkUtils;

import java.io.Serializable;

public class AuthToken implements Serializable {

    /*
    The AuthToken class is a serializable class sent over a onBoard.network to handle user
    authentications. The AuthToken class is sent by the client and is verified by
    the server using the ServeHandler class.
     */
    public String email;
    public String password;
    public String sessionID;
    public String userType;

    public AuthToken(String username, String password, String sessionID){
        this.email = username;
        this.password = password;
        this.sessionID = sessionID;
    }

    public AuthToken (String username, String password){
        this.email = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
