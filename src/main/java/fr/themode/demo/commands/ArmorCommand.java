package fr.themode.demo.commands;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;
import org.spongepowered.asm.mixin.injection.At;

public class ArmorCommand extends Command {

    public ArmorCommand() {
        super("armor",  "armorbar");

        setCondition(this::condition);

        setDefaultExecutor(this::defaultExecutor);

        Argument arg0 = ArgumentType.Word("mode").from("set", "add");

        Argument arg1 = ArgumentType.Integer("value");

        addCallback(this::modeCallback, arg0);
        addCallback(this::valueCallback, arg1);

        addSyntax(this::execute2, arg0, arg1);
        addSyntax(this::execute, arg0);
    }

    private boolean condition(CommandSender sender) {
        if (!sender.isPlayer()) {
            sender.sendMessage("The command is only available for player");
            return false;
        }
        return true;
    }

    private void defaultExecutor(CommandSender sender, Arguments args) {
        sender.sendMessage("Correct usage: armor [set/add] [number]");
    }

    private void modeCallback(CommandSender sender, String value, int error) {
        sender.sendMessage("SYNTAX ERROR: '" + value + "' should be replaced by 'set' or 'add'");
    }

    private void valueCallback(CommandSender sender, String value, int error) {
        switch (error) {
            case ArgumentNumber.NOT_NUMBER_ERROR:
                sender.sendMessage("SYNTAX ERROR: '" + value + "' isn't a number!");
                break;
        }
    }

    private void execute(CommandSender sender, Arguments args) {
        sender.sendMessage("/armor " + args.getWord("mode") + " [Integer]");
    }

    private void execute2(CommandSender sender, Arguments args) {
        Player player = (Player) sender;
        String mode = args.getWord("mode");
        int value = args.getInteger("value");

        switch (mode.toLowerCase()) {
            case "set":
                player.setAttribute(Attribute.ARMOR, value);
                break;
            case "add":
                player.setAttribute(Attribute.ARMOR,player.getAttributeValue(Attribute.ARMOR) + value);
                break;
        }
        player.sendMessage("You now have " + player.getAttributeValue(Attribute.ARMOR) + " armor");
    }

}