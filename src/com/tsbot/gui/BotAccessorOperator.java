package com.tsbot.gui;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.tsbot.credentials.Credential;

import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class BotAccessorOperator extends JFrame {

    private TSBotLogin save;
    private Credential serverAddress, serverPort, botNickname, queryUser, queryPass;
    private JProgressBar progress;


    /**
     *
     * Constructor for {@link com.tsbot.gui.BotAccessorOperator}.
     * The reason behind requiring an object of TSBotLogin is to simply redirect the user back to the original
     * login frame if the login failed for any reason. (incorrect password or invalid username)
     * Builds the frame for logging in the user.
     *
     *
     * @param save The archived object of TSBotLogin, to be used if needed - otherwise to be nulled.
     * @param serverAddress The desired server address credential.
     * @param serverPort the desired server port credential.
     * @param botNickname The desired botNickname credential.
     * @param queryUser The ServerQuery Username credential.
     * @param queryPass The ServerQuery Password credential.
     */
    public BotAccessorOperator(TSBotLogin save, Credential serverAddress, Credential serverPort, Credential botNickname,
                               Credential queryUser, Credential queryPass) {
        this.save = save;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.botNickname = botNickname;
        this.queryUser = queryUser;
        this.queryPass = queryPass;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(200,50));
        setSize(getPreferredSize());
        setUndecorated(true);

        progress = new JProgressBar(0, 100);
        progress.setStringPainted(true);
        progress.setForeground(Color.ORANGE);
        progress.setString("Authenticating...");
        getContentPane().add(progress, "Center");
    }


    /**
     * Establish a connection to the server that has been specified by the user.
     */
    public void work() {
        final TS3Config config = new TS3Config();
        config.setHost(serverAddress.toString());
        config.setDebugLevel(Level.ALL);
        config.setLoginCredentials(queryUser.toString(), queryPass.toString());

        final TS3Query query = new TS3Query(config);
        query.connect();

        final TS3Api api = query.getApi();
        api.selectVirtualServerByPort(Integer.valueOf(serverPort.toString()));

        progress.setValue(100);
        progress.setString("Success!");

        api.setNickname(botNickname.toString());
    }
}
