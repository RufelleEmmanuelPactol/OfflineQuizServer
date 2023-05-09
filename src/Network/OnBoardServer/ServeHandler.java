package Network.OnBoardServer;

import Main.Serialize;
import Network.PortHandler;
import Network.RequestToken;

import javax.sound.sampled.Port;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServeHandler extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    // the instance counter for this object
    private static int INSTANCE = 0x01;

    // checks if this ServeHandler instance is attached to a client, and if it is, it spawns a new thread.
    private boolean attachedToClient = false;



    public ServeHandler() throws IOException {
        serverSocket = new ServerSocket(PortHandler.requestPort());
    }

    public ServeHandler(ServerSocket socket){
        this.serverSocket = socket;
    }

    public <T> T acceptRequests() throws IOException, ClassNotFoundException, InterruptedException {
        clientSocket = serverSocket.accept();
        System.out.println("Object Stream: accepted the client socket at socket instance: " + INSTANCE);
        INSTANCE++;
        attachedToClient = true;

        DataInputStream clientData = new DataInputStream(
                new BufferedInputStream(clientSocket.getInputStream()));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Available bytes to read is: " + clientData.available());
        ArrayList<Byte> byteStream = new ArrayList<>();
        byte[] byteStreamPrimitive = null;
        System.out.println("Object Stream: reading the bytes.");
        try
        {
            int initialAvailability = clientData.available();
            while (clientData.available() != 0)
            {
                System.out.printf("Reading the object bytes progress: %.02f%%\n", Math.abs(100*((double)clientData.available()/initialAvailability) - 100));
                byteStream.add(clientData.readByte());
            }

            byteStreamPrimitive = new byte[byteStream.size()];
            for (int i=0; i<byteStream.size(); i++)
            {
                byteStreamPrimitive[i] = byteStream.get(i);
            }




        } catch (EOFException e)
        {

        }
        T object = null;
        System.out.println("Object Stream: returning the bytes as an object.");
        try
        {
            object = Serialize.constructFromBlob(byteStreamPrimitive);
        } catch (EOFException e)
        {
            System.out.println("EOF Exception has occurred.");
            return object;
        }

        return object;


    }

    public void sendOver() throws IOException {
        socket = new Socket(PortHandler.serverAddress, PortHandler.requestPort());
        outputStream = socket.getOutputStream();
    }


    @Override
    public void run(){
        System.out.println("Starting a ServeHandler session with this instance " + INSTANCE);
        try {
            RequestToken token = acceptRequests();
            System.out.println(token);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
