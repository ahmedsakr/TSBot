package com.tsbot.io;


import com.tsbot.bot.InputProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ahmad Sakr
 * @since March 31, 2015.
 */
public class InputIntelligenceReader extends BufferedReader {

    public static final Path INPUT_INTELLIGENCE_LOCATION = Paths.get(System.getProperty("user.home") +
            "/appdata/roaming/TSBot/InputIntelligence.dat");

    public InputIntelligenceReader() throws IOException {
        super(new FileReader(INPUT_INTELLIGENCE_LOCATION.toString()));
    }


    public ArrayList<InputProcess> processes() throws IOException {
        ArrayList<InputProcess> processes = new ArrayList<>();
        String currentLine;
        StringBuilder process = new StringBuilder();
        while ((currentLine = readLine()) != null) {
            String[] parts = currentLine.split(" ");
            for (String part : parts) {
                int decimal = Integer.valueOf(toDecimal(part));
                process.append(Character.toString((char) decimal));
            }

            processes.add(getInputProcess(process.toString()));
            process = new StringBuilder();
        }

        return processes;
    }


    private InputProcess getInputProcess(String process) {
        String[] details = process.split("__", 3);
        String[] input = details[0].split(":");
        String[] contains = details[1].split(":");
        String[] output = details[2].split(":");

        return new InputProcess(input[1], Boolean.valueOf(contains[1]), output[1]);
    }

    private String toDecimal(String binary) {
        return Integer.parseInt(binary, 2) + "";
    }

}
