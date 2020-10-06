package net.minestom.server.instance.block.states;
import net.minestom.server.instance.block.BlockAlternative;
import static net.minestom.server.instance.block.Block.*;
/**
 * Completely internal. DO NOT USE. IF YOU ARE A USER AND FACE A PROBLEM WHILE USING THIS CODE, THAT'S ON YOU.
 */
@Deprecated(forRemoval = false, since = "forever")
public class PolishedDioriteSlab {
	public static void initStates() {
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10811, "type=top", "waterlogged=true"));
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10812, "type=top", "waterlogged=false"));
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10813, "type=bottom", "waterlogged=true"));
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10814, "type=bottom", "waterlogged=false"));
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10815, "type=double", "waterlogged=true"));
		POLISHED_DIORITE_SLAB.addBlockAlternative(new BlockAlternative((short) 10816, "type=double", "waterlogged=false"));
	}
}
