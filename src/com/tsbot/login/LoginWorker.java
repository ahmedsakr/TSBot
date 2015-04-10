/**
 * Copyright (c) 2015 Ahmed Sakr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsbot.login;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.tsbot.login.security.Credential;

import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;

import com.tsbot.management.Management;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class LoginWorker extends JFrame {

    private TSBotLogin save;
    private Credential serverAddress, serverPort, botNickname, queryUser, queryPass;
    private JProgressBar progress;


    /**
     *
     * Constructor for {@link LoginWorker}.
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
    public LoginWorker(TSBotLogin save, Credential serverAddress, Credential serverPort, Credential botNickname,
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
     * Initialize the {@link TS3Api} and {@link TS3Query} objects to connect to the server.
     * If the connection is a success and the user's identification has been approved, then it proceeds
     * to the {@link Management} object. Otherwise, the user is taken back to the login interface.
     *
     * @throws InterruptedException
     */
    public void login() throws InterruptedException {
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
            return;
        }

        progress.setValue(1);
        progress.setString("Success! Loading...");

        new Management(api, botNickname);
        dispose();
    }


    /**
     * Validates whether to the connection to the server has been successful.
     *
     * @param api the api used to handshake the server.
     *
     * @return the connection status of the api.
     *         {@code true} if the api is successfully hooked and on-line.
     *         {@code false} if the api failed to connect for whatever reason.
     */
    private boolean isConnected(TS3Api api) {
        return api != null && api.getClients() != null;
    }

}
