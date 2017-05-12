package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tamami on 12/05/17.
 */
public class SendingThread extends Thread {

    private Socket clientSocket;
    private String messageToSend;

    public SendingThread(Socket socket, String userName, String message) {
        super("SendingThread: " + socket);
        clientSocket = socket;
        messageToSend = userName + SocketMessengerConstants.MESSAGE_SEPARATOR + message;
    }

    public void run() {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(messageToSend);
            writer.flush();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
