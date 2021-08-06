package dev.nova.thecandybot.game.base;

import dev.nova.thecandybot.Main;
import dev.nova.thecandybot.commands.base.Command;
import dev.nova.thecandybot.game.pushboxes.Grid;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GameInstance extends ListenerAdapter {

    private final Member member;
    private final TextChannel channel;
    private final Game game;

    public GameInstance(Game game, Member member, TextChannel channel, Command... commands){
        this.member = member;
        this.game = game;
        this.channel = channel;
        Main.registerCommands(commands);
        Main.registerEvents(this);
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Game getGame() {
        return game;
    }

    public Member getMember() {
        return member;
    }

    public abstract void newGame() throws Grid.GridMakingException;

    @OverridingMethodsMustInvokeSuper
    public void endGame(){
        game.instances.remove(member);
    }
}
