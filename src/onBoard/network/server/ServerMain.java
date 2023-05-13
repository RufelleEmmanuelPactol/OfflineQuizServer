package onBoard.network.server;

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
