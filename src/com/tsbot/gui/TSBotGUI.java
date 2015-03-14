package com.tsbot.gui;


import javax.swing.JFrame;
import java.awt.Dimension;


/**
 *
 * @author ahmad sakr
 * @since March 14, 2015.
 */
public class TSBotGUI extends JFrame {


    public TSBotGUI(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500,300));
        setSize(getPreferredSize());
    }
}
