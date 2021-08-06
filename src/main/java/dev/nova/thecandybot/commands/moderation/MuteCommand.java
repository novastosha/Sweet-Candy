package dev.nova.thecandybot.commands.moderation;

import dev.nova.thecandybot.commands.base.denyMessages.DenyMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class MuteCommand extends Command {

    public MuteCommand(){
        super(
                "mute",
                new String[]{"tempMute"},
                "Mutes a member of this guild!",
                "Applies the 'Muted' role to the user so he wouldn't be able to chat after a certain period of time",
                new Permission[]{Permission.MESSAGE_MANAGE},
                new DenyMessage(MessageAPI.getEmbed(":no_entry_sign: You don't have permission to mute members!",new Color(200,0,0),null)),
                null
        );
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        if(message.getMentionedMembers().size() == 0){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You didn't mention any user!",new Color(200,0,0),null)).queue();
            return false;
        }else if (message.getMentionedMembers().size() > 1) {
            message.getChannel().sendMessage(MessageAPI.getEmbed(":head_bandage: You mentioned so many users!",new Color(200,0,0),null)).queue();
        }


        Member toBan = message.getMentionedMembers().get(0);
        int lo = 1;
        if(toBan.getNickname() != null){
            lo = toBan.getNickname().split(" ").length;
        }else {
            lo = toBan.getEffectiveName().split(" ").length;
        }

        if(args.length == 1){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: Please provide a reason!",new Color(200,0,0),null)).queue();
            return false;
        }

        if(toBan.getUser().equals(message.getAuthor())){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't ban your self!",new Color(200,0,0),null)).queue();
            return false;
        }

        if(toBan.isOwner()){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't mute the server owner!",new Color(200,0,0),null)).queue();
            toBan.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(MessageAPI.getEmbed(":warning: "+user.getUser().getName()+" tried to mute you!",new Color(231, 205, 0),null)).queue();
            });
            return false;
        }

        if(!user.canInteract(toBan)){
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't mute: "+toBan.getUser().getName(), new Color(200, 0, 0), null)).queue();
            return false;
        }


        boolean exists = false;
        Role[] roles = new Role[]{null};
        for(Role r : message.getGuild().getRoles()) {
            if(r.getName().equals("Muted_SweetCandy")){
                exists = true;
                roles[0] = r;
            }
        }


        if(!exists) {
            message.getGuild().createRole()
                    .setName("Muted_SweetCandy")
                    .setPermissions(Permission.EMPTY_PERMISSIONS)
                    .setMentionable(false)
                    .queue(r -> {
                        roles[0] = r;
                    });
        }

        toBan.getGuild().addRoleToMember(toBan,roles[0]).queue();

        return true;
    }
}
