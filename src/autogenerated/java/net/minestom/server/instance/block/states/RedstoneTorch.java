package net.minestom.server.instance.block.states;
import net.minestom.server.instance.block.BlockAlternative;
import static net.minestom.server.instance.block.Block.*;
/**
 * Completely internal. DO NOT USE. IF YOU ARE A USER AND FACE A PROBLEM WHILE USING THIS CODE, THAT'S ON YOU.
 */
@Deprecated(forRemoval = false, since = "forever")
public class RedstoneTorch {
	public static void initStates() {
		REDSTONE_TORCH.addBlockAlternative(new BlockAlternative((short) 3887, "lit=true"));
		REDSTONE_TORCH.addBlockAlternative(new BlockAlternative((short) 3888, "lit=false"));
	}
}
