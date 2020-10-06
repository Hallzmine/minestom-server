package fr.themode.demo.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.BlockPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetCommand extends Command {
    //This doesn't work at all needs to be removed
    public SetCommand() {
        super("set", "setblock");

        Argument arg0 = ArgumentType.Word("block");

        addSyntax(this::execute, arg0);
    }

    private void execute(CommandSender sender, Arguments args) {
        Player player = (Player) sender;
        final Map<BlockPosition, Block> blocks = new HashMap<>();
        blocks.put(player.getPosition().toBlockPosition(), Block.valueOf(args.getWord("block")));
        ChunkBatch chunk = new ChunkBatch(new InstanceContainer(UUID.randomUUID(), player.getDimensionType(), null), player.getChunk());
        chunk.setBlock(player.getPosition().toBlockPosition(), Block.valueOf(args.getWord("block")));

    }
}
