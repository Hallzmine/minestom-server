package fr.themode.demo.entity;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.type.monster.EntityZombie;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.time.TimeUnit;

public class ZombieCreature extends EntityZombie {

    public ZombieCreature(Position spawnPosition) {
        super(spawnPosition);
        goalSelectors.add(new RandomLookAroundGoal(this, 20));
        goalSelectors.add(new MeleeAttackGoal(this, 500, TimeUnit.MILLISECOND));
        goalSelectors.add(new FollowTargetGoal(this));



        targetSelectors.add(new LastEntityDamagerTarget(this, 15));
        targetSelectors.add(new ClosestEntityTarget(this, 15, LivingEntity.class));
    }
}
