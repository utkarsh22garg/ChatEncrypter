/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Cryptography;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author MNNIT
 */
public class EncoderDecoder {

    public static String encodeMessage(String message, String cryptAlgo, PublicKey publicKey) {
        try {
            String secretKey = "";
            String encryptedKey = "";
            String encryptedMessage = "";
            switch (cryptAlgo) {
                case "AES":
                    secretKey = keyGenerator(cryptAlgo);
                    // encrypting message with the AES secret key
                    message = AES.encrypt(message, secretKey);
                    encryptedMessage = message;
                    // encrypting AES secret key with the RSA public key of receiver
                    encryptedKey = Base64.getEncoder().encodeToString(RSA.encryptWithPublic(publicKey, secretKey));
                    //encryptedKey = RSA.encryptWithPublic(publicKey, secretKey);
                    message += "###";
                    message += encryptedKey;
                    message += "###AES";
                    break;

                case "DES":
                    secretKey = keyGenerator(cryptAlgo);
                    // encrypting message with the AES secret key
                    message = DES.encrypt(message, secretKey);
                    encryptedMessage = message;
                    // encrypting AES secret key with the RSA public key of receiver
                    encryptedKey = Base64.getEncoder().encodeToString(RSA.encryptWithPublic(publicKey, secretKey));
                    //encryptedKey = RSA.encryptWithPublic(publicKey, secretKey);
                    message += "###";
                    message += encryptedKey;
                    message += "###DES";
            }
            String digitalSignature = addSignature(encryptedMessage);
            System.out.println("" + digitalSignature);
            return message + "###" + digitalSignature;
        } catch (Exception ex) {
            Logger.getLogger(EncoderDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String decodeMessage(String message, PublicKey publicKey) {
        String originalMessage = "";
        try {
            String[] encrypted = message.split("###", 4);
            System.out.println("" + encrypted[0] + " " + encrypted[1] + " " + encrypted[2] + " " + encrypted[3]);
            String secretKey = RSA.decryptWithPrivate(encrypted[1]);

            if (encrypted[2].equals("AES")) {
                originalMessage = AES.decrypt(encrypted[0], secretKey);
            } else {
                originalMessage = DES.decrypt(encrypted[0], secretKey);
            }

            if (verifySignature(encrypted[0], encrypted[3], publicKey)) {
                originalMessage += "(verified)";
            } else {
                originalMessage += "(not-verified)";
            }

        } catch (Exception ex) {
            Logger.getLogger(EncoderDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return originalMessage;
    }
    
    public static String decodeFile(String message, PublicKey publicKey) {
        String originalMessage = "";
        try {
            String[] encrypted = message.split("###", 4);
            System.out.println("" + encrypted[0] + " " + encrypted[1] + " " + encrypted[2] + " " + encrypted[3]);
            String secretKey = RSA.decryptWithPrivate(encrypted[1]);

            if (encrypted[2].equals("AES")) {
                originalMessage = AES.decrypt(encrypted[0], secretKey);
            } else {
                originalMessage = DES.decrypt(encrypted[0], secretKey);
            }
        } catch (Exception ex) {
            Logger.getLogger(EncoderDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return originalMessage;
    }

    private static String keyGenerator(String cryptAlgo) {
        String secretKey = "";
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(cryptAlgo);
            if (cryptAlgo.equals("AES")) {
                keyGen.init(128);
            }
            SecretKey key = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algo found during generating key for encoding message");
            Logger.getLogger(EncoderDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return secretKey;
    }

    public static String addSignature(String msg) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(RSA.getPrivateKey());
        rsa.update(msg.getBytes());
        return Base64.getEncoder().encodeToString(rsa.sign());
    }

    private static String getHash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {

        }
        return null;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    private static boolean verifySignature(String encrypted, String signature, PublicKey oppPublicKeyRSA) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(oppPublicKeyRSA);
        sig.update(encrypted.getBytes());

        return sig.verify(Base64.getDecoder().decode(signature));
    }
}
