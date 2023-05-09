package Network.OnBoardServer;

import Network.NetworkUtils;
import Network.PortHandler;
import Network.RequestToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;

public class ServerMain {

    public static void main(String[] args)  {
        try {
            while (true) {
                ConcurrentServerHandler serveHandler = new ConcurrentServerHandler();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
