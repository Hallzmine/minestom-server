package fr.themode.demo.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
        addSyntax(this::execute);
    }

    private void execute(CommandSender sender, Arguments args) {

        sender.sendMessage("Pong!");
    }

}
