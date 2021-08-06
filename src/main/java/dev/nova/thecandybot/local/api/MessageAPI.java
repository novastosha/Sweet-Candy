package dev.nova.thecandybot.local.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

public class MessageAPI {
    public static MessageEmbed getEmbed(String title, Color color, MessageEmbed.Field[] fields) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(color);
        embed.setTitle(title);
        if(fields != null) {
            for (MessageEmbed.Field field : fields) {
                embed.addField(field);
            }
        }

        return embed.build();

    }

    public static String combineArgs(int i, String[] args) {
        StringBuffer sb = new StringBuffer();
        for(int il = 0; il < args.length; il++) {
            if(il >= i) {
                if(il != args.length - 1) {
                    sb.append(args[il] + " ");
                }else {
                    sb.append(args[il]);
                }
            }
        }
        return sb.toString();

    }
}
