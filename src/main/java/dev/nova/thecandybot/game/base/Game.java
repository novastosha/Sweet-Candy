package dev.nova.thecandybot.game.base;

import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Game extends Command {


    public static final ArrayList<Game> GAMES = new ArrayList<>();

    public final HashMap<Member,GameInstance> instances;

    private final Class<? extends GameInstance> gameInstanceClass;

    public Game(String codeName, String gameBrief, String gameDescription, Class<? extends GameInstance> gameInstanceClass){
        super(codeName,new String[]{},gameBrief,gameDescription,null);
        this.gameInstanceClass = gameInstanceClass;
        this.instances = new HashMap<>();
    }

    public Class<? extends GameInstance> getGameInstanceClass() {
        return gameInstanceClass;
    }

    public void createInstance(Game game, @Nonnull Member member, @Nonnull TextChannel channel){
        try {

            if(gameInstanceClass == null){
                System.out.println("[GAMES] Unable to create game instance for game: "+getName());
                channel.sendMessage(MessageAPI.getEmbed(":warning: Unable to create a game instance of this game!",new Color(217, 217,0),new MessageEmbed.Field[]{
                        new MessageEmbed.Field("**Reason**","Game instance class is null!",false)
                })).queue();
                return;
            }
            Constructor<? extends GameInstance> constructor = gameInstanceClass.getConstructor(Game.class,Member.class,TextChannel.class);

            try {
                GameInstance instance = constructor.newInstance(game,member,channel);
                try {
                    instance.newGame();
                }catch (Exception e){
                    this.forceEndInstance(instance,e,channel,member);
                }
                instances.put(member,instance);
            } catch (InstantiationException | InvocationTargetException e) {
                System.out.println("[GAMES] Unable to create game instance for game: "+getName());
                channel.sendMessage(MessageAPI.getEmbed(":warning: Unable to create a game instance of this game!",new Color(217, 217,0),null)).queue();
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println("[GAMES] Unable to create game instance for game: "+getName());
                channel.sendMessage(MessageAPI.getEmbed(":warning: Unable to create a game instance of this game!",new Color(217, 217,0),new MessageEmbed.Field[]{
                        new MessageEmbed.Field("**Reason**","Found constructor but the system could not access it, make sure its 'public'",false),
                        new MessageEmbed.Field("**Game instance class**",gameInstanceClass.getName(),false)
                })).queue();
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            System.out.println("[GAMES] Unable to create game instance for game: "+getName());
            channel.sendMessage(MessageAPI.getEmbed(":warning: Unable to create a game instance of this game!",new Color(217, 217,0),new MessageEmbed.Field[]{
                    new MessageEmbed.Field("**Reason**","No constructor matching required parameter types!",false),
                    new MessageEmbed.Field("**Game instance class**",gameInstanceClass.getName(),false)
            })).queue();
            e.printStackTrace();
        }
    }

    public void forceEndInstance(GameInstance instance, Exception e, TextChannel message,Member member) {
        System.out.println("[GAMES] "+instance+" has been forcibly ended due to an exception!");
        e.printStackTrace();
        message.sendMessage(MessageAPI.getEmbed(":no_entry_sign: An unexpected error occurred that caused your game to end. \n Sorry "+instance.getMember().getUser().getName(),new Color(200,0,0),null)).queue();
        instances.remove(member);

    }

    @Override
    public boolean run(Member user, Message message, String[] args, TextChannel channel) {
        if(!instances.containsKey(user)) {
            createInstance(this, user, message.getTextChannel());
        }else{
            message.getChannel().sendMessage(MessageAPI.getEmbed(":no_entry_sign: You can't start a game until you finish your current one.",new Color(200,0,0),null)).queue();
        }
        return true;
    }
}
