package com.tsbot.gui;


import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author ahmad sakr
 * @since March 22, 2015.
 */
public class DeveloperConsole extends JFrame {



    public DeveloperConsole() {
        super("TSBot - Developer Console");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,400);
        setResizable(false);

        JTextArea textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textArea.setEditable(false);
        textArea.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(textArea);

        getContentPane().add(scroll);

        System.setOut(new PrintStream(new TSBotOutputStream(textArea)));

        JButton dump = new JButton("Dump");
        getContentPane().add(dump, BorderLayout.SOUTH);
    }
}


class TSBotOutputStream extends OutputStream {


    private JTextArea textArea;


    public TSBotOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }


    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
    }
}
