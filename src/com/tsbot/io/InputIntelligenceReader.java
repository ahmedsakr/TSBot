package com.tsbot.io;


import com.tsbot.bot.InputProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author Ahmad Sakr
 * @since March 31, 2015.
 */
public class InputIntelligenceReader extends BufferedReader {

    private ArrayList<InputProcess> processes;
    private static boolean updated;

    public static final Path INPUT_INTELLIGENCE_LOCATION = Paths.get(System.getProperty("user.home") +
            "/appdata/roaming/TSBot/InputIntelligence.dat");

    /**
     * Default constructor. Calls the super constructor of {@link BufferedReader}
     *
     * @throws IOException
     */
    public InputIntelligenceReader() throws IOException {
        super(new FileReader(INPUT_INTELLIGENCE_LOCATION.toString()));
    }

    /**
     * Connects to the file ({@link InputIntelligenceReader#INPUT_INTELLIGENCE_LOCATION}. Converts all binary input
     * to decimal, and then finally to String. Through the use of toInputProcess(String) function, an InputProcess
     * object is appended to the {@link InputIntelligenceReader#processes}.
     * <p>
     * Constant reading from the file is redundant, as it can lead to the same data if no other processes have been
     * deleted or added. In order to prevent this, a check is deployed to check whether the data in processes
     * is up-to-date or not. If it is update, then return {@link InputIntelligenceReader#processes} without
     * further inspection.
     *
     * @return {@link InputIntelligenceReader#processes}.
     * @throws IOException
     *
     * @see InputProcess
     * @see InputIntelligenceReader#toInputProcess(String)
     * @see InputIntelligenceReader#invalidate()
     * @see InputIntelligenceReader#isUpdated()
     */
    public ArrayList<InputProcess> processes() throws IOException {
        if (this.processes == null || !isUpdated()) {
            processes = new ArrayList<>();

            String currentLine;
            StringBuilder process = new StringBuilder();
            while ((currentLine = readLine()) != null) {
                String[] parts = currentLine.split(" ");
                for (String part : parts) {
                    int decimal = Integer.valueOf(toDecimal(part));
                    process.append(Character.toString((char) decimal));
                }

                processes.add(toInputProcess(process.toString()));
                process = new StringBuilder();
            }
        } else {
            return this.processes;
        }

        updated = true;
        return this.processes;
    }

    /**
     * Invalidates the data held in {@link InputIntelligenceReader#processes} and sets it to outdated.
     * Static usage is needed and optimal in this function for completely control over all objects of this class
     * and ease of invalidation without the need to construct the class.
     */
    public static void invalidate() {
        updated = false;
    }

    /**
     * Checks whether the data held by this object is updated and equal to the one in the file.
     *
     * @return true     if the data is updated.
     *         false    otherwise.
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Converts the String object process to an object of type {@link InputProcess}.
     *
     * @param process the data to be used for construction of the object of type {@link InputProcess}.
     *
     * @return an {@link InputProcess} Object
     */
    private InputProcess toInputProcess(String process) {
        String[] details = process.split("__", 3);
        String[] input = details[0].split(":");
        String[] contains = details[1].split(":");
        String[] output = details[2].split(":");

        return new InputProcess(input[1], Boolean.valueOf(contains[1]), output[1]);
    }

    /**
     * Converts a binary sequence into decimal.
     *
     * @param binary the binary sequence
     * @return the decimal value of the binary sequence.
     */
    private String toDecimal(String binary) {
        return Integer.parseInt(binary, 2) + "";
    }

}
