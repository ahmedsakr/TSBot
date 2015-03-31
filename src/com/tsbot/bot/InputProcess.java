package com.tsbot.bot;


/**
 *
 * @author Ahmad Sakr
 * @since March 31, 2015.
 */
public class InputProcess {

    private String input, output;
    private boolean contains;

    public InputProcess(String input,boolean contains, String output) {
        this.input = input;
        this.output = output;
        this.contains = contains;
    }


    public String getInputText() {
        return input;
    }

    public String getOutputText() {
        return output;
    }

    public boolean containsOnly() {
        return contains;
    }

}
