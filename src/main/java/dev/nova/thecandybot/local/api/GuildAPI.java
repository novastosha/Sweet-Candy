package dev.nova.thecandybot.local.api;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.local.api.sql.SQLCreateInsertUpdateDeleteWrapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class GuildAPI {

    /**
     * This class was made because of custom assigned roles.
     */
    public enum Role {
        MODERATOR;

        public net.dv8tion.jda.api.entities.Role getRoleForGuild(Guild guild) {
            return null;
        }
    }

    public static boolean createGuildTable(Guild guild) {

        String rolesSql = "CREATE TABLE IF NOT EXISTS GUILD_"+guild.getId()+"_ROLES"+
                "(ROLE_ID TEXT PRIMARY KEY     NOT NULL);";

        String commandsSql = "CREATE TABLE IF NOT EXISTS GUILD_"+guild.getId()+"_COMMANDS"+
                "(COMMAND_NAME TEXT PRIMARY KEY     NOT NULL," +
                "ENABLED INT);";

        try {
            new SQLCreateInsertUpdateDeleteWrapper(Main.getGuildsDatabase(),rolesSql,commandsSql).pushAndClose();
        } catch (Exception e) {
            System.out.println("Failed to create guild table!");
            e.printStackTrace();
            return false;
        }
            for(Command command : CommandManager.COMMANDS) {
                String sql = "INSERT INTO GUILD_"+guild.getId()+"_COMMANDS (COMMAND_NAME , ENABLED)"+
                        " VALUES ('"+command.getName()+"' , "+1+");";
                    try {
                        new SQLCreateInsertUpdateDeleteWrapper(Main.getGuildsDatabase(), sql).pushAndClose();
                    }catch (Exception e) {
                        continue;
                    }
            }
            return true;

    }

}
