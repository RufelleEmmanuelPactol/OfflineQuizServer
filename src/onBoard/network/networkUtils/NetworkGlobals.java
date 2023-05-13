package onBoard.network.networkUtils;

import onBoard.network.client.ClientHandler;
import onBoard.network.exceptions.InvalidAuthException;

import javax.swing.*;
import java.io.IOException;

public class NetworkGlobals {

    private static ClientHandler clientHandler;
    public static ClientHandler session(){
        return clientHandler;
    }

    public static ClientHandler createSession(String username, String password) throws IOException, ClassNotFoundException, InterruptedException {
        var watch = new Watch(5);
        NetworkGlobals.networkTimer().timeThisWhile(watch, 5);
        if (clientHandler == null) clientHandler = new ClientHandler(username, password);
        if (!clientHandler.isAuthenticatedUser()) {
            clientHandler = null;
            watch.watch = 10;
            throw new InvalidAuthException();
        }
        return clientHandler;
    }

    private static NetworkTimer networkTimer;
    public static NetworkTimer networkTimer() {return new NetworkTimer();}

    public static void showMsg (String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

}
