
import ChatEncrypter.Cryptography.RSA;
import ChatEncrypter.Networking.KeyTransmitter;
import ChatEncrypter.Networking.MessageListener;
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
public class ClientMain extends ChatGUI {

    private String serverIp, username;
    private String clientIp;
    private int clientPort, serverPort;

    private Inet4Address inet4Address;
    private RSA rsaObj;
    private Socket socket;
    private PublicKey receiverPublicKey;

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public ClientMain(String serverIp, String username, int serverPort) throws IOException {

        super();
        this.serverIp = serverIp;
        this.username = username;
        this.serverPort = serverPort;

        rsaObj = new RSA();

        try {
            inet4Address = (Inet4Address) Inet4Address.getLocalHost();
            this.clientIp = inet4Address.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        assignServerToClient(serverIp, serverPort);

    }

    public final void assignServerToClient(String serverIp, int serverPort) throws IOException {
        socket = new Socket(serverIp, serverPort);

        clientPort = socket.getLocalPort();

        super.showClientInfo(clientIp, clientPort);
        super.setSendSocket(socket);
        super.showHostInfo(serverIp, serverPort);
        super.connectClient(clientIp, clientPort);

        KeyTransmitter keyTransmitter = new KeyTransmitter(socket, rsaObj.getPublicKey());

        //client is first receiving server's public key
        receiverPublicKey = keyTransmitter.receivePublicKey();
        System.out.println(""+Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded()));
        
        //client is then transmitting its' public key
        keyTransmitter.transmitPublicKey();

        setSendPublicKey(receiverPublicKey);
        new MessageListener(socket, this,receiverPublicKey).start();
    }

}
