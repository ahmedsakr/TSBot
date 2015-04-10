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

package com.tsbot.io.conversation;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Consists the Writing part of the I/O package for the conversation handling. Solely brought to life in the means of
 * updating the conversation file. All Data written is strictly in binary.
 *
 *
 * @author ahmad sakr
 * @since March 31, 2015.
 */
public class ConWriter extends BufferedWriter {

    public static final Path CONVERSATION_INTELLIGENCE_LOCATION = Paths.get(System.getProperty("user.home") +
            "/appdata/roaming/TSBot/Conversation.dat");


    /**
     * Default constructor. Calls the super constructor function for {@link BufferedWriter#BufferedWriter(Writer)}.
     * The FileWriter Constructor is passed true as value of for the parameter append, hence it's by default
     * that the file will be appended, and not re-written.
     *
     * @throws IOException
     */
    public ConWriter() throws IOException {
        super(new FileWriter(CONVERSATION_INTELLIGENCE_LOCATION.toString(), true));
    }


    public ConWriter(String path) throws IOException {
        super(new FileWriter(path, true));
    }


    /**
     * Updates the file to hold a new record of a process. All data is converted to binary before it is written
     * to the file. All data held by objects of {@link ConReader} are invalidated.
     *
     * @param inputText the input text that the user will be required to input to trigger this process
     * @param contains the condition whether the inputText needs to be only in the original input or not.
     * @param outputText the output text of the process
     *
     * @throws IOException
     *
     * @see ConWriter#toBinary(String)
     * @see ConReader#invalidate()
     */
    public void update(String inputText, boolean contains, String outputText) throws IOException {
        String binaryInput = toBinary("<input_text=\"" + inputText.replace("\t", " ") + "\"\t");
        String binaryOutput = toBinary("contains=\"" + contains + "\"\t");
        String binaryContains = toBinary("output_text=\"" + outputText.replace("\t", " ") + "\"\t>");

        write(binaryInput + binaryOutput + binaryContains);
        newLine();

        ConReader.invalidate();
    }


    public void update(Object[] details) throws IOException {
        update(details[0].toString(), Boolean.valueOf(details[1].toString()), details[2].toString());
    }


    /**
     * If the file exists, then it will be deleted. The data held by all objects of {@link ConReader} is
     * invalidated automatically upon call of this method.
     * Static usage is crucially needed in this function. Constructing the object and calling this method object-wise
     * will throw a {@link java.nio.file.FileSystemException} as it will be in use by the writer.
     *
     * @return if the file has been deleted or not.
     * @throws IOException
     *
     * @see ConReader#invalidate()
     */
    public static boolean delete() throws IOException {
        ConReader.invalidate();
        return Files.deleteIfExists(CONVERSATION_INTELLIGENCE_LOCATION);
    }

    /**
     * Creates the file in the path {@link ConWriter#CONVERSATION_INTELLIGENCE_LOCATION} if it does not exist.
     * Static usage is crucially needed in this function. Constructing the object and calling this method object-wise
     * will throw a {@link java.nio.file.FileSystemException} as it will be in use by the writer.
     *
     * @return The {@link Path} to a file if it has been created. Otherwise, null is returned. (file already exists)
     * @throws IOException
     */
    public static Path create() throws IOException {
        if (Files.exists(CONVERSATION_INTELLIGENCE_LOCATION))
            return null;

        return Files.createFile(CONVERSATION_INTELLIGENCE_LOCATION);
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
