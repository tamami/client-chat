package socket;

import java.net.Socket;

/**
 * Created by tamami on 11/05/17.
 */
public class SocketMessageManager implements MessageListener {

    private Socket clientSocket;
    private String serverAddress;
    private PacketReceivingThread receivingThread;
    private boolean connected = false;


    @Override
    public void messageReceived(String from, String message) {

    }
}
