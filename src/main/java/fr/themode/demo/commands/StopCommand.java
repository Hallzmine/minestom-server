package fr.themode.demo.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class StopCommand implements CommandProcessor {
    @Override
    public String getCommandName() {
        return "stop";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean process(CommandSender sender, String command, String[] args) {
        if(hasAccess(sender.asPlayer()) == true) {
            MinecraftServer.stopCleanly();
            return true;
        } else {
            sender.sendMessage("You do not have the required permissions!");
            return false;
        }
    }

    @Override
    public boolean hasAccess(Player player) {
        return player.getPermissionLevel() >= 4;
    }
}
