package fr.themode.demo.items;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.data.Data;
import net.minestom.server.data.DataImpl;
import net.minestom.server.data.DataType;
import net.minestom.server.data.type.IntegerData;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.binary.BinaryWriter;

import java.util.Set;
import java.util.function.DoubleToIntFunction;

public class TestItem extends ItemStack {

    public TestItem() {
        super(Material.IRON_SWORD, (byte) 1, 0);
        int random_value = (int)(Math.random()*100);
        this.setDisplayName(ColoredText.of("Test Item"));
        //this.setData(Data.EMPTY.set("random", random_value, ));
        this.getLore().add(ColoredText.of(ChatColor.RED + "A test item!" + Integer.toString(random_value) + ChatColor.fromRGB(255, 0, 255) +" I'm purple"));
        this.setEnchantment(Enchantment.SHARPNESS, (short) 10);
    }

}
