package com.tsbot.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author ahmad sakr
 * @since March 31, 2015.
 */
public class InputIntelligenceWriter extends BufferedWriter {

    public static final Path INPUT_INTELLIGENCE_LOCATION = Paths.get(System.getProperty("user.home") +
            "/appdata/roaming/TSBot/InputIntelligence.dat");

    public InputIntelligenceWriter() throws IOException {
        super(new FileWriter(INPUT_INTELLIGENCE_LOCATION.toString(), true));
    }

    public void update(String inputText, String outputText, boolean contains) throws IOException {
        String binaryInput = toBinary("Input Text:" + inputText + "__");
        String binaryOutput = toBinary("Contains:" + contains + "__");
        String binaryContains = toBinary("Output Text:" + outputText);

        write(binaryInput + binaryOutput + binaryContains);
        newLine();
    }


    private String toBinary(String decimal) {
        StringBuilder builder = new StringBuilder();
        for (byte b : decimal.getBytes()) {
            builder.append(Integer.toBinaryString((int) b));
            builder.append(' ');
        }

        return builder.toString();
    }
}
