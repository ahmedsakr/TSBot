package com.tsbot.gui;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;


/**
 *
 * @author ahmad sakr
 * @since March 28, 2015.
 */
public class InputProcessing extends JFrame {


    public InputProcessing() {
        super("TSBot - Input Intelligence");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        setSize(getPreferredSize());
        setResizable(false);
    }


    public void load() {
        JTable rules = new JTable(
                new Object[][]{{"Hello", "False", "Hello, <name>!"}},
                new Object[]{"Text", "Input exact as Text", "Output"}
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane scrollPane = new JScrollPane(rules);
        getContentPane().add(scrollPane);

        JPanel buttons = new JPanel();
        JButton process = new JButton("Add Process");
        JButton remove = new JButton("Remove selected processes");
        buttons.add(process);
        buttons.add(remove);

        getContentPane().add(buttons, BorderLayout.SOUTH);

    }
}
