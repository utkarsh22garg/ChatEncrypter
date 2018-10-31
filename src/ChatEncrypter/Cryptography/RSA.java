/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Cryptography;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author MNNIT
 */
public class RSA {

    private KeyPair keypair;
    private String privateKeyString, publicKeyString;
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public RSA() {
        buildKeyPair();
        getKeys();
    }

    private void buildKeyPair() {
        try {
            final int keySize = 2048;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            keypair = keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No RSA algorithm is found in the system");
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public KeyPair getRSAKeyPair() {
        return keypair;
    }

    private void getKeys() {
        publicKey = keypair.getPublic();
        privateKey = keypair.getPrivate();
        publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("Public Key: "+publicKeyString+" Private Key: "+privateKeyString);
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public String getPrivateKeyString() {
        return privateKeyString;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }
    
    public static byte[] encryptWithPublic(PublicKey publicKey, String message){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            try {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                return cipher.doFinal(message.getBytes());
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String decryptWithPrivate(String secretKey) throws Exception {
        byte[] encrypted=Base64.getDecoder().decode(secretKey);
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(encrypted));
    }

    public static byte[] encryptWithPrivate(String message){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            try {
                cipher.init(Cipher.ENCRYPT_MODE, RSA.privateKey);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                return cipher.doFinal(Base64.getDecoder().decode(message));
            } catch (IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String decryptWithPublic(String secretKey,PublicKey publicKey) throws Exception {
        byte[] encrypted=Base64.getDecoder().decode(secretKey);
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(encrypted));
    }
    
}
