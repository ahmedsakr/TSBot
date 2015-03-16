package com.tsbot.gui;

import com.tsbot.credentials.Password;
import com.tsbot.credentials.Username;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class BotAccessorOperator extends JFrame {

    private TSBotLogin save;
    private Username username;
    private Password password;
    private JProgressBar progress;


    /**
     *
     * Constructor for {@link com.tsbot.gui.BotAccessorOperator}.
     * The reason behind requiring an object of TSBotLogin is to simply redirect the user back to the original
     * login frame if the login failed for any reason. (incorrect password or invalid username)
     * Builds the frame for logging in the user.
     *
     *
     * @param save the archived object of TSBotLogin, to be used if needed - otherwise to be nulled.
     * @param username the Username object
     * @param password the Password object
     */
    public BotAccessorOperator(TSBotLogin save, Username username, Password password) {
        this.save = save;
        this.username = username;
        this.password = password;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(200,50));
        setSize(getPreferredSize());
        setUndecorated(true);

        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setForeground(Color.GREEN);
        progress.setString("Authenticating...");

        getContentPane().add(progress, "Center");
    }


    /**
     * Handshaking with that database source begins here.
     * In the following order: sends a request to check if the username exists. If so, then it will proceed to check
     * if the password provided is equivalent to that of the username.
     *
     * If not, the user will be redirected to the original login frame with their corresponding error displayed
     * on a {@link javax.swing.JOptionPane}.
     */
    public void work() {

    }
}
