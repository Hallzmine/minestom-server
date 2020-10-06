package fr.themode.demo.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class DataGetCommand extends Command {

    //I don't think this works, needs to be removed
    public DataGetCommand() {
        super("data");
        addSyntax(this::execute);
    }

    private void execute(CommandSender sender, Arguments args) {
        Player player = (Player) sender;
        String nbt = player.getInventory().getItemInMainHand().toString();
        sender.sendMessage(nbt);
    }
}
