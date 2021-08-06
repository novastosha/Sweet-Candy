package dev.nova.thecandybot.listeners.console;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ConsoleEvents extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;
        /*if(event.isFromType(ChannelType.PRIVATE)){
            ConsolePrivateRespond.interactRespondPrivate(event);
        }*/
    }
}
