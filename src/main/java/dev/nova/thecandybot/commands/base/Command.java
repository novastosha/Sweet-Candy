package dev.nova.thecandybot.commands.base;

import dev.nova.thecandybot.commands.base.denyMessages.DenyMessage;
import dev.nova.thecandybot.local.api.GuildAPI;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

public abstract class Command {

    private final String name;
    private final String[] alias;
    private final String brief;
    private final String description;
    private final Permission[] permissions;
    private final Class<? extends Command>[] children;
    private final GuildAPI.Role[] requiredRoles;
    private final DenyMessage denyMessage;

    /**
     *
     * {@inheritDoc}
     *
     * Creates a command with permission(s) required to execute it.
     *
     * @param name Name of the command
     * @param alias Alias for the command
     * @param brief A small description about the command
     * @param description Full information about the command
     * @param permissions {@link net.dv8tion.jda.api.Permission} Permissions required to execute the command
     * @param denyMessage The message when the command will not be executed when requirements aren't met.
     * @param commandChildren {@link Command} The children of the command
     *
     */
    @SuppressWarnings({"unchecked"})
    public Command(String name, String[] alias, String brief, String description,
                   Permission[] permissions,DenyMessage denyMessage, Class<? extends Command>[] commandChildren) {
        this.name = name;
        this.alias = alias != null ? alias : new String[0];
        this.brief = brief;
        this.description = description;
        this.permissions = permissions != null ? permissions : new Permission[0];
        this.requiredRoles = null;
        this.children = commandChildren != null ? commandChildren : new Class[0];
        this.denyMessage = denyMessage;

    }

    /**
     *
     * {@inheritDoc}
     *
     * Creates a command with a role(s) required to execute it.
     *
     * @param name Name of the command
     * @param alias Alias for the command
     * @param brief A small description about the command
     * @param description Full information about the command
     * @param requiredRoles {@link dev.nova.thecandybot.local.api.GuildAPI.Role} Roles required to execute the command
     * @param denyMessage The message when the command will not be executed when requirements aren't met.
     * @param commandChildren {@link Command} The children of the command
     */
    @SuppressWarnings({"unchecked"})
    public Command(String name, String[] alias, String brief, String description, GuildAPI.Role[] requiredRoles, DenyMessage denyMessage, Class<? extends Command>[] commandChildren) {
        this.name = name;
        this.alias = alias != null ? alias : new String[0];
        this.brief = brief;
        this.permissions = null;
        this.description = description;
        this.requiredRoles = requiredRoles != null ? requiredRoles : new GuildAPI.Role[0];
        this.children = commandChildren != null ? commandChildren : new Class[0];
        this.denyMessage = denyMessage;

    }

    /**
     *
     * {@inheritDoc}
     *
     * Creates a command with no roles or permissions required to execute it.
     *
     * @param name Name of the command
     * @param alias Alias for the command
     * @param brief A small description about the command
     * @param description Full information about the command
     * @param commandChildren {@link Command} The children of the command
     *
     */
    @SuppressWarnings({"unchecked"})
    public Command(String name, String[] alias, String brief, String description, Class<? extends Command>[] commandChildren) {
        this.name = name;
        this.alias = alias != null ? alias : new String[0];
        this.brief = brief;
        this.permissions = null;
        this.description = description;
        this.requiredRoles = null;
        this.children = commandChildren != null ? commandChildren : new Class[0];
        this.denyMessage = null;
    }

    public DenyMessage getDenyMessage() {
        return denyMessage;
    }

    public GuildAPI.Role[] getRequiredRoles() {
        return requiredRoles;
    }

    public Class<? extends Command>[] getChildren() {
        return children;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    public String getBrief() {
        return brief;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public abstract boolean run(Member user, Message message, String[] args,TextChannel channel);

}
