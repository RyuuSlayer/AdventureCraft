package de.ryuu.adventurecraft.script.wrappers.potion;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.potion.Potion;

import java.util.List;

public class PotionObjectWrapper implements IObjectWrapper {
    private Potion potion;

    public PotionObjectWrapper(Potion potion) {
        this.potion = potion;
    }

    @Override
    public Potion internal() {
        return potion;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public int getId() {
        return Potion.getIdFromPotion(potion);
    }

    public String getName() {
        return potion.getName();
    }

}