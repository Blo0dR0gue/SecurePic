package com.dhbw.secure_pic.crypter;


/**
 *  This class implements the AES encryption method used to encrypt/decrypt messages.
 *  It extends the Crypter class
 */

public class AES extends Crypter{

    private final String pswd;

    /**
     * @param pswd is the password entered by the user
     */
    public AES(String pswd) {
        this.pswd = pswd;
    }

}