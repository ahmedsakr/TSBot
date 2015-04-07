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
import com.tsbot.interaction.listeners.CommandListener;
import com.tsbot.interaction.Functions;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import com.tsbot.effects.GhostText;
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

        JTextField pokeText = new JTextField(15);
        pokeText.setBounds(10,60,300,40);
        GhostText pokeDisplayText = new GhostText("Enter Poke Text...", pokeText);
        getContentPane().add(pokeText);

        JButton pokeAll = new JButton("Poke All");
        pokeAll.setBounds(320,60,150,20);
        getContentPane().add(pokeAll);
        pokeAll.addActionListener(a -> {

            if (pokeText.getText().isEmpty() || pokeText.getText().equalsIgnoreCase("Enter Poke Text..."))
                return;

            Runnable r = () -> {
                this.functions.poke(this.api.getClients(), pokeText.getText());
                pokeText.setText(pokeDisplayText.toString());
            };

            new Thread(r).start();
        });

        JButton pokeClient = new JButton("Poke Client");
        pokeClient.setBounds(320, 80, 150, 20);
        getContentPane().add(pokeClient);
        pokeClient.addActionListener(a -> {

            if (pokeText.getText().isEmpty() || pokeText.getText().equalsIgnoreCase("Enter Poke Text..."))
                return;

            String client = JOptionPane.showInputDialog(null,
                    "Enter a client's username:", "Input Client", JOptionPane.INFORMATION_MESSAGE);

            if (client == null || client.isEmpty())
                return;

            Runnable r = () -> {
                this.functions.poke(this.api.getClientByNameExact(client, true), pokeText.getText());
                pokeText.setText(pokeDisplayText.toString());
            };

            new Thread(r).start();

        });

        DefaultListModel model = new DefaultListModel();
        JList onlineClients = new JList(model);
        JScrollPane pane = new JScrollPane(onlineClients);
        onlineClients.setBackground(Color.GRAY);
        onlineClients.setSelectionBackground(Color.RED);
        pane.setBounds(10, 120, 300, 150);
        getContentPane().add(pane);

        this.api.getClients().forEach((client) -> {
            if (!client.getNickname().equalsIgnoreCase(botNickname)) {
                model.addElement(client.getNickname());
            }
        });

        JButton permissions = new JButton("Permissions");
        permissions.setBounds(320, 120, 150, 30);
        getContentPane().add(permissions);
        permissions.addActionListener(a -> {
            Runnable run = () -> {
                List<Client> selectedClients = new ArrayList<>();
                for (int index : onlineClients.getSelectedIndices()) {
                    selectedClients.add(this.api.getClientByNameExact(model.get(index).toString(), true));
                }

                this.functions.permissions(selectedClients);
            };

            new Thread(run).start();
        });


        JButton ban = new JButton("Ban");
        ban.setBounds(320, 160, 150, 30);
        getContentPane().add(ban);
        ban.addActionListener(a -> {

            int seconds = Integer.valueOf(JOptionPane.showInputDialog(null,
                    "Enter amount of hours to ban:", "Ban Time", JOptionPane.INFORMATION_MESSAGE));

            if (seconds == 0)
                return;

            Runnable run = () -> {
                List<Client> selectedClients = new ArrayList<>();
                for (int index : onlineClients.getSelectedIndices()) {
                    selectedClients.add(this.api.getClientByNameExact(model.get(index).toString(), true));
                }

                this.functions.ban(selectedClients, seconds);
            };

            new Thread(run).start();
        });

        JButton banList = new JButton("Ban List");
        banList.setBounds(320, 200, 150, 30);
        getContentPane().add(banList);

        JButton refresh = new JButton("Refresh Clients");
        refresh.setBounds(320, 240, 150, 30);
        getContentPane().add(refresh);
        refresh.addActionListener(a -> this.functions.refreshClients(onlineClients, this.api.getClients(), botNickname));

        JButton inputIntelligence = new JButton("Input Intelligence");
        inputIntelligence.setBounds(10, 280, 145, 30);
        getContentPane().add(inputIntelligence);
        inputIntelligence.addActionListener((a) -> {
            InputProcessing input = new InputProcessing();
            input.setVisible(true);
            input.setLocationRelativeTo(null);
        });

        JButton developerConsole = new JButton("Developer Console");
        developerConsole.setBounds(165, 280, 145, 30);
        getContentPane().add(developerConsole);
        developerConsole.addActionListener((a) -> {
            DeveloperConsole input = new DeveloperConsole();
            input.setVisible(true);
            input.setLocationRelativeTo(null);
        });
    }
}