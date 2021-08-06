package dev.nova.thecandybot.commands.guild;

import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.base.denyMessages.NoPermsRedEmbed;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ModeratorRoleCommand extends Command {
    public ModeratorRoleCommand() throws IllegalArgumentException {
        super(
                "moderatorRole",
                new String[]{"modRole"},
                "Sets a role as a moderator role",
                "Binds the custom attribute 'mod' to the role ID so the role can have moderator permissions.",
                new Permission[]{Permission.MESSAGE_MANAGE},
                new NoPermsRedEmbed(),
                null
        );
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        return false;
    }
}
