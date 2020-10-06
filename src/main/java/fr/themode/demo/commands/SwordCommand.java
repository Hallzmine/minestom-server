package fr.themode.demo.commands;

import fr.themode.demo.items.SwordConstructor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class SwordCommand extends Command {

    //The command to produce a new sword
    public SwordCommand() {
        //Just sets the command's name
        super("sword");

        //Arguments, it takes in the components, I should make a list here of all the components so I can tab autofill, use .from()
        Argument arg0 = ArgumentType.Word("handle");
        Argument arg1 = ArgumentType.Word("hilt");
        Argument arg2 = ArgumentType.Word("blade");

        //calls the execute
        addSyntax(this::execute, arg0, arg1, arg2);
    }
    private void execute(CommandSender sender, Arguments args) {
        //gets player from sender
        Player player = (Player) sender;
        //adds the itemstack to the player's inventory
        player.getInventory().addItemStack(SwordConstructor.SwordMaker(args.getWord("handle"), args.getWord("hilt"), args.getWord("blade")));
    }
}
