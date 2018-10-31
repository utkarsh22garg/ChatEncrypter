/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

import java.net.Socket;
import java.security.PublicKey;

/**
 *
 * @author MNNIT
 */
public interface RSAKeyHelper {
    
    public void getPublicKey(Socket receiverSocket);
    public void sendPublicKey(Socket receiverSocket,PublicKey publicKey);
    
}
