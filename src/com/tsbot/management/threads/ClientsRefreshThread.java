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

package com.tsbot.management.threads;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.tsbot.management.interaction.Functions;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ahmad Sakr
 * @since April 11, 2015.
 */
public class ClientsRefreshThread extends Thread {

    private TS3Api api;
    private JList clients;
    private Functions functions;
    private String botNickname;

    public ClientsRefreshThread(TS3Api api, JList clients, String botNickname) {
        this.api = api;
        this.clients = clients;
        this.botNickname = botNickname;
        this.functions = new Functions(api);
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                sleep(7500);
                Object clientName = this.clients.getSelectedValue();
                functions.refreshClients(this.clients, this.api.getClients(), this.botNickname);
                ListModel model = this.clients.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).equals(clientName)) {
                        this.clients.setSelectedIndex(i);
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}