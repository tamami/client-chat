import manager.MessageManager;
import socket.SocketMessageManager;

/**
 * Created by tamami on 12/05/17.
 */
public class Messenger {

    public static void main(String[] args) {
        MessageManager messageManager;

        if(args.length == 0) {
            messageManager = new SocketMessageManager("localhost");
        } else {
            messageManager = new SocketMessageManager(args[0]);
        }

        ClientGUI clientGui = new ClientGUI(messageManager);
        clientGui.setSize(300,400);
        clientGui.setResizable(false);
        clientGui.setVisible(true);
    }

}
