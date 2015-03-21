package com.tsbot.gui;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ahmad sakr
 * @since March 20, 2015.
 */
public class TSControl extends JFrame {

    private TS3Api api;

    public TSControl(TS3Api api) {
        super("TSBot - Control");
        this.api = api;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500,400));
        setSize(getPreferredSize());
        setResizable(false);
        getContentPane().setLayout(null);
    }


    public void load() {
        JTextField chatArea = new JTextField(15);
        chatArea.setBounds(10, 10, 300, 40);
        new GhostText("Send A Message...", chatArea);
        getContentPane().add(chatArea);

        JButton serverChat = new JButton("Send Server Chat");
        serverChat.setBounds(320, 10, 150, 20);
        getContentPane().add(serverChat);
        serverChat.addActionListener(a -> sendServerMessage(chatArea));

        JButton channelChat = new JButton("Send Channel Chat");
        channelChat.setBounds(320, 30, 150, 20);
        getContentPane().add(channelChat);
        channelChat.addActionListener(a -> sendChannelMessage(chatArea));

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

            Runnable r = () -> poke(api.getClients(), pokeText.getText());
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

            Runnable r = () -> poke(api.getClientByName(client), pokeText.getText());
            new Thread(r).start();
            pokeText.setText("");
        });

    }


    private void sendServerMessage(JTextField field) {

        if (field.getText().isEmpty() || field.getText().equalsIgnoreCase("Send A Message..."))
            return;

        api.sendServerMessage(field.getText());
        field.setText("");
    }

    private void sendChannelMessage(JTextField field) {

        if (field.getText().isEmpty() || field.getText().equalsIgnoreCase("Send A Message..."))
            return;

        api.sendChannelMessage(field.getText());
        field.setText("");
    }

    private void poke(List<Client> clients, String text) {
        clients.forEach(client -> api.pokeClient(client.getId(), text));
    }
}
