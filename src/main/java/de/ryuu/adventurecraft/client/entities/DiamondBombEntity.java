package de.ryuu.adventurecraft.client.entities;

import de.ryuu.adventurecraft.client.init.AdventureCraftItems;
import de.ryuu.adventurecraft.client.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DiamondBombEntity extends BombEntity {
    public DiamondBombEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public DiamondBombEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return AdventureCraftItems.DIAMOND_BOMB;
    }

    @Override
    public CustomExplosion getExplosion() {
        return new CustomExplosion(this.world, this, this.getX(), this.getY(), this.getZ(), 3f, CustomExplosion.BlockBreakEffect.UNSTOPPABLE, Explosion.DestructionType.BREAK);
    }
}