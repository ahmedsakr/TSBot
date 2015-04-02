package com.tsbot.io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
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


    /**
     * Default constructor. Calls the super constructor function for {@link BufferedWriter#BufferedWriter(Writer)}.
     * The FileWriter Constructor is passed true as value of for the parameter append, hence it's by default
     * that the file will be appended, and not re-written.
     *
     * @throws IOException
     */
    public InputIntelligenceWriter() throws IOException {
        super(new FileWriter(INPUT_INTELLIGENCE_LOCATION.toString(), true));
    }


    /**
     * Updates the file to hold a new record of a process. All data is converted to binary before it is stored.
     *
     * @param inputText the input text that the user will be required to input to trigger this process
     * @param contains the condition whether the inputText needs to be only in the original input or not.
     * @param outputText the output text of the process
     *
     * @throws IOException
     *
     * @see InputIntelligenceWriter#toBinary(String)
     */
    public void update(String inputText, boolean contains, String outputText) throws IOException {
        String binaryInput = toBinary("Input Text:" + inputText + "__");
        String binaryOutput = toBinary("Contains:" + contains + "__");
        String binaryContains = toBinary("Output Text:" + outputText);

        write(binaryInput + binaryOutput + binaryContains);
        newLine();
    }


    /**
     * If the file exists, then it will be deleted.
     * Static usage is crucially needed in this function. Constructing the object and calling this method object-wise
     * will throw a {@link java.nio.file.FileSystemException} as it will be in use by the writer.
     *
     * @throws IOException
     */
    public static void delete() throws IOException {
        Files.deleteIfExists(INPUT_INTELLIGENCE_LOCATION);
    }

    /**
     * Creates the file in the path {@link InputIntelligenceWriter#INPUT_INTELLIGENCE_LOCATION}.
     * Static usage is crucially needed in this function. Constructing the object and calling this method object-wise
     * will throw a {@link java.nio.file.FileSystemException} as it will be in use by the writer.
     *
     * @throws IOException
     */
    public static void create() throws IOException {
        Files.createFile(INPUT_INTELLIGENCE_LOCATION);
    }


    /**
     * Converts a string to binary sequence.
     *
     * @param decimal the string in question.
     *
     * @return the binary sequence
     */
    private String toBinary(String decimal) {
        StringBuilder builder = new StringBuilder();
        for (byte b : decimal.getBytes()) {
            builder.append(Integer.toBinaryString((int) b));
            builder.append(' ');
        }

        return builder.toString();
    }
}
