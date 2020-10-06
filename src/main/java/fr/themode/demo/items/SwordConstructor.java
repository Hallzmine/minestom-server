package fr.themode.demo.items;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.attribute.AttributeSlot;
import net.minestom.server.item.attribute.ItemAttribute;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class SwordConstructor {

    //called with the components to produce a new CustomSword which contains all the required information to produce it in game
    public static ItemStack SwordMaker(String handle, String hilt, String blade) {

        //The sword's vanilla item, determined by the handle
        Material material;
        //The amount the recipe outputs, it will almost always be 1, I don't see why I would ever set this to anything but 1
        byte amount = 1;
        //The texture based off of the material that I want to call, currently will be just 0 because I do not have any talent with texturing, will be determined by blade or hilt, unsure
        int custommodeldata = 0;
        //displayname
        ColoredText title;
        //Is it unbreakable
        boolean unbreakable = false;
        //Lore arraylist
        ArrayList<ColoredText> lore = new ArrayList<ColoredText>();
        //Attack Damage
        double damage = 0;
        // enchantable/infusable system, determined by blade
        boolean enchantable = true;
        boolean infusable = true;
        //The lore of the handle and blade components, things like "Tasty" or "Brittle" that are chosen based on the components
        ColoredText handle_lore = ColoredText.of("");
        ColoredText blade_lore = ColoredText.of("");

        //BIG SWITCH, I need to turn this into a file that I can call from
        //Handles the handle system, handles output a material and a handle_lore, handle_lore is optional material isn't
        switch(handle) {
            case "iron":
                material = Material.IRON_SWORD;
                break;

            case "wood":
                material = Material.WOODEN_SWORD;
                break;

            case "stone":
                material = Material.STONE_SWORD;
                break;

            case "gold":
                material = Material.GOLDEN_SWORD;
                break;

            case "diamond":
                material = Material.DIAMOND_SWORD;
                break;

            case "prismarine":
                material = Material.GOLDEN_SWORD;
                handle_lore = ColoredText.of(ChatColor.BLUE + "Brittle");
                break;

            case "blaze":
                material = Material.DIAMOND_SWORD;
                handle_lore = ColoredText.of(ChatColor.GOLD + "Naturally Strong");
                break;

            case "brick":
                material = Material.GOLDEN_SWORD;
                handle_lore = ColoredText.of(ChatColor.RED + "Brittle");
                break;

            case "clay":
                material = Material.GOLDEN_SWORD;
                handle_lore = ColoredText.of(ChatColor.GRAY + "Mushy");
                break;

            case "slime":
                material = Material.WOODEN_SWORD;
                handle_lore = ColoredText.of(ChatColor.BRIGHT_GREEN + "Flexible");
                break;

            case "coal":
                material = Material.STONE_SWORD;
                break;

            case "honey":
                material = Material.IRON_SWORD;
                handle_lore = ColoredText.of(ChatColor.GOLD + "Tasty");
                break;

            case "steel":
                material = Material.NETHERITE_SWORD;
                handle_lore = ColoredText.of(ChatColor.GRAY + "Oh no! I've been steeled!");
                break;

            case "bone":
                material = Material.STONE;
                handle_lore = ColoredText.of(ChatColor.GRAY + "Brittle");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + handle);
        }


        //Big blade switch, outputs damage, infusability, and enchantability, if infusable/enchantable are not set they default to true. Damage is often randomized here but is not required. ALSO outputs blade_lore not required.
        switch(blade) {
            case "iron":
                damage = 6.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                break;

            case "diamond":
                damage =  7.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                infusable = false;
                break;

            case "gold":
                damage =  4.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                break;

            case "wood":
                damage =  4.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                infusable=false;
                enchantable=false;
                break;

            case "stone":
                damage =  5.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                enchantable=false;
                break;

            case "chorus":
                damage = 5 + (((Math.floor(Math.random()*10)/10)*10)-5);
                infusable=false;
                blade_lore = ColoredText.of(ChatColor.PURPLE + "Unstable");
                break;

            case "honey":
                damage = 5.5;
                enchantable = false;
                blade_lore = ColoredText.of(ChatColor.GOLD + "Sticky");
                //Make it apply slowness to enemies
                break;

            case "quartz":
                damage = 8;
                infusable=false;
                blade_lore = ColoredText.of(ChatColor.WHITE + "Sharp!");
                break;

            case "bone":
                damage = 6 + (((Math.floor(Math.random()*10)/10)*2.5)-1.25);
                infusable = false;
                enchantable = false;
                blade_lore = ColoredText.of(ChatColor.WHITE + "Brittle, but sharp!");
                break;

            case "flint":
                damage = 5.5;
                blade_lore = ColoredText.of(ChatColor.GRAY + "Primitive");
                break;

            case "coal":
                damage = 5.5 + (((Math.floor(Math.random()*10)/10)*1)-1);
                blade_lore = ColoredText.of(ChatColor.DARK_RED + "Flammable");
                //Make it apply flame to enemies
                break;

            case "scute":
                damage = 9;
                blade_lore = ColoredText.of(ChatColor.BRIGHT_GREEN + "CUTE!");
                infusable = false;
                enchantable = false;
                break;

            case "steel":
                damage = 9;
                blade_lore = ColoredText.of(ChatColor.GRAY + "Would be a shame if someone were to STEEL this blade");
                break;

            case "prismarine":
                damage = 6 + (((Math.floor(Math.random()*10)/10)*1)-1);
                blade_lore = ColoredText.of(ChatColor.BLUE + "Shiny... Too shiny.....");
                //Give nauseau, night vision and blindness to targets
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + blade);
        }

        //Sets the displayname of the item, currently just defaults to a blue name "Custom Blade"
        title = ColoredText.of(ChatColor.fromRGB(0, 102, 255) + "Custom Blade");
        //Builds the new CustomSword
        CustomSword item = new CustomSword(material, amount, custommodeldata, title, unbreakable, lore, damage);
        //sets the enchant/infuse via the previously defined variables
        item.setEnchantable(enchantable);
        item.setInfusable(infusable);
        //gets the item stack of the CustomSword
        ItemStack item_stack = item;
        //gets the lore from the handle and adds it
        item.getLore().add(handle_lore);

        //Hilt switch, has to be here as it effects attributes which need to be called after the construction of the itemstack
        //Can output a number of things such as attributes but also optionally adds lore
        switch(hilt) {
            case "iron":
                break;

            case "prismarine":
                item_stack.addAttribute(new ItemAttribute(UUID.randomUUID(), "Armor", Attribute.ARMOR, AttributeOperation.ADDITION, 5, AttributeSlot.MAINHAND));
                item.getLore().add(ColoredText.of(ChatColor.BLUE + "Shielding!"));
                break;

            default:
                break;
        }

        //Adds the blade lore
        item.getLore().add(blade_lore);
        //Handles the rest of the lore including the components and the damage
        item.getLore().add(ColoredText.of(""));
        item.getLore().add(ColoredText.of("Handle: " + handle));
        item.getLore().add(ColoredText.of("Hilt: " +  hilt));
        item.getLore().add(ColoredText.of("Blade: " + blade));
        item.getLore().add(ColoredText.of(""));
        item.getLore().add(ColoredText.of("Damage: " + damage));

        //returns the stack
        return item_stack;
    }


}
