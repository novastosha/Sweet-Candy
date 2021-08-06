package dev.nova.thecandybot.commands.adminmail;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import dev.nova.thecandybot.commands.base.Command;

public class AdminMail extends Command {
    public AdminMail() {
        super("adminmail", new String[]{"am","adminm","amail"}, "Send messages to admins", "Send a message to admins from a specific guild", null);
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {


        return true;
    }

    public static class MessageListener extends ListenerAdapter {

    }
}
