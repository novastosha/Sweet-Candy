package dev.nova.thecandybot.commands.manager;

import dev.nova.thecandybot.commands.base.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public static final List<Command> COMMANDS = new ArrayList<>();

    public static void loadCommand(Command command){
        if(getByName(command.getName()) == null) {
            COMMANDS.add(command);
            System.out.println("[COMMANDS] Added: " + command.getName() + " (" + command.getClass().getSimpleName() + ") as a command!");
        }
    }

    public static String[] getArgs(String[] raw, int i) {

        StringBuilder str = new StringBuilder();
        for( int l = i; l < raw.length; l++ ) {
            if(l != raw.length-1) {
                str.append(raw[l] + " ");
            }else{
                str.append(raw[l]);
            }
        }

        return str.toString().trim().split(" ");

    }

    public static Command getByName(String command){
        for(Command c : COMMANDS){
            if(c.getName().equals(command)) return c;
            else{
                for(String alias : c.getAlias()){
                    if(alias.equals(command)) return c;
                }
            }
        }
        return null;
    }


}
