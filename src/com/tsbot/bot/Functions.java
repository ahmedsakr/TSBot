package com.tsbot.bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
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
     * Removes every single server group assigned to the selected clients - leaving them as guests.
     *
     * @param clients the clients to be acted upon.
     */
    public void demote(List<Client> clients) {
        List<ServerGroup> groups = api.getServerGroups();
        for (Client client: clients) {
            for (ServerGroup group : groups) {
                if (group.getName().equalsIgnoreCase("Guest"))
                    continue;

                api.removeClientFromServerGroup(group.getId(), client.getDatabaseId());
            }
        }
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