package Network;

import java.io.IOException;
import java.net.ServerSocket;

public class PortHandler {
    private static int currentPort = 9016;
    public static String serverAddress = "localhost";
    private static int clientPort = 10500;


    public static int requestPort()
    {
        try (ServerSocket testSocket = new ServerSocket(currentPort)) {
        } catch (IOException ioexcept) {
            currentPort++;
            return requestPort();
        } return currentPort;
    }

    public static int clientGetSignature(){
        return currentPort;
    }

    public static int clientRequestPort(){
        try (ServerSocket s = new ServerSocket(clientPort)){}
        catch (IOException ioException){
            clientPort++;
            return clientRequestPort();

        } return clientPort;
    }



}
