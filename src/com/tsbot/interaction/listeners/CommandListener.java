package com.tsbot.interaction.listeners;


import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.tsbot.interaction.Commands;

import java.io.IOException;


/**
 *
 * @author ahmad sakr
 * @since March 28, 2015.
 */
public class CommandListener implements TS3Listener {

    private Commands commands;
    private String botNickname;

    public CommandListener(TS3Api api, String botNickname) {
        this.botNickname = botNickname;
        commands = new Commands(api, botNickname);
    }


    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        if (e.getTargetMode() == TextMessageTargetMode.SERVER) {
            try {
                commands.handleTextMessage(e.getInvokerName(), e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleLoginMessage(e.getInvokerName());

    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleLeaveMessage(e.getInvokerName());
    }

    @Override
    public void onServerEdit(ServerEditedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleServerEdit(e.getInvokerName());
    }

    @Override
    public void onChannelEdit(ChannelEditedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChannelEdit(e.getInvokerName());
    }

    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChatDescriptionEdit(e.getInvokerName());
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleClientMoved(e.getInvokerName());
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChannelCreation(e.getInvokerName());
    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChannelDeleted(e.getInvokerName());
    }

    @Override
    public void onChannelMoved(ChannelMovedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChannelMoved(e.getInvokerName());
    }

    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
        if (e.getInvokerName().equalsIgnoreCase(botNickname))
            return;

        commands.handleChannelPasswordChange(e.getInvokerName());
    }
}
