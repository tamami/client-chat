package manager;

import socket.MessageListener;

/**
 * Created by tamami on 11/05/17.
 */
public interface MessageManager {

    public void connect(MessageListener listener);
    public void disconnect(MessageListener listener);
    public void sendMessage(String from, String message);

}
