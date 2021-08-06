package dev.nova.thecandybot.commands.information;

import dev.nova.thecandybot.local.api.GuildAPI;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class CommandInfo extends Command {

    public CommandInfo() throws IllegalArgumentException {
        super("commandinfo", new String[]{"cmdinfo","command-info","cmd-info","cmdinformation","commandinformation","command-information","cmd-information"}, "Tells you about a command", "Tells you about a command!", null);
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        if(args.length == 0){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: Please specify a command!",new Color(200,0,0),null)).queue();
            return false;
        }
        if(args.length == 1) {
            Command command = CommandManager.getByName(args[0]);
            if (command == null) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":thinking: Unknown command!", new Color(217, 217, 0), null)).queue();
                return false;
            }
            String n = "";
            for (String alias : command.getAlias()) {
                n = n + "- " + alias + "\n";
            }

            String p = "";
            if (command.getPermissions() != null) {
                for (Permission permission : command.getPermissions()) {
                    p = p + "- " + permission.getName() + "\n";
                }
            }


            String role = "";
            if(command.getRequiredRoles() != null){
                for (GuildAPI.Role role1 : command.getRequiredRoles()) {
                    role = role + "- " + role1.getRoleForGuild(user.getGuild()).getAsMention() + "\n";
                }
            }

            String children = "";
            if(command.getChildren() != null) {

                for (Class clazz : command.getChildren()) {
                    children = children + "- " + clazz.getSimpleName() + "\n";
                }
            }

            message.getChannel().sendMessage(MessageAPI.getEmbed("Command info of: "+command.getName(),new Color(0,0,0),new MessageEmbed.Field[]{
                    new MessageEmbed.Field("Alias",n,false),
                    new MessageEmbed.Field("Permissions",p,false),
                    new MessageEmbed.Field("Roles Required",role,false),
                    new MessageEmbed.Field("Command's children",children,false),
                    new MessageEmbed.Field("Brief",command.getBrief(),true),
                    new MessageEmbed.Field("Description",command.getDescription(),true),

            })).queue();
        }else{
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: Way too many arguments!",new Color(200,0,0),null)).queue();
            return false;
        }
        return true;
    }
}
