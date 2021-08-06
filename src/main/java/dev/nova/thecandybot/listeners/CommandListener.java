package dev.nova.thecandybot.listeners;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.denyMessages.DenyMessage;
import dev.nova.thecandybot.commands.base.denyMessages.NoPermsRedEmbed;
import dev.nova.thecandybot.local.api.GuildAPI;
import dev.nova.thecandybot.local.api.MessageAPI;
import dev.nova.thecandybot.local.api.sql.SQLSelectWrapper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.manager.CommandManager;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CommandListener extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String[] raw = event.getMessage().getContentDisplay().split(" ");
        try {
            String command = raw[0].substring(1);
            Command cmd = CommandManager.getByName(command);
            String[] args = CommandManager.getArgs(raw, 1);
            if (args.length != 0 && args[0].equals("")) {
                args = new String[0];
            }

            if (cmd == null) return;
            if(event.getMember() == null) return;
            if(GuildAPI.createGuildTable(event.getGuild())) {

                ResultSet resultSet = new SQLSelectWrapper(Main.getGuildsDatabase(), "SELECT COMMAND_NAME,ENABLED FROM GUILD_"+event.getGuild().getId()+ "_COMMANDS WHERE COMMAND_NAME = "+'"' +cmd.getName()+'"').get();
                int enabled = resultSet.getInt("ENABLED");

                if (enabled == 1) {

                    System.out.println(cmd.getPermissions() != null && cmd.getPermissions().length != 0 && event.getMember().getPermissions().containsAll(Arrays.asList(cmd.getPermissions())));

                    System.out.println();
                    if(cmd.getRequiredRoles() != null && cmd.getRequiredRoles().length != 0 || cmd.getPermissions() != null && cmd.getPermissions().length != 0 && !event.getMember().getPermissions().containsAll(Arrays.asList(cmd.getPermissions()))) {
                        if(cmd.getDenyMessage() != null) {
                            DenyMessage message = cmd.getDenyMessage();

                            if (message.getDenyMessageString() != null) {
                                event.getChannel().sendMessage(message.getDenyMessageString()).queue();
                            } else if (message.getDenyMessageEmbed() != null) {
                                event.getChannel().sendMessage(message.getDenyMessageEmbed()).queue();
                            }else{
                                event.getChannel().sendMessage(new NoPermsRedEmbed().getDenyMessageEmbed()).queue();
                            }

                        }
                        return;

                    }

                    cmd.run(event.getMember(), event.getMessage(), args,event.getChannel());
                } else {
                    event.getChannel().sendMessage(MessageAPI.getEmbed(":exclamation: This command is disabled!", new Color(200, 0, 0), null)).queue();
                }
            }else{
                event.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: There was an error while trying to create your guild table! Please try again!",new Color(200,0,0),null)).queue();
            }

            /* FIXME if(cmd.getPermissions() == null && !event.getGuild().getMembersWithRoles(cmd.getRequiredRoles()).contains(event.getMember())){
                Object deny = null;
                try {
                    deny =cmd.getDenyMessage();
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }

                if(deny instanceof MessageEmbed) {
                    event.getMessage().getChannel().sendMessage((MessageEmbed) cmd.getDenyMessage()).queue();
                }else if(deny instanceof String){
                    event.getMessage().getChannel().sendMessage((String) cmd.getDenyMessage()).queue();
                }
                return;
            }*/
        } catch (StringIndexOutOfBoundsException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
