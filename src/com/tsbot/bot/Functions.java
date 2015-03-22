package com.tsbot.bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/**
 *
 * @author ahmad sakr
 * @since March 21, 2015.
 */
public class Functions {


    private TS3Api api;


    /**
     * Constructor for {@link com.tsbot.bot.Functions}.
     *
     * @param api the corresponding api associated with the server.
     */
    public Functions(TS3Api api) {
        this.api = api;
    }


    /**
     * Loads the permissions for the selected clients, giving the operator an interface to
     * deselect/select permissions the user should have.
     *
     * @param clients the clients to be acted upon.
     */
    public void permissions(List<Client> clients) {
        loadPermissions(clients, api.getServerGroups());
    }


    private void loadPermissions(List<Client> clients, List<ServerGroup> groups) {
        if (clients.size() == 0) {
            return;
        }

        JFrame frame = new JFrame("Change Permissions - " + clients.get(0).getNickname());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500,100);
        frame.setResizable(false);


        JPanel panel = new JPanel();

        List<JCheckBox> permissions = new ArrayList<>();
        List<Integer> permissionsId = new ArrayList<>();
        for (ServerGroup group : groups) {
            // TeamSpeak default server group ids not for users.
            if (group.getId() < 10)
                continue;

            JCheckBox checkBox = new JCheckBox(group.getName());
            checkBox.setFocusPainted(false);

            for (int groupId : clients.get(0).getServerGroups()) {
                if (groupId == group.getId()) {
                    checkBox.setSelected(true);
                }
            }

            permissions.add(checkBox);
            permissionsId.add(group.getId());
            panel.add(checkBox);
        }


        JScrollPane scroll = new JScrollPane(panel);
        frame.getContentPane().add(scroll);

        JPanel actions = new JPanel();
        JCheckBox sameAction = null;

        if (clients.size() > 1) {
            sameAction = new JCheckBox("Same permissions for the next " + (clients.size() - 1 == 1 ?
                    (clients.size() - 1 + " client") : (clients.size() - 1 + " clients")));
            sameAction.setFocusPainted(false);
            actions.add(sameAction);
        }

        JButton next = new JButton("Next");
        next.setFocusPainted(false);
        actions.add(next);

        frame.getContentPane().add(actions, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        final JCheckBox decisionValue = sameAction;
        next.addActionListener((a) -> {
            Runnable run = () -> {
                if (decisionValue != null && decisionValue.isSelected()) {
                    for (Client client : clients) {
                        for (int i = 0; i < permissions.size(); i++) {
                            if (permissions.get(i).isSelected()) {
                                api.addClientToServerGroup(permissionsId.get(i), client.getDatabaseId());
                            } else {
                                api.removeClientFromServerGroup(permissionsId.get(i), client.getDatabaseId());
                            }
                        }
                    }

                    clients.clear();
                    frame.dispose();
                    loadPermissions(clients, groups);
                } else {
                    for (int i = 0; i < permissions.size(); i++) {
                        if (permissions.get(i).isSelected()) {
                            api.addClientToServerGroup(permissionsId.get(i), clients.get(0).getDatabaseId());
                        } else {
                            api.removeClientFromServerGroup(permissionsId.get(i), clients.get(0).getDatabaseId());
                        }
                    }

                    clients.remove(0);
                    frame.dispose();
                    loadPermissions(clients, groups);
                }


            };

            new Thread(run).start();
        });
    }

    /**
     * Sends a message to the main chat. (server)
     *
     * @param field the field where text is being taken from.
     */
    public void sendServerMessage(JTextField field) {

        if (field.getText().isEmpty() || field.getText().equalsIgnoreCase("Send A Message..."))
            return;

        api.sendServerMessage(field.getText());
        field.setText("");
    }


    /**
     * Sends a message to the channel chat the bot is currently residing in.
     *
     * @param field the field where text is being taken from.
     */
    public void sendChannelMessage(JTextField field) {

        if (field.getText().isEmpty() || field.getText().equalsIgnoreCase("Send A Message..."))
            return;

        api.sendChannelMessage(field.getText());
        field.setText("");
    }


    /**
     * Pokes all the clients in the List clients with the message text.
     *
     * @param clients the clients to be poked.
     * @param text    the associated text of the poke.
     */
    public void poke(List<Client> clients, String text) {
        clients.forEach(client -> api.pokeClient(client.getId(), text));
    }


    /**
     * Bans all the clients in the List clients, with a timeout of hours provided.
     *
     * @param clients the clients to be banned from the server.
     * @param hours   the time period for the clients to be banned.
     */
    public void ban(List<Client> clients, int hours) {
        // hours is being multiplied by 3600 as the input for banClient is seconds.
        clients.forEach((client) -> api.banClient(client.getId(), hours * 3600));
    }


    /**
     * Updates the online clients in the list.
     * Remove all online clients in the list are re-add the updated ones.
     *
     * @param list the list to update the elements in.
     * @param clients the updated online clients.
     */
    public void refreshClients(JList list, List<Client> clients) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        for (Client client: clients) {
            model.addElement(client.getNickname());
        }

        list.setModel(model);
    }
}