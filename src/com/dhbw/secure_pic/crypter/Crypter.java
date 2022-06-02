package com.dhbw.secure_pic.crypter;

import com.dhbw.secure_pic.data.Information;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// TODO COMMENT
// TODO implement

// TODO reduce number of specific exceptions? more general?

/**
 * This class implements the Crypter used to encrypt/decrypt messages.<br>
 * Class is abstract, hence its functionality could only be used through its children.
 *
 * @author Kirolis Eskondis supported by Frederik Wolter
 */
public abstract class Crypter {

    /**
     * @param information includes the message to encrypt/decrypt
     */
    public abstract Information encrypt(Information information) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    public abstract Information decrypt(Information information) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

}
