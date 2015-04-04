package com.tsbot.bot;


/**
 *
 * @author Ahmad Sakr
 * @since March 31, 2015.
 */
public class Intellect {

    private String input, output;
    private boolean contains;


    /**
     * Assigns all parameters to their corresponding ones.
     *
     * @param input the input text
     * @param contains the contains condition
     * @param output the output text
     */
    public Intellect(String input, boolean contains, String output) {
        this.input = input;
        this.output = output;
        this.contains = contains;
    }

    /**
     *
     * @return the input text.
     */
    public String getInputText() {
        return input;
    }

    /**
     *
     * @return the output text.
     */
    public String getOutputText() {
        return output;
    }


    /**
     *
     * @return the contains condition.
     */
    public boolean containsOnly() {
        return contains;
    }
}
