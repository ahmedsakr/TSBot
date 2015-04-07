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
        frame.setSize(500, 100);

        JPanel panel = new JPanel();

        // Parallel lists. Every checkbox (permissions) has a corresponding ServerGroup Id (permissionsIds)
        List<JCheckBox> permissions = new ArrayList<>();
        List<Integer> permissionIds = new ArrayList<>();

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
            permissionIds.add(group.getId());
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

        final JCheckBox finalSameAction = sameAction;
        next.addActionListener((a) -> {

            boolean isSamePermissions = finalSameAction != null && finalSameAction.isSelected();
            Runnable run = () -> {

                // if the operator has indicated to assign the selected ranks to all the upcoming clients.
                if (isSamePermissions) {

                    clients.forEach((client) -> changePermissions(permissions, permissionIds, client));

                    clients.clear();
                    frame.dispose();
                } else {

                    changePermissions(permissions, permissionIds, currentClient);

                    clients.remove(0);
                    frame.dispose();
                    permissions(clients, groups);
                }

            };

            new Thread(run).start();
        });
    }


    /**
     * According to the data from boxes and permissionIds, the client's permissions will be changed accordingly.
     * If a certain ServerGroup is selected, then the client will be added to that ServerGroup if they are not
     * part of it already. If they are part of it, the iteration will be ignored as attempting to add a client
     * to a ServerGroup that they already partake in returns an error.
     *
     * If a certain ServerGroup is deselected, then the client will be removed from that ServerGroup if they are part
     * of it. If they are not part of it, the iteration will be ignored as attempting to remove a client from a
     * ServerGroup that they do not partake in returns an error.
     *
     * @param boxes A list of {@link JCheckBox}s that represent the ServerGroups
     * @param permissionIds The corresponding list of ServerGroup Identification numbers.
     * @param client The client to take action upon.
     */
    private void changePermissions(List<JCheckBox> boxes, List<Integer> permissionIds, Client client) {
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).isSelected()) {
                int groupId = permissionIds.get(i);

                // if the user is already in that server group, then jump to the next iteration.
                if (inServerGroup(client, groupId)) {
                    continue;
                }

                api.addClientToServerGroup(groupId, client.getDatabaseId());
            } else {
                int groupId = permissionIds.get(i);

                // if the user is already not in that server group, then jump to the next iteration.
                if (!inServerGroup(client, groupId)) {
                    continue;
                }

                api.removeClientFromServerGroup(groupId, client.getDatabaseId());
            }
        }
    }


    /**
     * Performs a sequential search on the client's assigned ServerGroups in a quest to find if it contains groupId.
     *
     * @param client The client in question.
     * @param groupId the groupId the search is attempting to locate.
     * @return {@code true} If the groupId is found in the client's assigned ServerGroups
     *         {@code false} if the groupId is not found.
     */
    private boolean inServerGroup(Client client, int groupId) {
        for (int id : client.getServerGroups()) {
            if (groupId == id) {
                return true;
            }
        }

        return false;
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