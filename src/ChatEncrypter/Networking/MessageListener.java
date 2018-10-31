/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

import ChatEncrypter.Cryptography.EncoderDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import javax.swing.JOptionPane;

/**
 *
 * @author MNNIT
 */
public class MessageListener extends Thread {

    private Socket s;
    private WritableGUI gui;
    private PublicKey receiverPublicKey;
    private byte[] buffer;

    public MessageListener(Socket s, WritableGUI gui, PublicKey receiverPublicKey) {
        this.s = s;
        this.gui = gui;
        this.receiverPublicKey = receiverPublicKey;
        buffer = new byte[1024 * 50];
    }

    @Override
    public void run() {

        try {
            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            //BufferedReader br =  new BufferedReader(new InputStreamReader(is));
            String message = "aao";
            System.out.println("Listening messages");
            while (!(message = dis.readUTF()).equals("stop")) {
                //System.out.println(message);

                message = EncoderDecoder.decodeMessage(message, receiverPublicKey);

                if (message.equals("Iamsendingafile(verified)")) {

                    String fileName = dis.readUTF();
                    fileName = EncoderDecoder.decodeFile(fileName, receiverPublicKey);
                    
                    String file = dis.readUTF();
                    file = EncoderDecoder.decodeFile(file, receiverPublicKey);
                    
                    Files.write(Paths.get(System.getProperty("user.dir")+"/"+fileName),file.getBytes());

                    gui.write("****File is received****");
                } else {
                    gui.write("Received:    " + message);
                }

            }

            s.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Receiver Left", "Connect to another!!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            //Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
