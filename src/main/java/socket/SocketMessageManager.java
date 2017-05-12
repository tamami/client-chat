package socket;

import manager.MessageManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by tamami on 11/05/17.
 */
public class SocketMessageManager implements MessageManager {

    private Socket clientSocket;
    private String serverAddress;
    private PacketReceivingThread receivingThread;
    private boolean connected = false;

    public SocketMessageManager(String address) {
        serverAddress = address;
    }


    @Override
    public void connect(MessageListener listener) {
        if(connected) return;

        try {
            clientSocket = new Socket(InetAddress.getByName(serverAddress),
                    SocketMessengerConstants.SERVER_PORT);
            receivingThread = new PacketReceivingThread(listener);
            receivingThread.start();
            connected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(MessageListener listener) {
        if(!connected) return;

        try {
            Thread disconnectThread = new SendingThread(clientSocket, "",
                    SocketMessengerConstants.DISCONNECT_STRING);
            disconnectThread.start();
            disconnectThread.join(10000);
            receivingThread.stopListening();
            clientSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    @Override
    public void sendMessage(String from, String message) {
        if(!connected) return;

        new SendingThread(clientSocket, from, message).start();
    }
}
