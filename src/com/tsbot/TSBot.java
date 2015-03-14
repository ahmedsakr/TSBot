package com.tsbot;


import com.tsbot.gui.TSBotLogin;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author ahmad sakr
 * @since March 13, 2015.
 */
public class TSBot {


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException |
                InstantiationException e) {
            e.printStackTrace();
        }

        TSBotLogin gui = new TSBotLogin("TeamSpeak Bot - Login");
        gui.init();
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
    }
}
