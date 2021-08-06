package dev.nova.thecandybot.commands.base.denyMessages;

import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class NoPermsRedEmbed extends DenyMessage{
    public NoPermsRedEmbed() {
        super(MessageAPI.getEmbed(":no_entry_sign: You don't have permissions to execute this command!", Color.RED,null));
    }
}
