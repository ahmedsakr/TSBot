package com.tsbot;


import com.tsbot.gui.TSBotGUI;

/**
 *
 * @author ahmad sakr
 * @since March 13, 2015.
 */
public class TSBot {


    public static void main(String[] args) {
        TSBotGUI gui = new TSBotGUI("TeamSpeak Bot");
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
    }
}
