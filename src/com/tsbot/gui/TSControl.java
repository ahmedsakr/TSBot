package com.tsbot.gui;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.tsbot.bot.Functions;

import java.awt.Color;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * Constructor for {@link com.tsbot.gui.TSControl}.
     * After the user has been admitted access to the bot, the user is ready to start
     * utilizing all functions and features the bot has to offer. In result, build the frame
     * and display it to the user.
     *
     * @param api the corresponding api for the provided server.
     */
    public TSControl(TS3Api api) {
        super("TSBot - Control");
        this.api = api;
        functions = new Functions(this.api);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500,400));
        setSize(getPreferredSize());
        setResizable(false);
        getContentPane().setLayout(null);
    }


    /**
     * CRITICAL: TSControl#load() has to be called for the constitutes of the frame to appear!
     * The constructor only builds the frame itself, not it's expected components.
     *
     * That being said, this delivers the content to the frame itself.
     */
    public void load() {
        JTextField chatArea = new JTextField(15);
        chatArea.setBounds(10, 10, 300, 40);
        new GhostText("Send A Message...", chatArea);
        getContentPane().add(chatArea);

        JButton serverChat = new JButton("Send Server Chat");
        serverChat.setBounds(320, 10, 150, 20);
        getContentPane().add(serverChat);
        serverChat.addActionListener(a -> functions.sendServerMessage(chatArea));

        JButton channelChat = new JButton("Send Channel Chat");
        channelChat.setBounds(320, 30, 150, 20);
        getContentPane().add(channelChat);
        channelChat.addActionListener(a -> functions.sendChannelMessage(chatArea));

        JTextField pokeText = new JTextField(15);
        pokeText.setBounds(10,60,300,40);
        new GhostText("Enter Poke Text...", pokeText);
        getContentPane().add(pokeText);

        JButton pokeAll = new JButton("Poke All");
        pokeAll.setBounds(320,60,150,20);
        getContentPane().add(pokeAll);
        pokeAll.addActionListener(a -> {

            if (pokeText.getText().isEmpty() || pokeText.getText().equalsIgnoreCase("Enter Poke Text..."))
                return;

            Runnable r = () -> functions.poke(api.getClients(), pokeText.getText());
            new Thread(r).start();
            pokeText.setText("");
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

            Runnable r = () -> functions.poke(api.getClientByName(client), pokeText.getText());
            new Thread(r).start();
            pokeText.setText("");
        });

        DefaultListModel model = new DefaultListModel();
        JList onlineClients = new JList(model);
        JScrollPane pane = new JScrollPane(onlineClients);
        onlineClients.setBackground(Color.GRAY);
        onlineClients.setSelectionBackground(Color.RED);
        pane.setBounds(10, 120, 300, 150);
        getContentPane().add(pane);

        api.getClients().forEach((client) -> model.addElement(client.getNickname()));

        JButton demote = new JButton("Demote");
        demote.setBounds(320, 120, 150, 30);
        getContentPane().add(demote);
        demote.addActionListener(a -> {
            Runnable run = () -> {
                List<Client> selectedClients = new ArrayList<>();
                for (int index : onlineClients.getSelectedIndices()) {
                    selectedClients.add(api.getClientByName(model.get(index).toString()).get(0));
                }

                functions.demote(selectedClients);
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
                    selectedClients.add(api.getClientByName(model.get(index).toString()).get(0));
                }

                functions.ban(selectedClients, seconds);
            };

            new Thread(run).start();
        });

        JButton refresh = new JButton("Refresh Clients");
        refresh.setBounds(320, 200, 150, 30);
        getContentPane().add(refresh);
        refresh.addActionListener(a -> functions.refreshClients(onlineClients, api.getClients()));
    }
}
