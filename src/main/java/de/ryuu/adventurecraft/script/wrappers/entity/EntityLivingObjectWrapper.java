package de.ryuu.adventurecraft.script.wrappers.entity;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.potion.PotionEffectObjectWrapper;
import net.minecraft.entity.EntityLiving;

import java.util.List;

public class EntityLivingObjectWrapper extends EntityObjectWrapper {
    private final EntityLiving entity;

    public EntityLivingObjectWrapper(EntityLiving entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public EntityLiving internal() {
        return entity;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public boolean navigateTo(double x, double y, double z) {
        return entity.getNavigator().tryMoveToXYZ(x, y, z, 0.3D);
    }

    public boolean navigateTo(double x, double y, double z, double speed) {
        return entity.getNavigator().tryMoveToXYZ(x, y, z, speed);
    }

    public boolean navigateTo(EntityObjectWrapper entity2, double speed) {
        return entity.getNavigator().tryMoveToEntityLiving(entity2.internal(), speed);
    }

    public float getHealth() {
        return entity.getHealth();
    }

    public void setHealth(float health) {
        entity.setHealth(health);
        ;
    }

    public void addPotionEffect(PotionEffectObjectWrapper potionEffect) {
        entity.addPotionEffect(potionEffect.internal());
    }

    public void clearActivePotions() {
        entity.clearActivePotions();
    }

    public boolean canBeHitWithPotion() {
        return entity.canBeHitWithPotion();
    }

    public boolean canBePushed() {
        return entity.canBePushed();
    }

}