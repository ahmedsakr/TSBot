package com.tsbot.gui;


import javax.swing.*;
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
     *
     * @param title the specified title of the frame.
     */
    public TSBotLogin(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));
        setSize(getPreferredSize());
        setResizable(false);
        setLayout(null);
    }


    /**
     * Initiates all the needed components onto the frame.
     * For optimal usage, call of the method is to be done before setting the frame visible.
     */
    public void load() {

        JTextField usernameInput = new JTextField(15);
        usernameInput.setBounds(30,30,220,30);
        new GhostText("Username", usernameInput);

        JPasswordField passwordInput = new JPasswordField(15);
        passwordInput.setBounds(30,70,220,30);
        new GhostText("Password", passwordInput);

        JButton login = new JButton("Login");
        login.setBounds(150,110,100,30);

        String[] messages = {"BITCHEEEEEEEEEEES", "YOU MOTHERFUCKING ASSHOLES", "YOU DENSE MOTHERFUCKER"};
        JLabel motd = new JLabel("Message of the day: \"" + messages[(int) (Math.random() * messages.length)] + "\"");
        motd.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        motd.setBounds(10,150, 300, 30);

        getContentPane().add(usernameInput);
        getContentPane().add(passwordInput);
        getContentPane().add(login);
        getContentPane().add(motd);
    }
}
