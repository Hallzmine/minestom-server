package fr.themode.demo.items;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.data.Data;
import net.minestom.server.data.DataImpl;
import net.minestom.server.data.type.BooleanData;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.attribute.AttributeSlot;
import net.minestom.server.item.attribute.ItemAttribute;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class CustomSword extends ItemStack {

    //The constructor that is used for creating new itemstacks for the custom sword system
    public CustomSword(Material material, byte amount, int custommodeldata, ColoredText title, boolean unbreakable, ArrayList<ColoredText> lore, double damage) {
        //Basic Itemstack super
        super(material, amount);
        //Just a check to see if custom modeldata is a needed field or not, will be used in the future for custom item textures
        if (custommodeldata != 0) {
            this.setCustomModelData(custommodeldata);
        }
        //Displayname, lore, unbreakable, damage, pretty straight foward
        this.setDisplayName(title);
        this.setLore(lore);
        this.setUnbreakable(unbreakable);
        this.addAttribute(new ItemAttribute(UUID.randomUUID(), "effect.damageBoost", Attribute.ATTACK_DAMAGE, AttributeOperation.ADDITION, damage, AttributeSlot.MAINHAND));
    }

    //Sets the enchantable tag on the sword to either true or false, this will be used later to check if the item can be enchanted. This is based on the sword's components.
    public void setEnchantable(Boolean value) {
        //I have no clue why setting data took me an entire day to implement, it is super system I just have monkey brain sometimes.
        Data bada = new DataImpl();
        //This class<t> is the bane of my exsistance
        bada.set("enchantable", value, Boolean.class);
        this.setData(bada);

    }

    //Gets the enchantable tag from the custom sword
    public boolean getEnchantable() {
        this.getData().get("enchantable");
        return false;
    }

    //Sets the infusable tag on the sword to either true or false, this will be used later to check if the item can be enchanted. This is based on the sword's components.
    public void setInfusable(Boolean value) {
        Data bada = new DataImpl();
        bada.set("infusable", value, Boolean.class);
        this.setData(bada);

    }

    //Gets the infusable tag from the custom sword
    public boolean getInfusable() {
        this.getData().get("infusable");
        return false;
    }

}
