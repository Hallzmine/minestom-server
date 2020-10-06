package fr.themode.demo.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntities;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GiveCommand extends Command {

    public GiveCommand() {
        super("give", "i");

        Argument arg0 = ArgumentType.Word("item");
        Argument arg1 = ArgumentType.Integer("amount");
        addSyntax(this::execute, arg0, arg1);
    }

    private void execute(CommandSender sender, Arguments args) {
        List<Entity> entities = args.getEntities("targets");
        Player player = (Player) sender;
        player.getInventory().addItemStack(new ItemStack(Material.valueOf(args.getWord("item")), (byte) args.getInteger("amount")));

    }

}

