/**
 * Copyright (c) 2014 Ahmad Sakr
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

package com.tsbot.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.JTextComponent;

/**
 *
 * @author ahmad sakr (http://ahmadsakr.me)
 */
public class GhostText implements FocusListener, KeyListener {

    private String text;
    private JTextComponent field;
    private Color oldForeground;
    private Color ghostColor = new Color(0,0,0);

    
    /**
     * Default constructor.
     *
     * @param ghostText the text that will inherit ghost properties, Disappearing on click & gray background
     *                  when not focused
     * @param field     the field to work on
     */
    public GhostText(String ghostText, JTextComponent field) {
        super();
        this.text = ghostText;
        this.field = field;
        field.addFocusListener(this);
        field.addKeyListener(this);

        // saving the old Foreground of the field before changing it for later use.
        oldForeground = this.field.getForeground();
        focusLost(null);
    }


    /**
     * If the user clicked on the field, this will execute the block of code inside of it.
     * In this case, removal of ghost text as user will start typing and changing the oldForeground back to the one before.
     *
     * @param e the focus event
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (field.getText().equalsIgnoreCase(text)) {
            field.setForeground(ghostColor);
            field.setCaretPosition(0);
        }
    }


    /**
     * If the user lost focus on the field, a condition has to be tested. If the user did not write anything in
     * the field, then the ghost text will be visible once again with the gray oldForeground,
     * else the text will not be revived.
     *
     * @param e the focus event
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (field.getText().isEmpty()) {
            field.setForeground(ghostColor);
            field.setText(text);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    /**
     * Detects any action done by the user that triggers a condition. If the user started to type
     * and the only text is the ghost text specified, then it will restore the old foreground and
     * set text to nothing.
     * Detects whether the user is trying to delete the ghost text without adding any text. It will
     * disallow the user.
     *
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (field.getText().equalsIgnoreCase(text)) {
            field.setText("");
            field.setForeground(oldForeground);
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (field.getText().equalsIgnoreCase(text)) {
                field.setText("");
            }
        }
    }


    /**
     * if the text is empty, the ghost text and the foreground are restored. Please note:
     * the return in the first if statement is crucial, because if the method does not return,
     * it will also go to the other if statement and pass the condition as it's already empty.
     *
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (field.getText().isEmpty()) {
            field.setText(text);
            field.setForeground(ghostColor);
            field.setCaretPosition(0);
        }
    }
}