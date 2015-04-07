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

package com.tsbot;


import com.tsbot.login.TSBotLogin;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author ahmad sakr
 * @since March 13, 2015.
 */
public class TSBot {


    /**
     * Starting point for TSBot.
     * Sets the Look and feel to the user's OS default.
     * Redirects the user to the login page in order to validate the personnel before usage of the application.
     *
     * @param args the runtime arguments.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException |
                InstantiationException e) {
            e.printStackTrace();
        }

        TSBotLogin gui = new TSBotLogin();
        gui.load();
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
    }
}
