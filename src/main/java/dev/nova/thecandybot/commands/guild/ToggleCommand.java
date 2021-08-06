package dev.nova.thecandybot.commands.guild;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.base.denyMessages.DenyMessage;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.local.api.GuildAPI;
import dev.nova.thecandybot.local.api.MessageAPI;
import dev.nova.thecandybot.local.api.sql.SQLCreateInsertUpdateDeleteWrapper;
import dev.nova.thecandybot.local.api.sql.SQLSelectWrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.ResultSet;

public class ToggleCommand extends Command {

    public ToggleCommand() throws IllegalArgumentException {
        super("toggleCommand", new String[]{"toggleCmd"}, "Disables a command for this guild", "", new Permission[]{Permission.ADMINISTRATOR}, new DenyMessage(MessageAPI.getEmbed(":no_entry_sign: You don't have permission to disable commands",new Color(200,0,0),null)), null);
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        if(GuildAPI.createGuildTable(message.getGuild())){
            if(args.length == 0) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: Please provide a command",new Color(200,0,0),null)).queue();
                return true;
            }

            Command command = CommandManager.getByName(args[0]);

            if(command == null) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":thinking: Unknown command!",new Color(224, 202, 0),null)).queue();
                return true;
            }

            if(command == this){
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You cannot toggle this command on or off!",new Color(200,0,0),null)).queue();
                return true;
            }

            try {
                ResultSet resultSet = new SQLSelectWrapper(Main.getGuildsDatabase(),"SELECT COMMAND_NAME,ENABLED FROM GUILD_"+message.getGuild().getId()+ "_COMMANDS WHERE COMMAND_NAME = "+'"' +command.getName()+'"').get();
                int enabled = resultSet.getInt("ENABLED");

                if(enabled == 1){
                    message.getChannel().sendMessage(MessageAPI.getEmbed(":white_check_mark: The command has been disabled!",new Color(0,150,0),null)).queue();
                    new SQLCreateInsertUpdateDeleteWrapper(Main.getGuildsDatabase(),"UPDATE GUILD_"+message.getGuild().getId()+"_COMMANDS set ENABLED = 0 where COMMAND_NAME = "+'"'+command.getName()+'"'+";").pushAndClose();

                } else if(enabled == 0){
                    message.getChannel().sendMessage(MessageAPI.getEmbed(":white_check_mark: The command has been enabled!",new Color(0,150,0),null)).queue();
                    new SQLCreateInsertUpdateDeleteWrapper(Main.getGuildsDatabase(),"UPDATE GUILD_"+message.getGuild().getId()+"_COMMANDS set ENABLED = 1 where COMMAND_NAME = "+'"'+command.getName()+'"'+";").pushAndClose();
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: There was an error while trying to request to the database! Please try again!",new Color(200,0,0),null)).queue();
            }
        }else{
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: An error occurred while creating guild database!",new Color(200,0,0),null)).queue();
        }
        return true;
    }
}
