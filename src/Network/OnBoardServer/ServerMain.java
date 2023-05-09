package Network.OnBoardServer;

import Network.RequestToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            ConcurrentServerHandler serveHandler = new ConcurrentServerHandler();
        }
    }
}
