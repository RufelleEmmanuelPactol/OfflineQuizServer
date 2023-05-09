package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PortHandler {
    /*
    Points to the last occupied server port.
     */
    private static int currentPort = 9016;

    public static String serverAddress = "localhost";
    /*
    Gets the signature from here
     */
    private static int clientPort = 10500;

    public static int getCurrentPort() {
        return clientPort;
    }

    public static void setCurrentPort(int currentPort) {
        PortHandler.currentPort = currentPort;
    }

    /*
    A port requesting service for the server.
     */
    public static int requestPort()
    {
        try (ServerSocket testSocket = new ServerSocket(currentPort)) {
        } catch (IOException ioexcept) {
            currentPort++;
            return requestPort();
        } return currentPort;
    }

    /*
    Forms a new signature.
     */
    public static int getClientSignature(){
        try (ServerSocket testSocket = new ServerSocket(clientPort)) {
        } catch (IOException ioexcept) {
            clientPort++;
            return requestPort();
        } return clientPort;
    }

    public static Socket generateSocket() throws IOException {
        try (ServerSocket testSocket = new ServerSocket(currentPort)) {
        } catch (IOException ioexcept) {
            currentPort++;
            return generateSocket();
        } return new Socket(serverAddress, --currentPort);
    }





}
