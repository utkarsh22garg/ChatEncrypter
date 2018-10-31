
import ChatEncrypter.Cryptography.RSA;
import ChatEncrypter.Networking.ClientListener;
import ChatEncrypter.Networking.KeyTransmitter;
import ChatEncrypter.Networking.RSAKeyHelper;
import ChatEncrypter.Networking.SocketHelper;
import ChatEncrypter.Networking.WritableGUI;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MNNIT
 */

public class ServerMain extends ChatGUI implements SocketHelper{

    private String hostname;
    private int port;
    private Inet4Address inet4Address;
    private Socket clientSocket;
    private RSA rsaobj;
    private PublicKey receiverPublicKey;

    public ServerMain(int port) {
        super();
        this.port = port;

        rsaobj = new RSA();

        listenClient(this, port);
        try {
            inet4Address = (Inet4Address) Inet4Address.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("Unable to get the local host of the server");
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.showHostInfo(inet4Address.getHostAddress(), port);
    }

    public final void listenClient(WritableGUI gui, int port) {
        new ClientListener(this, port, this).start();
    }

    @Override
    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
        setSendSocket(socket);
        try {
            KeyTransmitter keyTransmitter = new KeyTransmitter(clientSocket, rsaobj.getPublicKey());

            //server is first transmitting it's public key
            keyTransmitter.transmitPublicKey();
            //server is then receving the client's public key
            receiverPublicKey = keyTransmitter.receivePublicKey();
            setSendPublicKey(receiverPublicKey);
            System.out.println(""+Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded()));
            
        } catch (IOException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PublicKey getReceiverPublicKey() {
        
        return receiverPublicKey;
    }

}
