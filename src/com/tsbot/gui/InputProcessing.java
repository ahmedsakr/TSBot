package com.tsbot.gui;


import com.tsbot.bot.InputProcess;
import com.tsbot.io.InputIntelligenceReader;
import com.tsbot.io.InputIntelligenceWriter;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ahmad sakr
 * @since March 28, 2015.
 */
public class InputProcessing extends JFrame {

    public JTable rules;

    public InputProcessing() {
        super("TSBot - Input Intelligence");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        setSize(getPreferredSize());

        components();
    }


    private void components() {
        DefaultTableModel model = new DefaultTableModel();
        rules = new JTable(model);

        model.addColumn("Input Text");
        model.addColumn("Input exact as Input Text");
        model.addColumn("Output Text");

        try {
            List<InputProcess> processes = new InputIntelligenceReader().processes();
            processes.forEach((process) ->
                    model.addRow(new Object[]{
                            process.getInputText(), process.containsOnly(), process.getOutputText()}));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(rules);
        getContentPane().add(scrollPane);

        JPanel buttons = new JPanel();
        JButton process = new JButton("Amend Intelligence");
        JButton remove = new JButton("Remove selected processes");
        buttons.add(process);
        buttons.add(remove);

        getContentPane().add(buttons, BorderLayout.SOUTH);

        process.addActionListener((a) -> {
            RuleAddition rule = new RuleAddition(rules);
            rule.setVisible(true);
            rule.setLocationRelativeTo(null);
        });
    }
}


class RuleAddition extends JFrame {

    private JTable rules;

    public RuleAddition(JTable rules) {
        super("TSBot - Amend Intelligence");
        this.rules = rules;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));
        setSize(getPreferredSize());
        setResizable(false);

        components();
    }

    private void components() {
        JLabel instructions = new JLabel("<html>" + color("TIP", "ff0000") + ": You can add " +
                color("##name##/##botname##", "ff0000") + " to your input/output fields for it to be replaced" +
                " by the invoker's/bot's name, respectively.</html>");

        getContentPane().add(instructions, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(null);

        JTextField inputText = new JTextField();
        inputText.setBounds(20,40,350,30);
        new GhostText("Input Text", inputText);

        JTextField outputText = new JTextField();
        outputText.setBounds(20, 80, 350, 30);
        new GhostText("Output Text", outputText);

        JCheckBox contains = new JCheckBox("<html>User's input should only " + color("contain", "ff0000") +
                " the specified input text in order for it to execute," +
                " and not explicitly " + color("exact", "ff0000") + " as the input.</html>");
        contains.setBounds(20,140, 350, 50);

        form.add(inputText);
        form.add(outputText);
        form.add(contains);

        JPanel actions = new JPanel();
        JButton update = new JButton("Update");
        JButton cancel = new JButton("Cancel");

        actions.add(update);
        actions.add(cancel);

        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(actions, BorderLayout.SOUTH);

        update.addActionListener((a) -> {
            try (InputIntelligenceWriter writer = new InputIntelligenceWriter()){
                writer.update(inputText.getText(), outputText.getText(), contains.isSelected());
                DefaultTableModel model = (DefaultTableModel) rules.getModel();
                model.addRow(new Object[]{inputText.getText(), contains.isSelected(), outputText.getText()});
                dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    private String color(String text, String hex) {
        return "<span style='color:#" + hex + "'>" + text + "</span>";
    }

}