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

package com.tsbot.management.interaction.listeners;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.tsbot.management.interaction.menus.ActionPopMenu;
import javax.swing.JList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Ahmad Sakr
 * @since April 10, 2015.
 */
public class UserActionListener extends MouseAdapter {

    private ActionPopMenu menu;

    public UserActionListener(TS3Api api, JList clients) {
        this.menu = new ActionPopMenu(api, clients);
    }

    public void mousePressed(MouseEvent e){

    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger()) {
            JList list = (JList) e.getSource();
            list.setSelectedIndex(list.locationToIndex(e.getPoint()));

            if (list.getSelectedIndex() != -1) {
                showMenu(e);
            }
        }
    }

    private void showMenu(MouseEvent e){
        this.menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
