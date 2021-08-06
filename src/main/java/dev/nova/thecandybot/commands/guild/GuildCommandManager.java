package dev.nova.thecandybot.commands.guild;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.denyMessages.NoPermsRedEmbed;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.local.api.GuildAPI;
import dev.nova.thecandybot.local.api.MessageAPI;
import dev.nova.thecandybot.local.api.sql.SQLSelectWrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import dev.nova.thecandybot.commands.base.Command;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.sql.ResultSet;

public class GuildCommandManager extends Command {

    public GuildCommandManager() throws IllegalArgumentException {
        super("commandsManager", new String[]{"cmdManager","commandMgmt","cmdMgmt"}, "Control commands in your guild!", "Gives the administrators full control of the commands provided by the bot, disable, enable etc...", new Permission[]{Permission.ADMINISTRATOR}, new NoPermsRedEmbed(), null);
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        if(GuildAPI.createGuildTable(message.getGuild())) {
            try {
                ResultSet resultSet = new SQLSelectWrapper(Main.getGuildsDatabase(),"SELECT * FROM GUILD_"+message.getGuild().getId()+"_COMMANDS").get();
                MessageEmbed.Field[] fields = new MessageEmbed.Field[CommandManager.COMMANDS.size()];
                int index = 0;
                while(resultSet.next()) {
                    String commandName = resultSet.getString("COMMAND_NAME");
                    int enabled = resultSet.getInt("ENABLED");

                    if(enabled == 1){
                        fields[index] = new MessageEmbed.Field(commandName,"Enabled: Yes",false);
                    }else if(enabled == 0){
                        fields[index] = new MessageEmbed.Field(commandName,"Enabled: No",false);
                    }


                    index++;
                }

                message.getChannel().sendMessage(MessageAPI.getEmbed("Current Commands",new Color(179, 70, 101),fields)).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
