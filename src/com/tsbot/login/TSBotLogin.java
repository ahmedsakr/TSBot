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

package com.tsbot.login;


import com.tsbot.login.security.Credential;
import com.tsbot.effects.GhostText;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


/**
 *
 * @author ahmad sakr
 * @since March 14, 2015.
 */
public class TSBotLogin extends JFrame {


    /**
     * Constructor for {@link TSBotLogin}.
     * Builds the JFrame in question with the provided title.
     */
    public TSBotLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 350));
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
        new GhostText("Bot Nickname", usernameInput);


        JTextField serverAddress = new JTextField(15);
        serverAddress.setBounds(30,90,220,30);
        serverAddress.setBackground(Color.GRAY);
        new GhostText("Server Address", serverAddress);

        JTextField serverPort = new JTextField(15);
        serverPort.setBounds(30, 130, 220, 30);
        serverPort.setBackground(Color.GRAY);
        new GhostText("Server Port", serverPort);

        JTextField serverQueryUsername = new JTextField(15);
        serverQueryUsername.setBounds(30,170,220,30);
        serverQueryUsername.setBackground(Color.GRAY);
        new GhostText("ServerQuery Username", serverQueryUsername);

        JTextField serverQueryPassword = new JTextField(15);
        serverQueryPassword.setBounds(30,210,220,30);
        serverQueryPassword.setBackground(Color.GRAY);
        new GhostText("ServerQuery Password", serverQueryPassword);

        JLabel exit = new JLabel("EXIT");
        exit.setForeground(new Color(6,69,173));
        exit.setBounds(100,260,30,30);
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
        login.setBounds(150,260,100,30);

        login.addActionListener(a -> {
            String username = usernameInput.getText();

            if (username.isEmpty() || username.equals("Username")) {
                JOptionPane.showMessageDialog(this,
                        "Username cannot be empty!", "Missing Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Credential address = new Credential(serverAddress.getText());
            Credential port = new Credential(serverPort.getText());
            Credential botNickname = new Credential(username);
            Credential queryUser = new Credential(serverQueryUsername.getText());
            Credential queryPass = new Credential(serverQueryPassword.getText());

            BotAccessorOperator loginWorker = new BotAccessorOperator(this, address, port, botNickname, queryUser,
                    queryPass);
            dispose();

            loginWorker.setVisible(true);
            loginWorker.setLocationRelativeTo(null);
            Runnable run = () -> {
                try {
                    loginWorker.work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(run).start();
        });

        String[] messages = {"place_holder_1", "place_holder_2", "place_holder_3"};
        JLabel motd = new JLabel("Message of the day: \"" + messages[(int) (Math.random() * messages.length)] + "\"");
        motd.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        motd.setBackground(Color.DARK_GRAY);
        motd.setForeground(Color.WHITE);
        motd.setOpaque(true);
        motd.setBounds(0,300, 300, 30);

        getContentPane().add(usernameInput);
        getContentPane().add(serverAddress);
        getContentPane().add(serverPort);
        getContentPane().add(serverQueryUsername);
        getContentPane().add(serverQueryPassword);
        getContentPane().add(exit);
        getContentPane().add(login);
        getContentPane().add(motd);
    }
}
