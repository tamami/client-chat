package socket;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

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

    }

    @Override
    public void run() {
        while(keepListening) {
            byte[] buffer = new byte[SocketMessengerConstants.MESSAGE_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, SocketMessengerConstants.MESSAGE_SIZE);
            try {
                multicastSocket.receive(packet);
            } catch(InterruptedIOException iioe) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            String message = new String(packet.getData());
            message = message.trim();

            StringTokenizer tokenizer = new StringTokenizer(message,
                    SocketMessengerConstants.MESSAGE_SEPARATOR);

            if(tokenizer.countTokens() == 2) {
                messageListener.messageReceived(tokenizer.nextToken(), tokenizer.nextToken());
            }
        }

        try {
            multicastSocket.leaveGroup(multicastGroup);
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopListening() {
        keepListening = false;
    }
}
