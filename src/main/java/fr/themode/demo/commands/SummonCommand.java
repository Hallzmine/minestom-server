package fr.themode.demo.commands;

import fr.themode.demo.entity.ChickenCreature;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.type.monster.EntityZombie;

public class SummonCommand extends Command {

    //This summons either a chicken or a zombie
    public SummonCommand() {
        //name of command
        super("summon");

        //argument autofills from zombie or chicken
        Argument arg0 = ArgumentType.Word("entity").from("zombie","chicken");
        //runs execute
        addSyntax(this::execute, arg0);

    }

    private void execute(CommandSender sender, Arguments args) {
        //gets the player
        Player player = (Player) sender;
        //gets the entity string name from the arg
        String entity = args.getWord("entity");

        //Uses a system that I didn't make
        switch (entity.toLowerCase()) {
            case "zombie":
                //Creates a new entity zombie at the player
                EntityZombie zombie = new EntityZombie(player.getPosition());
                //gives it SPEED
                zombie.setAttribute(Attribute.MOVEMENT_SPEED, 0.25f);
                //puts it into player instance
                zombie.setInstance(player.getInstance());
                break;
            case "chicken":
                //creates new chicken at player
                ChickenCreature chickenCreature = new ChickenCreature(player.getPosition());
                //puts it into player instance
                chickenCreature.setInstance(player.getInstance());
                break;
        }
        //finish statement
        player.sendMessage(entity + " has been summoned.");
    }
}