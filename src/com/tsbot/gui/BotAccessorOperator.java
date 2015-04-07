package com.tsbot.gui;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.tsbot.credentials.Credential;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;

import com.tsbot.io.IntelligenceReader;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

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
        setPreferredSize(new Dimension(200, 50));
        setSize(getPreferredSize());
        setUndecorated(true);

        progress = new JProgressBar(0, 1);
        progress.setStringPainted(true);
        progress.setForeground(Color.GREEN);
        progress.setString("Authenticating...");

        progress.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.BLACK; }
            protected Color getSelectionForeground() { return Color.BLACK; }
        });

        getContentPane().add(progress, "Center");
    }


    /**
     * Establish a connection to the server that has been specified by the user.
     * TODO rewrite the work method, it's horribly written.
     */
    public void work() throws InterruptedException {
        TS3Config config = new TS3Config();
        config.setHost(serverAddress.toString());
        config.setDebugLevel(Level.ALL);
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED);
        config.setLoginCredentials(queryUser.toString(), queryPass.toString());

        TS3Query query = new TS3Query(config);
        query.connect();

        TS3Api api = query.getApi();
        api.selectVirtualServerByPort(Integer.valueOf(serverPort.toString()));

        if (!isConnected(api)) {

            dispose();
            save.setVisible(true);
            query.exit();
            config = null;
            JOptionPane.showMessageDialog(save, "Connection to " + serverAddress + " failed." +
                    " Make sure you input the correct ServerQuery credentials.");
        } else {

            progress.setValue(1);
            progress.setString("Success! Loading...");
            api.setNickname(botNickname.toString());

            prepareEnvironment();

            DeveloperConsole console = new DeveloperConsole();
            console.setVisible(true);
            console.setLocation(0, 0);

            InputProcessing input = new InputProcessing();
            input.setVisible(true);
            input.setLocation(0, console.getHeight() + 10);

            TSControl control = new TSControl(api, botNickname.toString());
            control.setVisible(true);
            control.setLocation(console.getWidth() + 200, console.getHeight() / 2);

            dispose();
        }
    }


    private boolean isConnected(TS3Api api) {
        return api != null && api.getClients() != null;
    }

    private void prepareEnvironment() {
        try {
            IntelligenceReader.createNeededDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
