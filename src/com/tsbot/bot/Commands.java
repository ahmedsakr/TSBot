package com.tsbot.bot;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.tsbot.io.InputIntelligenceReader;

import java.io.IOException;

/**
 *
 * @author ahmad sakr
 * @since March 28, 2015.
 */
public class Commands {

    private TS3Api api;
    private String botName;

    public Commands(TS3Api api, String botName) {
        this.api = api;
        this.botName = botName;
    }

    public void handleTextMessage(String username, String message) throws IOException {
        try (InputIntelligenceReader reader = new InputIntelligenceReader()) {
            reader.processes().stream().filter(process ->
                    message.replaceAll(botName, "##botname##").equalsIgnoreCase(process.getInputText()) ||
                            (message.replaceAll(botName, "##botname##").contains(process.getInputText()) &&
                                    process.containsOnly()))
                    .forEach(process ->

                            this.api.sendServerMessage(process.getOutputText().
                                    replaceAll("##botname##", botName).replaceAll("##name##", username)));
        }
    }


    public void handleLoginMessage(String username) {

    }


    public void handleLeaveMessage(String username) {

    }


    public void handleServerEdit(String username) {

    }

    public void handleChannelEdit(String username) {

    }

    public void handleChatDescriptionEdit(String username) {

    }

    public void handleClientMoved(String username) {

    }

    public void handleChannelCreation(String username) {

    }

    public void handleChannelDeleted(String username) {

    }

    public void handleChannelMoved(String username) {

    }

    public void handleChannelPasswordChange(String username) {

    }

}
