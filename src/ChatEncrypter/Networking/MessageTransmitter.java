/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MNNIT
 */
public class MessageTransmitter {

    private final Socket s;
    private OutputStream os;
    private byte[] buffer;
    private WritableGUI gui;
    //private BufferedWriter bw;

    public MessageTransmitter(Socket s, WritableGUI gui) {
        this.s = s;
        //System.out.println(""+s.getInetAddress()+" "+s.getPort());
        this.gui = gui;
        buffer = new byte[1024 * 50];
        try {
            os = s.getOutputStream();
            //bw = new BufferedWriter(new OutputStreamWriter(os));

        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String message, String original) {
        DataOutputStream dos = new DataOutputStream(os);
        try {
            dos.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Message can't be Send");
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        gui.write("Send:    " + original);
        gui.clear();
    }

    public void sendFile(String message) {
        DataOutputStream dos = new DataOutputStream(os);
        try {
            dos.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Message can't be Send");
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
