package com.tsbot.credentials;


/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class Username {


    private String username;

    /**
     * Default constructor for {@link com.tsbot.credentials.Username}.
     *
     * @param username the username in question.
     */
    public Username(String username) {
        this.username = username;
    }


    /**
     * Explicitly editing the in-built method toString().
     *
     * @return the username.
     */
    @Override
    public String toString() {
        return username;
    }
}
