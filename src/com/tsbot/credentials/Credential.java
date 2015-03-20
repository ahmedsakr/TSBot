package com.tsbot.credentials;


/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class Credential {


    private String credential;

    /**
     * Default constructor for {@link Credential}.
     *
     * @param credential the credential in question.
     */
    public Credential(String credential) {
        this.credential = credential;
    }


    /**
     * Explicitly editing the in-built method toString().
     *
     * @return the credential.
     */
    @Override
    public String toString() {
        return credential;
    }
}
