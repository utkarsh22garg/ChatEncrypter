/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MNNIT
 */
public class KeyTransmitter {

    private ObjectOutputStream objOs;
    private ObjectInputStream objIn;
    private PublicKey publicKey;
    private Socket socket;
    private PublicKey clientPublicKey;

    public KeyTransmitter(Socket socket, PublicKey senderPublicKey) throws IOException {
        this.socket = socket;
        this.publicKey = senderPublicKey;
        objOs = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());

    }

    public void transmitPublicKey() {
        try {
            objOs.writeObject(publicKey);
            System.out.println("PublicKey sent");
        } catch (IOException ex) {
            System.out.println("Error in sending the PublicKey");
            Logger.getLogger(KeyTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PublicKey receivePublicKey() {
        try {
            clientPublicKey = (PublicKey) objIn.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(KeyTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("PublicKey arrived");
        return clientPublicKey;
    }

}
