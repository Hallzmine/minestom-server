package net.minestom.server.instance.block.states;
import net.minestom.server.instance.block.BlockAlternative;
import static net.minestom.server.instance.block.Block.*;
/**
 * Completely internal. DO NOT USE. IF YOU ARE A USER AND FACE A PROBLEM WHILE USING THIS CODE, THAT'S ON YOU.
 */
@Deprecated(forRemoval = false, since = "forever")
public class WarpedPressurePlate {
	public static void initStates() {
		WARPED_PRESSURE_PLATE.addBlockAlternative(new BlockAlternative((short) 15069, "powered=true"));
		WARPED_PRESSURE_PLATE.addBlockAlternative(new BlockAlternative((short) 15070, "powered=false"));
	}
}
