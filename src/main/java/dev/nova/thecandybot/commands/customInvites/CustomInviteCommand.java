package dev.nova.thecandybot.commands.customInvites;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.base.denyMessages.NoPermsRedEmbed;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.entities.InviteImpl;
import net.dv8tion.jda.internal.handle.InviteCreateHandler;

import java.awt.*;

public class CustomInviteCommand extends Command {
    public CustomInviteCommand() throws IllegalArgumentException {
        super("customInvite", null, "Creates a custom invite with custom actions", "Creates an invite for a specific channel with actions on join with that invite", new Permission[]{Permission.ADMINISTRATOR}, new NoPermsRedEmbed(), null);
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {

        Invite invite = channel.createInvite().complete();

        channel.sendMessage(MessageAPI.getEmbed("Created an invite!", Color.PINK,new MessageEmbed.Field[]{
            new MessageEmbed.Field("Code",invite.getCode(),false)
        })).queue();

        channel.sendMessage(MessageAPI.getEmbed(":warning: To change actions of the invite, you must not delete it and use the command: !customInvite control 'CODE'",Color.YELLOW,null)).queue();

        return true;
    }
}
