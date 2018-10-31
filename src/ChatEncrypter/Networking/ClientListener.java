/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author MNNIT
 */
public class ClientListener extends Thread {

    private int port;
    private ServerSocket server;
    private WritableGUI gui;
    private SocketHelper socketHelper;
    private PublicKey receiverPublicKey;

    public ClientListener(WritableGUI gui, int port, SocketHelper socketHelper) {
        this.port = port;
        this.gui = gui;
        this.socketHelper = socketHelper;
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Try using another port", "Error in creating the server socker", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error while creating the Server Socket!!");
            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Socket clientSocket;

        try {
            clientSocket = server.accept();
            System.out.println("Client Connected");
            
            gui.connectClient(clientSocket.getInetAddress().toString(), clientSocket.getPort());
            socketHelper.setClientSocket(clientSocket);
            PublicKey receiverPublicKey = socketHelper.getReceiverPublicKey();
            new MessageListener(clientSocket, gui,receiverPublicKey).start();
        } catch (IOException ex) {
            System.out.println("Can't get any client Socket");
            Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
