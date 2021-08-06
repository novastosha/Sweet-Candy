package dev.nova.thecandybot;

import dev.nova.thecandybot.commands.customInvites.CustomInviteCommand;
import dev.nova.thecandybot.commands.guild.ToggleCommand;
import dev.nova.thecandybot.commands.information.CommandInfo;
import dev.nova.thecandybot.commands.information.InfoCommand;
import dev.nova.thecandybot.commands.moderation.BanCommand;
import dev.nova.thecandybot.commands.moderation.MuteCommand;
import dev.nova.thecandybot.listeners.CommandListener;
import dev.nova.thecandybot.listeners.console.ConsoleEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.commands.guild.GuildCommandManager;
import dev.nova.thecandybot.commands.manager.CommandManager;
import dev.nova.thecandybot.game.base.Game;
import dev.nova.thecandybot.game.pushboxes.PBGame;


import javax.security.auth.login.LoginException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

public class Main {

    public static JDA JDA;
    private static JDABuilder builder;
    private static int connectionTries = 1;
    private static Connection guildsDatabase;

    public static Connection getGuildsDatabase() {
        return guildsDatabase;
    }

    public static void main(String[] args) throws LoginException {
        try{

            //Database connection

            connect();

            if(guildsDatabase == null) {
                System.out.println("[DATABASE] Unable to connect to guilds database");
                System.exit(255);
            }

            // Directories creation

            new File("guilds").mkdirs();

            // Events, commands and games registeration

            registerCommands(
                    new BanCommand(),
                    new InfoCommand(),
                    new MuteCommand(),
                    new CommandInfo(),
                    new GuildCommandManager(),
                    new ToggleCommand(),
                    new CustomInviteCommand()
            );

            registerGames(
                    new PBGame()
            );


            ListenerAdapter[] listeners = new ListenerAdapter[]{
                    new CommandListener(),
                    new ConsoleEvents(),
            };


            // JDA bot init

            builder = JDABuilder.createDefault("ODcyNTk1ODc4MzI4ODExNTgw.YQsKPQ.-FpiOsT7cXpCPgOT0cV0Ua8KnN4");
            builder.setActivity(Activity.watching("I being developed..."));

            initBot(builder);
            registerEvents(listeners);



        } catch (LoginException e) {
            System.out.println("Unable to login!");
            System.exit(1);
        } catch (ErrorResponseException e){
            System.out.println("Could not connect to discord.com, retrying!");
            initBot(builder);
        }


    }

    /**
     * Connects to all databases neccessary.
     */
    private static void connect() {
        new File("guilds").mkdirs();
        connectionTries++;

        try {

            //Guilds databse
            guildsDatabase = DriverManager.getConnection("jdbc:sqlite:guilds/guildDB.db");
            guildsDatabase.setAutoCommit(false);


            connectionTries = 1;
            System.out.println("[DATABASE] Connected to database!");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("[DATABASE] Unable to connect to database!");
            if(connectionTries <= 3) {
                System.out.println("[DATABASE] Trying to reconnect!");

                connect();
            }else{
                System.out.println("[DATABASE] Unable to connect to database after 3 tries, Exiting!");

                System.gc();
                System.exit(255);
            }


        }

    }

    private static void initBot(JDABuilder builder) throws LoginException {
        JDA = builder.build();
    }

    private static void registerGames(Game... games) {
        Arrays.stream(games).forEach(game -> {
            registerCommands(game);
            Game.GAMES.add(game);
            System.out.println("[GAMES] Game: "+game.getName()+ " ("+game.getClass().getSimpleName()+")"+" has been registered!");
        });
    }


    public static void registerCommands(Command... commands) {
        Arrays.stream(commands).forEach(CommandManager::loadCommand);

    }
    
    public static void registerEvents(ListenerAdapter... events){
        for(ListenerAdapter listenerAdapter : events){

            JDA.addEventListener(listenerAdapter);
            System.out.println("[EVENTS] "+listenerAdapter.getClass().getSimpleName()+" has been added as a event listener!");
        }
    }

}
