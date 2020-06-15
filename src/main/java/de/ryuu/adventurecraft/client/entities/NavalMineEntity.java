package de.ryuu.adventurecraft.client.entities;

import de.ryuu.adventurecraft.client.init.AdventureCraftItems;
import de.ryuu.adventurecraft.client.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class NavalMineEntity extends BombEntity {
    public NavalMineEntity(EntityType<? extends BombEntity> entityType, World world) {
        super(entityType, world);
    }

    public NavalMineEntity(EntityType<? extends BombEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return AdventureCraftItems.NAVAL_MINE;
    }

    @Override
    protected CustomExplosion getExplosion() {
        return new CustomExplosion(this.world, this, this.getX(), this.getY(), this.getZ(), 4f, CustomExplosion.BlockBreakEffect.AQUATIC, Explosion.DestructionType.BREAK);

    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onCollision(HitResult hitResult) {
        world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.NEUTRAL, 0.8F, 0.6F);
        super.onCollision(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

    @Override
    public BombTriggerType getTriggerType() {
        return BombTriggerType.IMPACT;
    }
}
