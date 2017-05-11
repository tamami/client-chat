package socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by tamami on 11/05/17.
 */
public class PacketReceivingThread extends Thread {

    private MessageListener messageListener;
    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private boolean keepListening = true;

    public PacketReceivingThread(MessageListener listener) {
        super("PacketReceivingThread");
        messageListener = listener;

        try {
            multicastSocket = new MulticastSocket(SocketMessengerConstants.MULTICAST_LISTENING_PORT);
            multicastGroup = InetAddress.getByName(
                SocketMessengerConstants.MULTICAST_ADDRESS);
            multicastSocket.joinGroup(multicastGroup);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    @Override
    public void run() {

    }
}
