package dev.nova.thecandybot.commands.moderation;

import dev.nova.thecandybot.commands.base.denyMessages.DenyMessage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BanCommand extends Command {

    public BanCommand() {
        super(
                "ban",
                null,
                "Ban a user from this server",
                "Makes the user unable to join this guild permanently (only if an admin manually unbans him)",
                new Permission[]{
                    Permission.BAN_MEMBERS
                },
                new DenyMessage(MessageAPI.getEmbed(":no_entry_sign: You don't have permission to ban members!",new Color(200,0,0),null)),
                null
            );
    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {

            if (message.getMentionedMembers().size() == 0) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You didn't mention any user!", new Color(200, 0, 0), null)).queue();
                return false;
            } else if (message.getMentionedMembers().size() > 1) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":head_bandage: You mentioned so many users!", new Color(200, 0, 0), null)).queue();
            }


            Member toBan = message.getMentionedMembers().get(0);
            int lo = 1;
            if (toBan.getNickname() != null) {
                lo = toBan.getNickname().split(" ").length;
            } else {
                lo = toBan.getEffectiveName().split(" ").length;
            }

            if (args.length == 1) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: Please provide a reason!", new Color(200, 0, 0), null)).queue();
                return false;
            }


            if (toBan.getUser().equals(message.getAuthor())) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't ban your self!", new Color(200, 0, 0), null)).queue();
                return false;
            }

            if (toBan.isOwner()) {
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't ban the server owner!", new Color(200, 0, 0), null)).queue();
                toBan.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(MessageAPI.getEmbed(":warning: " + user.getUser().getName() + " tried to ban you!", new Color(231, 205, 0), null)).queue();
                });
                return false;
            }

            if(!user.canInteract(toBan)){
                message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't ban: "+toBan.getUser().getName(), new Color(200, 0, 0), null)).queue();
                return false;
            }


            message.getChannel().sendMessage(MessageAPI.getEmbed(":white_check_mark: " + toBan.getEffectiveName() + " has been banned!", new Color(0, 200, 0), null)).queue();
            int finalLo = lo;
            toBan.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(MessageAPI.getEmbed(":warning: You have been banned from: " + message.getGuild().getName(), new Color(231, 205, 0), new MessageEmbed.Field[]{
                        new MessageEmbed.Field("**Reason**", MessageAPI.combineArgs(finalLo + 1, args), false),
                        new MessageEmbed.Field("**Ban Issuer**", user.getUser().getName(), false)
                })).queue();
            });

            toBan.ban(1)
                    .reason(MessageAPI.combineArgs(finalLo + 1, args))
                    .queueAfter(2, TimeUnit.SECONDS);
            return true;
        }
}
