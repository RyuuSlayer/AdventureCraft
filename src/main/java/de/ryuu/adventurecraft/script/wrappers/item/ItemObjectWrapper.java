package de.ryuu.adventurecraft.script.wrappers.item;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.item.Item;

import java.util.List;

public class ItemObjectWrapper implements IObjectWrapper {
    private Item item;

    public ItemObjectWrapper(Item item) {
        this.item = item;
    }

    @Override
    public Item internal() {
        return item;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public String getUnlocalizedName() {
        return item.getUnlocalizedName();
    }

}