package onBoard.network.server;

import onBoard.connectivity.SQLConnector;
import onBoard.network.networkUtils.NetworkGlobals;

import java.sql.SQLException;

public class ServerMain {

    public static void main(String[] args)  {
        onboardASCII();
        try {
            while (true) {
                ConcurrentServerHandler serveHandler = new ConcurrentServerHandler();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void onboardASCII(){
        var onboard = "    .-'''-.                           .-'''-.                                \n" +
                "   '   _    \\                        '   _    \\                _______       \n" +
                " /   /` '.   \\    _..._   /|       /   /` '.   \\               \\  ___ `'.    \n" +
                ".   |     \\  '  .'     '. ||      .   |     \\  '                ' |--.\\  \\   \n" +
                "|   '      |  '.   .-.   .||      |   '      |  '       .-,.--. | |    \\  '  \n" +
                "\\    \\     / / |  '   '  |||  __  \\    \\     / /  __    |  .-. || |     |  ' \n" +
                " `.   ` ..' /  |  |   |  |||/'__ '.`.   ` ..' /.:--.'.  | |  | || |     |  | \n" +
                "    '-...-'`   |  |   |  ||:/`  '. '  '-...-'`/ |   \\ | | |  | || |     ' .' \n" +
                "               |  |   |  |||     | |          `\" __ | | | |  '- | |___.' /'  \n" +
                "               |  |   |  |||\\    / '           .'.''| | | |    /_______.'/   \n" +
                "               |  |   |  ||/\\'..' /           / /   | |_| |    \\_______|/    \n" +
                "               |  |   |  |'  `'-'`            \\ \\._,\\ '/|_|                  \n" +
                "               '--'   '--'                     `--'  `\"                      ";
        System.out.println(onboard);
        var server = "                            \n" +
                "                   ___  ___ _ ____   _____ _ __ \n" +
                "                   / __|/ _ \\ '__\\ \\ / / _ \\ '__|\n" +
                "                   \\__ \\  __/ |   \\ V /  __/ |   \n" +
                "                   |___/\\___|_|    \\_/ \\___|_|   \n" +
                "                                               \n" +
                "                              ";
        System.out.println(server);
        System.out.println("v.0.2.1");
    }
}
