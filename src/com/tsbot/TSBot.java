package com.tsbot;


import com.tsbot.gui.TSBotLogin;

import com.tsbot.io.InputIntelligenceReader;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.io.IOException;


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
