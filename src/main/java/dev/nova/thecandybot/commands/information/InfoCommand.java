package dev.nova.thecandybot.commands.information;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class InfoCommand extends Command {
    public InfoCommand() {
        super(
                "info",
                new String[]{"information", "help"},
                "Tells you about the bot and its functionalites",
                "Sends an embed in the text channel where it talls about the bot and how it wroks and how to invite it.",
                new Class[]{
                        CommandInfo.class
                }
        );
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {


        String n = "";
        for(Command command : CommandManager.COMMANDS){
            n = n + "- "+command.getName()+"\n";
        }
        message.getChannel().sendMessage(MessageAPI.getEmbed(
                "I am Sweet Candy bot",
                new Color(179, 70, 101),
                new MessageEmbed.Field[]{new MessageEmbed.Field("My Commands",n,false)}
        )).queue();
        return true;
    }
}
