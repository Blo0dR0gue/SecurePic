package com.dhbw.secure_pic.crypter;

import com.dhbw.secure_pic.auxiliary.exceptions.CrypterException;
import com.dhbw.secure_pic.data.Information;
import com.dhbw.secure_pic.pipelines.utility.ProgressMonitor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

// FIXME comment
// TODO do more work self instead of handing it over to library?

/**
 * This class implements the RSA encryption method used to encrypt/decrypt messages.<br>
 * It extends the Crypter class.
 *
 * @author Kirolis Eskondis
 */
public class RSA extends Crypter {

    // region attributes
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String algorithm;
    // endregion

    /** Enum representing the available key types. */
    enum keyType {
        PUBLIC,
        PRIVATE
    }

    /**
     * This constructor is used for generating the KeyPair.
     * The receiver uses this to create private and public key
     */
    public RSA() throws CrypterException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
            this.algorithm = "RSA";
        } catch (NoSuchAlgorithmException e) {
            throw CrypterException.handleException(e);  // wrap exceptions thrown by crypter to CrypterException
        }
    }

    /**
     * This constructor is used for RSA Encryption as it is used by the sender
     *
     * @param password is the only key needed for encryption so privateKey is set to NULL
     */
    public RSA(String password, keyType type) throws NoSuchAlgorithmException, InvalidKeySpecException, CrypterException, IOException {
        this.algorithm = "RSA";
        switch (type){
            case PUBLIC -> {
                this.privateKey = null;
                this.publicKey = (PublicKey) getKeyFromString(password, type);
            }
            case PRIVATE -> {
                this.privateKey = (PrivateKey) getKeyFromString(password, type);;
                this.publicKey = null;
            }
            default -> {
                this.privateKey = null;
                this.publicKey = null;
            }
        }

    }

    /**
     * @param information contains the message to encrypt
     *
     * @return overwritten {@link Information}
     *
     * @throws CrypterException
     */
    @Override
    public Information encrypt(Information information, ProgressMonitor monitor) throws CrypterException {
        try {
            Cipher encryptCipher = Cipher.getInstance(algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            monitor.updateProgress(50);

            byte[] encryptedBytes = encryptCipher.doFinal(information.getData());
            byte[] outPutBytes = Base64.getEncoder().encode(encryptedBytes);
            information.setEncryptedData(outPutBytes);

            monitor.updateProgress(100);

            return information;

        }  catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            throw CrypterException.handleException(e);    // wrap exceptions thrown by crypter to CrypterException
        }
    }

    /**
     * @param information contains the message to decrypt
     *
     * @return overwritten {@link Information}
     *
     * @throws CrypterException
     */
    @Override
    public Information decrypt(Information information, ProgressMonitor monitor) throws CrypterException {
        try {
            Cipher decryptionCipher = Cipher.getInstance(algorithm);
            decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey);

            monitor.updateProgress(50);

            byte[] decryptedBytes = decryptionCipher.doFinal(Base64.getDecoder().decode(information.toText()));
            information.setEncryptedData(decryptedBytes);

            monitor.updateProgress(100);

            return information;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            throw CrypterException.handleException(e);  // wrap exceptions thrown by crypter to CrypterException
        }
    }

    private Key getKeyFromString(String password, keyType type) throws NoSuchAlgorithmException, InvalidKeySpecException, CrypterException {

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Key key = null;
        int i = password.lastIndexOf("/");

        BigInteger m =  new BigInteger(password.substring(0,i));
        BigInteger e =  new BigInteger(password.substring(i+1));
        if(type.equals(keyType.PRIVATE)) {
            key = keyFactory.generatePrivate(new RSAPrivateKeySpec(m, e));
        } else{
            key = keyFactory.generatePublic(new RSAPublicKeySpec(m, e));
        }
        return key;
    }

    public String getPublicKeyAsString() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey,RSAPublicKeySpec.class);
        String keyString = publicKeySpec.getModulus().toString() + "/" + publicKeySpec.getPublicExponent().toString();

        return keyString;
    }

    public String getPrivateKeyAsString() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey,RSAPrivateCrtKeySpec.class);
        String keyString = privateKeySpec.getModulus().toString() + "/" + privateKeySpec.getPrivateExponent().toString();

        return keyString;
    }

    //FIXME check if still needed

    // region getter
    // Getters are used to output the keys to the user
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    // endregion
}
