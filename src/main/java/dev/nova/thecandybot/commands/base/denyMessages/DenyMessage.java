package dev.nova.thecandybot.commands.base.denyMessages;

import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nullable;

public class DenyMessage {

    @Nullable
    private final String denyMessageString;

    @Nullable
    private final MessageEmbed denyMessageEmbed;

    public DenyMessage(String denyMessage) {
        this.denyMessageString = denyMessage;
        this.denyMessageEmbed = null;
    }

    public DenyMessage(MessageEmbed denyMessage){
        this.denyMessageEmbed = denyMessage;
        this.denyMessageString = null;
    }

    @Nullable
    public MessageEmbed getDenyMessageEmbed() {
        return denyMessageEmbed;
    }

    @Nullable
    public String getDenyMessageString() {
        return denyMessageString;
    }
}
