package com.tsbot.gui;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;


/**
 *
 * @author ahmad sakr
 * @since March 14, 2015.
 */
public class TSBotLogin extends JFrame {


    /**
     * Constructor for {@link com.tsbot.gui.TSBotLogin}.
     * Builds the JFrame in question with the provided title.
     */
    public TSBotLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));
        setSize(getPreferredSize());
        setResizable(false);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(255,62,51)));

        setContentPane(panel);
    }


    /**
     * Initiates all the needed components onto the frame.
     * For optimal usage, call of the method is to be done before setting the frame visible.
     */
    public void load() {

        JTextField usernameInput = new JTextField(15);
        usernameInput.setBounds(30,30,220,30);
        usernameInput.setBackground(Color.GRAY);
        new GhostText("Username", usernameInput);

        JPasswordField passwordInput = new JPasswordField(15);
        passwordInput.setBackground(Color.GRAY);
        passwordInput.setBounds(30,70,220,30);
        new GhostText("Password", passwordInput);

        JLabel exit = new JLabel("EXIT");
        exit.setForeground(new Color(6,69,173));
        exit.setBounds(100,110,30,30);
        exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton login = new JButton("Login");
        login.setBounds(150,110,100,30);

        String[] messages = {"place_holder_1", "place_holder_2", "place_holder_3"};
        JLabel motd = new JLabel("Message of the day: \"" + messages[(int) (Math.random() * messages.length)] + "\"");
        motd.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        motd.setBackground(Color.DARK_GRAY);
        motd.setForeground(Color.WHITE);
        motd.setOpaque(true);
        motd.setBounds(0,150, 300, 30);

        getContentPane().add(usernameInput);
        getContentPane().add(passwordInput);
        getContentPane().add(exit);
        getContentPane().add(login);
        getContentPane().add(motd);
    }
}
