import manager.MessageManager;
import socket.MessageListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by tamami on 12/05/17.
 */
public class ClientGUI extends JFrame {

    private JMenu serverMenu;
    private JTextArea messageArea;
    private JTextArea inputArea;
    private JButton connectButton;
    private JMenuItem connectMenuItem;
    private JButton disconnectButton;
    private JMenuItem disconnectMenuItem;
    private JButton sendButton;
    private JLabel statusBar;
    private String username;
    private MessageManager messageManager;
    private MessageListener messageListener;

    public ClientGUI(MessageManager manager) {
        super("Messenger");

        messageManager = manager;
        messageListener = new MyMessageListener();

        serverMenu = new JMenu("Server");
        serverMenu.setMnemonic('S');
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(serverMenu);
        setJMenuBar(menuBar);

        Icon connectIcon = new ImageIcon(getClass().getResource(""));

        connectButton = new JButton("Connect", connectIcon);
        connectMenuItem = new JMenuItem("Connect", connectIcon);
        connectMenuItem.setMnemonic('C');

        ActionListener connectListener = new ConnectListener();
        connectButton.addActionListener(connectListener);
        connectMenuItem.addActionListener(connectListener);

        Icon disconnectIcon = new ImageIcon(getClass().getResource(""));

        disconnectButton = new JButton("Disconnect", disconnectIcon);
        disconnectMenuItem = new JMenuItem("Disconnect", disconnectIcon);
        disconnectMenuItem.setMnemonic('D');

        disconnectButton.setEnabled(false);
        disconnectMenuItem.setEnabled(false);

        ActionListener disconnectListener = new DisconnectListener();
        disconnectButton.addActionListener(disconnectListener);
        disconnectMenuItem.addActionListener(disconnectListener);

        serverMenu.add(connectMenuItem);
        serverMenu.add(disconnectMenuItem);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(connectButton);
        buttonPanel.add(disconnectButton);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout(10,10));
        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        inputArea = new JTextArea(4, 20);
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);
        inputArea.setEditable(false);

        Icon sendIcon = new ImageIcon(getClass().getResource(""));

        sendButton = new JButton("Send", sendIcon);
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                messageManager.sendMessage(username, inputArea.getText());
                inputArea.setText("");
            }
        });

        Box box = new Box(BoxLayout.X_AXIS);
        box.add(new JScrollPane(inputArea));
        box.add(sendButton);
        messagePanel.add(box, BorderLayout.SOUTH);

        statusBar = new JLabel("Not Connected");
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

        Container container = getContentPane();
        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(messagePanel, BorderLayout.CENTER);
        container.add(statusBar, BorderLayout.SOUTH);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent event) {
                        messageManager.disconnect(messageListener);
                        System.exit(0);
                    }
                }
        );
    }

    private class DisconnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageManager.disconnect(messageListener);

            sendButton.setEnabled(false);
            disconnectButton.setEnabled(false);
            disconnectMenuItem.setEnabled(false);
            inputArea.setEditable(false);
            connectButton.setEnabled(true);
            connectMenuItem.setEnabled(true);
            statusBar.setText("Tidak Tersambung");
        }
    }

    private class ConnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            messageManager.connect(messageListener);
            username = JOptionPane.showInputDialog(ClientGUI.this, "Isikan user name:");
            messageArea.setText("");

            connectButton.setEnabled(false);
            connectMenuItem.setEnabled(false);
            disconnectButton.setEnabled(true);
            disconnectMenuItem.setEnabled(true);
            sendButton.setEnabled(true);
            inputArea.setEditable(true);
            inputArea.requestFocus();
            statusBar.setText("Tersambung: " + username);
        }
    }

    private class MyMessageListener implements MessageListener {

        @Override
        public void messageReceived(String from, String message) {
            SwingUtilities.invokeLater(new MessageDisplayer(from, message));
        }
    }

    private class MessageDisplayer implements Runnable {
        private String fromUser;
        private String messageBody;

        public MessageDisplayer(String from, String body) {
            fromUser = from;
            messageBody = body;
        }

        @Override
        public void run() {
            messageArea.append("\n" + fromUser + "> " + messageBody);
            messageArea.setCaretPosition(messageArea.getText().length());
        }
    }

}
