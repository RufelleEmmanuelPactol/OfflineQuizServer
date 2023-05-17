package onBoard.network.networkUtils;

import onBoard.dataClasses.ClassData;
import onBoard.network.client.ClientHandler;
import onBoard.network.exceptions.InvalidAuthException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class NetworkGlobals {

    private static ClientHandler clientHandler;
    public static void endSession () throws IOException {
        clientHandler.closeSession();
        clientHandler = null;
    }
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


    /* ===============================

    The cleanup method is a method
    used for cleaning up JFrames and
    making sure that upon the pressing
    of the (x) button, the session ends
    successfully and the port closes.

    ================================ */
    public static void cleanup(JFrame thisFrame){
        thisFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                try {
                    NetworkGlobals.endSession();
                } catch (Exception x) {}
                thisFrame.dispose();
            }
        });
    }

    /*==============================
    Special Mode for Lucky's Credentials
    =============================== */

    public static boolean luckyMode = false;


    /*===============================
    ClassID status determining the
    current class instance.
    ================================ */
    public static ClassData currentClass = null;
}
