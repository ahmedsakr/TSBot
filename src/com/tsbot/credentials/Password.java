package com.tsbot.credentials;


import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class Password {


    private String password;


    /**
     * Default constructor for the {@link com.tsbot.credentials.Password} class.
     * Takes an unencrypted password character array and hashes it using the message digest algorithm.
     * Leaving no trail of the original password - for best security practices.
     *
     * @param password the unencrypted char[] password.
     */
    public Password(char[] password) {
        try {
            hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * hashes the supplied password and saves it into the password attribute.
     *
     * @param password the unencrypted char[] password
     * @throws NoSuchAlgorithmException
     */
    private void hash(char[] password) throws NoSuchAlgorithmException {
        String unhashed = new String(password);
        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(unhashed.getBytes(Charset.forName("UTF-8")));

        this.password = new BigInteger(1, md.digest()).toString(16);
    }


    /**
     * Explicitly overriding the in-built toString() method.
     *
     * @return the encrypted password.
     */
    @Override
    public String toString() {
        return password;
    }
}
