package de.ryuu.adventurecraft.script.wrappers.potion;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public class PotionEffectObjectWrapper implements IObjectWrapper {
    private PotionEffect potionEffect;

    public PotionEffectObjectWrapper(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
    }

    @Override
    public PotionEffect internal() {
        return potionEffect;
    }

    public int getDuration() {
        return potionEffect.getDuration();
    }

    public int getAmplifier() {
        return potionEffect.getAmplifier();
    }

    public boolean isDurationMax() {
        return potionEffect.getIsPotionDurationMax();
    }

    public boolean fromBeacon() {
        return potionEffect.getIsAmbient();
    }

    public PotionObjectWrapper getPotion() {
        return new PotionObjectWrapper(potionEffect.getPotion());
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

}