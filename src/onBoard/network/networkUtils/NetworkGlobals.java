package onBoard.network.networkUtils;

import onBoard.dataClasses.ClassData;
import onBoard.dataClasses.User;
import onBoard.network.client.ClientHandler;
import onBoard.network.exceptions.InvalidAuthException;
import onBoard.network.utils.DateBuilder;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;
import java.time.LocalDateTime;
import java.util.Locale;

public class NetworkGlobals {

    private static ClientHandler clientHandler;
    public static void endSession () throws IOException {
        clientHandler.closeSession();
        clientHandler = null;
    }
    public static ClientHandler session(){
        return clientHandler;
    }

    public static ClientHandler createSession(String username, String password) throws Throwable {
        if (clientHandler == null) clientHandler = new ClientHandler(username, password);
        if (!clientHandler.isAuthenticatedUser()) {
            clientHandler = null;
            throw new InvalidAuthException();
        }
        clientHandler.getUserInfo();
        return clientHandler;
    }

    public static void mthsetnil(){
        clientHandler = null;
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

    /*================================
    Retrieves current time and returns
    a DateBuilder object with filled-up
    current date time.
    ==================================*/

    public static DateBuilder getTimeNow(){
        var time = LocalDateTime.now();
        DateBuilder builder = new DateBuilder().setYear(time.getYear()).setMonth(time.getMonthValue())
                .setDay(time.getDayOfMonth()).setHour(time.getHour()).setMinute(time.getMinute());
        return builder;
    }

    public static void connectionOfficer(){

    }

    public static User getCurrentUser(){
        return session().getUser();
    }

    public static int t = 0;
}
