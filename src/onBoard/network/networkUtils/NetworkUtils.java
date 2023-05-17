package onBoard.network.networkUtils;

import onBoard.connectivity.SQLConnector;
import onBoard.connectivity.Serialize;
import onBoard.network.client.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NetworkUtils {

    public static <T> T getObject(Socket socket) throws IOException, ClassNotFoundException, InterruptedException {
           // System.out.println("Accepting byte stream listening from port " + socket.getLocalPort());
            var inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            ArrayList<Byte> byteStreamBuffer = new ArrayList<>();

            // sleep to add time buffer to send bytes
            waitForBufferInstance(inputStream);

            while (inputStream.available() != 0) byteStreamBuffer.add(inputStream.readByte());
            byte[] objectByte = new byte[byteStreamBuffer.size()];
            for (int i = 0; i < byteStreamBuffer.size(); i++)
                objectByte[i] = byteStreamBuffer.get(i);
            System.out.println("Attempting to construct an object from port " + socket.getLocalPort());
            var x = Serialize.constructFromBlob(objectByte);
            System.out.println("✅Succeeded with constructing an object from port " + socket.getLocalPort());
            return (T) x;

    }


    public static void waitForBufferInstance (DataInputStream stream) {
        try {
            TimeUnit.MILLISECONDS.sleep(250);
            while (stream.available() == 0) {
                // delay
            }
        } catch (EOFException e){
            waitForBufferInstance(stream);
        } catch (IOException e) {
            System.out.println("Encountered exception at waiting for buffer instance.");
        } catch (Exception e) {
            System.out.println("Encountered time exception while waiting for buffer instance.");
        }
    }
    public static void waitForBufferInstance (InputStream inputStream) {
        var stream = new DataInputStream(new BufferedInputStream(inputStream));
        try {
            TimeUnit.MICROSECONDS.sleep(100);
            while (stream.available() == 0) {
                // delay
            }
        } catch (EOFException e){
            waitForBufferInstance(stream);
        } catch (IOException e) {
            System.out.println("Encountered exception at waiting for buffer instance.");
        } catch (Exception e) {
            System.out.println("Encountered time exception while waiting for buffer instance.");
        }
    }

    static SQLConnector connector;
    public static SQLConnector sqlconnector() {
        if (connector == null) {
            connector = new SQLConnector();
        } return  connector;

    }

    public static void sendRequest(RequestToken requestToken, Socket socket, int signature) throws IOException{
        requestToken.signature = signature;
        try {
            socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
        } catch (IOException e){
            System.out.println("An exception occurred with the sendRequest method with error: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public static void sendRequest(RequestToken requestToken, Socket socket) throws IOException{

        try {
            socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
        } catch (IOException e){
            System.out.println("An exception occurred with the sendRequest method with error: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public static void sendGeneralToken(Object requestToken, Socket socket) throws IOException{

        try {
            socket.getOutputStream().write(Serialize.writeToBytes(requestToken));
        } catch (IOException e){
            System.out.println("An exception occurred with the sendRequest method with error: " + e);
            e.printStackTrace();
            throw e;
        }
    }

}


