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

package com.tsbot.management;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.tsbot.management.interaction.listeners.UserActionListener;
import com.tsbot.management.interaction.listeners.CommandListener;
import com.tsbot.management.interaction.Functions;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import com.tsbot.effects.GhostText;
import com.tsbot.management.threads.ClientsRefreshThread;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/**
 *
 * @author ahmad sakr
 * @since March 20, 2015.
 */
public class TSControl extends JFrame {

    private TS3Api api;
    private Functions functions;
    private String botNickname;
    private JList onlineClients;


    /**
     * Constructor for {@link TSControl}.
     * After the user has been admitted access to the bot, the user is ready to start
     * utilizing all functions and features the bot has to offer. In result, build the frame
     * and display it to the user.
     *
     * @param api the corresponding api for the provided server.
     * @param botNickname the bot's nickname.
     */
    public TSControl(TS3Api api, String botNickname) {
        super("TSBot - Control");
        this.api = api;
        this.botNickname = botNickname;
        this.functions = new Functions(this.api);
        this.api.registerAllEvents();
        this.api.addTS3Listeners(new CommandListener(this.api, botNickname));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        setSize(getPreferredSize());
        setResizable(false);
        getContentPane().setLayout(null);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (api != null) {
                    api.logout();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        components();

        ClientsRefreshThread refreshThread = new ClientsRefreshThread(this.api, onlineClients, this.botNickname);
        refreshThread.start();
    }


    /**
     * Loads all the components onto the frame.
     */
    private void components() {

        JTextField chatArea = new JTextField(15);
        chatArea.setBounds(10, 10, 300, 40);
        GhostText chatDisplayText = new GhostText("Send A Message...", chatArea);
        getContentPane().add(chatArea);

        JButton serverChat = new JButton("Send Server Chat");
        serverChat.setBounds(320, 10, 150, 20);
        getContentPane().add(serverChat);
        serverChat.addActionListener(a -> {
            this.functions.sendServerMessage(chatArea);
            chatArea.setText(chatDisplayText.toString());
        });

        JButton channelChat = new JButton("Send Channel Chat");
        channelChat.setBounds(320, 30, 150, 20);
        getContentPane().add(channelChat);
        channelChat.addActionListener(a -> {
            this.functions.sendChannelMessage(chatArea);
            chatArea.setText(chatDisplayText.toString());
        });

        DefaultListModel model = new DefaultListModel();
        onlineClients = new JList(model);
        onlineClients.addMouseListener(new UserActionListener(this.api, onlineClients));
        JScrollPane pane = new JScrollPane(onlineClients);
        onlineClients.setBackground(Color.GRAY);
        onlineClients.setSelectionBackground(Color.RED);
        pane.setBounds(10, 60, 460, 250);
        getContentPane().add(pane);

        this.api.getClients().forEach((client) -> {
            if (!client.getNickname().equalsIgnoreCase(botNickname)) {
                model.addElement(client.getNickname());
            }
        });

        JButton inputIntelligence = new JButton("Conversation Intelligence");
        inputIntelligence.setBounds(10, 320, 300, 30);
        getContentPane().add(inputIntelligence);
        inputIntelligence.addActionListener((a) -> {
            Conversation input = new Conversation();
            input.setVisible(true);
            input.setLocationRelativeTo(null);
        });

        JButton developerConsole = new JButton("Developer Console");
        developerConsole.setBounds(320, 320, 150, 30);
        getContentPane().add(developerConsole);
        developerConsole.addActionListener((a) -> {
            DeveloperConsole input = new DeveloperConsole();
            input.setVisible(true);
            input.setLocationRelativeTo(null);
        });
    }
}