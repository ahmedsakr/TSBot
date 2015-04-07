package com.tsbot.interaction;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ahmad sakr
 * @since March 21, 2015.
 */
public class Functions {


    private TS3Api api;


    /**
     * Constructor for {@link Functions}.
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
        permissions(clients, api.getServerGroups());
    }


    /**
     * Loads the permissions for the selected clients, giving the operator an interface to
     * deselect/select permissions the user should have.
     * The current client's server groups are displayed automatically to display occupied server groups.
     *
     * @param clients the clients to be acted upon.
     * @param groups  the groups to act upon.
     */
    private void permissions(List<Client> clients, List<ServerGroup> groups) {
        if (clients.size() == 0) {
            return;
        }

        Client currentClient = clients.get(0);

        JFrame frame = new JFrame("Permissions - " + currentClient.getNickname());
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

            for (int groupId : currentClient.getServerGroups()) {
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
        boolean isSamePermissions = sameAction != null;
        next.addActionListener((a) -> {
            Runnable run = () -> {
                if (isSamePermissions) {
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
                    permissions(clients, groups);
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
        clients.forEach(client -> poke(client, text));
    }


    /**
     * Pokes a certain client specified in the param arguments with the certain text.
     *
     * @param client the client being poked.
     * @param text    the associated text of the poke.
     */
    public void poke(Client client, String text) {
        if (client == null) {
            JOptionPane.showMessageDialog(null,
                    "The Specified client is not online.", "Client Offline", JOptionPane.ERROR_MESSAGE);
        } else {
            api.pokeClient(client.getId(), text);
        }
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
    public void refreshClients(JList list, List<Client> clients, String botNickname) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        for (Client client: clients) {
            if (!client.getNickname().equalsIgnoreCase(botNickname)) {
                model.addElement(client.getNickname());
            }
        }

        list.setModel(model);
    }
}