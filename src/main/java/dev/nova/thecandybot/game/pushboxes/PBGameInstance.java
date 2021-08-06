package dev.nova.thecandybot.game.pushboxes;

import dev.nova.thecandybot.local.api.MessageAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;
import dev.nova.thecandybot.game.base.Game;
import dev.nova.thecandybot.game.base.GameInstance;

import java.awt.*;

public class PBGameInstance extends GameInstance {

    private Message message;
    private Grid grid;

    public PBGameInstance(Game game, Member member, TextChannel channel) {
        super(game,member, channel);
    }

    protected String getGrid(){
        return "ok";
    }

    @Override

    public void newGame() throws Grid.GridMakingException {
        this.grid = new Grid(1,1, Grid.Difficulty.EASY);;




        getChannel().sendMessage(MessageAPI.getEmbed("Pushboxes", Color.BLACK,new MessageEmbed.Field[]{
                new MessageEmbed.Field("Game", "", false)
        })).queue(message -> {
            message.addReaction("⬆").queue();
            message.addReaction("⬇").queue();
            message.addReaction("⬅").queue();
            message.addReaction("➡").queue();
            this.message = message;

        });
    }

    @Override
    public void endGame() {
        super.endGame();
    }


    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if(!event.isFromGuild()) return;
        if(event.getUser().isBot()) return;
        if(event.getMember().equals(getMember()) && message.getId().equals(event.getMessageId())){
            event.getReaction().removeReaction(event.getUser()).queue();
            switch (event.getReaction().getReactionEmote().getName()){
                /*case "➡":
                    grid.moveRight();
                    break;
                case "⬅":
                    grid.moveLeft();
                    break;
                case "⬆":
                    grid.moveUp();
                    break;*/
                case "⬇":
                        message.editMessage(MessageAPI.getEmbed("Pushboxes", Color.BLACK,new MessageEmbed.Field[]{
                                new MessageEmbed.Field("Game", getGrid(), false)
                        })).queue();
                    break;
            }
        }else if(!event.getMember().equals(getMember())){
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }



}
