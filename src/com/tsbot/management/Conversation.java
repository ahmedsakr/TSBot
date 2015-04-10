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

package com.tsbot.management;


import com.tsbot.management.interaction.Intellect;
import com.tsbot.effects.GhostText;
import com.tsbot.io.conversation.ConReader;
import com.tsbot.io.conversation.ConWriter;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmad sakr
 * @since March 28, 2015.
 */
public class Conversation extends JFrame {

    public JTable rules;

    /**
     * Default Constructor.
     *
     * Constructs the Conversation Frame and adds all the components to it.
     */
    public Conversation() {
        super("TSBot - Conversation Intelligence");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        setSize(getPreferredSize());

        components();
    }


    private void components() {
        DefaultTableModel model = new DefaultTableModel();
        rules = new JTable(model);

        model.addColumn("Input Text");
        model.addColumn("Strictly exact as user input");
        model.addColumn("Output Text");

        try (ConReader reader = new ConReader()){
            List<Intellect> intelligence = reader.intelligence();
            intelligence.forEach((intellect) ->
                    model.addRow(new Object[]{
                            intellect.getInputText(), intellect.containsOnly(), intellect.getOutputText()}));
        } catch (IOException e) {
            try {
                ConWriter.create();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        JScrollPane scrollPane = new JScrollPane(rules);
        getContentPane().add(scrollPane);

        JPanel buttons = new JPanel();
        JButton process = new JButton("Amend Intelligence");
        JButton remove = new JButton("Remove selected rows");
        JButton save = new JButton("Save Intelligence");
        JButton loadFile = new JButton("Load file");
        buttons.add(process);
        buttons.add(remove);
        buttons.add(save);
        buttons.add(loadFile);

        getContentPane().add(buttons, BorderLayout.SOUTH);

        process.addActionListener((a) -> {
            RuleAddition rule = new RuleAddition(rules);
            rule.setVisible(true);
            rule.setLocationRelativeTo(null);
        });

        remove.addActionListener((a) -> {
            ArrayList<Integer> indices = new ArrayList<>();
            for (int k : rules.getSelectedRows())
                indices.add(k);

            try {
                removeElements(model, indices);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        save.addActionListener((a) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("dat", "dat"));
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            chooser.setLocation(0,0);
            int action = chooser.showSaveDialog(this);

            if (action == JFileChooser.APPROVE_OPTION) {
                try (ConWriter writer = new ConWriter(chooser.getSelectedFile().toString() + ".dat")) {
                    Object[][] data = getTableData();
                    for (Object[] row : data) {
                        writer.update(row);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        loadFile.addActionListener((a) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("dat", "dat"));
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            chooser.setLocation(0, 0);

            int action = chooser.showOpenDialog(this);

            if (action == JFileChooser.APPROVE_OPTION) {
                List<Intellect> intelligence = null;

                try (ConReader reader = new ConReader(chooser.getSelectedFile().toPath())) {
                    intelligence = reader.intelligence();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // the file that has been chosen is not a valid conversation file.
                if (intelligence == null) {
                    JOptionPane.showMessageDialog(this,
                            "File selected is an invalid file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                } else {
                    int answer = JOptionPane.showConfirmDialog(this,
                            "Would you like to override your current rules or wipe out your existent rules to be" +
                                    " replaced by the loaded ones?", "Override", JOptionPane.YES_NO_OPTION);

                    /** If the user has chosen to override their current conversation file, then it will directly
                     *  load the new ones on top of them and save them.
                     */
                    if (answer == JOptionPane.YES_OPTION) {

                        updateIntelligence(model, intelligence);

                    } else if (answer == JOptionPane.NO_OPTION) {

                        /**
                         *  The operator has specified that they do not want their present conversation file
                         *  and wish to destroy it and add the newly loaded ones.
                         */
                        while (model.getRowCount() > 0)
                            model.removeRow(0);

                        updateIntelligence(model, intelligence);
                    }
                }
            }
    });

        rules.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                Object[][] data = getTableData();

                try {
                    ConWriter.delete();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try (ConWriter writer = new ConWriter()) {
                    for (Object[] details : data) {
                        writer.update(details);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });
    }


    /**
     * Removes the selected indices from the table and updates the master file.
     *
     * @param model the model of the intelligence table
     * @param indices the selected indices to be removed
     *
     * @throws IOException
     */
    private void removeElements(DefaultTableModel model, ArrayList<Integer> indices) throws IOException {

        // if all the rows have been deleted the master file will be deleted as it will be redundant. (empty)
        if (model.getRowCount() == 0) {
            ConWriter.delete();
            return;
        }

        /**
         *  breaking point of the recursion calls. Once all the selected indices have been deleted,
         *  the file will be deleted completely and reconstructed with the remaining data.
         */
        if (indices.size() == 0) {
            ConWriter.delete();
            try (ConWriter writer = new ConWriter()) {

                Object[] details = new Object[model.getColumnCount()];
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        details[j] = model.getValueAt(i, j);
                    }

                    writer.update(String.valueOf(details[0]), Boolean.valueOf(details[1].toString()),
                            String.valueOf(details[2]));
                }
                return;
            }
        }

        model.removeRow(indices.remove(0));

        /**
         *  All indices have to be decremented by one as the amount of rows has decremented by one.
         *  Failure in doing this step will cause an {@link ArrayIndexOutOfBoundsException}.
         **/
        for (int i = 0; i < indices.size(); i++) {
            indices.set(i, indices.get(i) - 1);
        }

        removeElements(model, indices);
    }


    /**
     * Acquires all the data from the table and holds them in a 2D Object Array.
     *
     * @return the 2D object array holding the data of the table.
     */
    private Object[][] getTableData() {
        Object[][] data = new Object[rules.getRowCount()][rules.getColumnCount()];

        for (int row = 0; row < rules.getRowCount(); row++) {
            for (int col = 0; col < rules.getColumnCount(); col++) {
                data[row][col] = rules.getValueAt(row, col);
            }
        }

        return data;
    }


    /**
     * Updates the intelligence master file and the table.
     *
     * @param model The table model.
     * @param intelligence The latest data provided.
     */
    private void updateIntelligence(DefaultTableModel model, List<Intellect> intelligence) {
        for (Intellect intellect : intelligence) {
            model.addRow(new Object[]{intellect.getInputText(), intellect.containsOnly(),
                    intellect.getOutputText()});
        }

        try {
            ConWriter.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ConWriter writer = new ConWriter()) {
            Object[][] data = getTableData();
            for (Object[] row : data) {
                writer.update(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        contains.setBounds(20, 140, 350, 50);

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
            try (ConWriter writer = new ConWriter()){
                writer.update(inputText.getText(), !contains.isSelected(), outputText.getText());
                DefaultTableModel model = (DefaultTableModel) rules.getModel();
                model.addRow(new Object[]{inputText.getText(), !contains.isSelected(), outputText.getText()});
                dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        cancel.addActionListener((a) -> dispose());
    }

    private String color(String text, String hex) {
        return "<span style='color:#" + hex + "'>" + text + "</span>";
    }

}