package com.tsbot.gui;

import com.tsbot.credentials.Password;
import com.tsbot.credentials.Username;

/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class BotAccessorOperator {

    private TSBotLogin save;
    private Username username;
    private Password password;


    public BotAccessorOperator(TSBotLogin save, Username username, Password password) {
        this.save = save;
        this.username = username;
        this.password = password;
    }
}
