package de.ryuu.adventurecraft.util;

import com.google.common.collect.Maps;
import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import java.util.Map;

// Adapted from: net.minecraft.util.datafix.fixes.TileEntityId
public class ACDataFixer implements IFixableData {
    private static final Map<String, String> OLD_TO_NEW_ID_MAP = Maps.newHashMap();

    static {
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_lockeddoorblock", Reference.MOD_ID + ":lockeddoorblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_killblock", Reference.MOD_ID + ":killblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_clockblock", Reference.MOD_ID + ":clockblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_redstone_trigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_redstone_activator", Reference.MOD_ID + ":redstone_activator");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_relayblock", Reference.MOD_ID + ":relayblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_scriptblock", Reference.MOD_ID + ":scriptblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_updatedetectorblock", Reference.MOD_ID + ":updatedetectorblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_storageblock", Reference.MOD_ID + ":storageblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_emitterblock", Reference.MOD_ID + ":emitterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_imagehologramblock", Reference.MOD_ID + ":imagehologramblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_collisiontriggerblock", Reference.MOD_ID + ":collisiontriggerblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_lightblock", Reference.MOD_ID + ":lightblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_hiddenblock", Reference.MOD_ID + ":hiddenblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_messageblock", Reference.MOD_ID + ":messageblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_inverterblock", Reference.MOD_ID + ":inverterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_memoryblock", Reference.MOD_ID + ":memoryblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_triggerfilterblock", Reference.MOD_ID + ":triggerfilterblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_delayblock", Reference.MOD_ID + ":delayblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_urlblock", Reference.MOD_ID + ":urlblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_summonblock", Reference.MOD_ID + ":summonblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_musicblock", Reference.MOD_ID + ":musicblock");
        OLD_TO_NEW_ID_MAP.put("minecraft:ac_camerablock", Reference.MOD_ID + ":camerablock");

        OLD_TO_NEW_ID_MAP.put("ac_lockeddoorblock", Reference.MOD_ID + ":lockeddoorblock");
        OLD_TO_NEW_ID_MAP.put("ac_killblock", Reference.MOD_ID + ":killblock");
        OLD_TO_NEW_ID_MAP.put("ac_clockblock", Reference.MOD_ID + ":clockblock");
        OLD_TO_NEW_ID_MAP.put("ac_redstone_trigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("ac_redstonetrigger", Reference.MOD_ID + ":redstone_trigger");
        OLD_TO_NEW_ID_MAP.put("ac_redstone_activator", Reference.MOD_ID + ":redstone_activator");
        OLD_TO_NEW_ID_MAP.put("ac_relayblock", Reference.MOD_ID + ":relayblock");
        OLD_TO_NEW_ID_MAP.put("ac_scriptblock", Reference.MOD_ID + ":scriptblock");
        OLD_TO_NEW_ID_MAP.put("ac_updatedetectorblock", Reference.MOD_ID + ":updatedetectorblock");
        OLD_TO_NEW_ID_MAP.put("ac_storageblock", Reference.MOD_ID + ":storageblock");
        OLD_TO_NEW_ID_MAP.put("ac_emitterblock", Reference.MOD_ID + ":emitterblock");
        OLD_TO_NEW_ID_MAP.put("ac_imagehologramblock", Reference.MOD_ID + ":imagehologramblock");
        OLD_TO_NEW_ID_MAP.put("ac_collisiontriggerblock", Reference.MOD_ID + ":collisiontriggerblock");
        OLD_TO_NEW_ID_MAP.put("ac_lightblock", Reference.MOD_ID + ":lightblock");
        OLD_TO_NEW_ID_MAP.put("ac_hiddenblock", Reference.MOD_ID + ":hiddenblock");
        OLD_TO_NEW_ID_MAP.put("ac_messageblock", Reference.MOD_ID + ":messageblock");
        OLD_TO_NEW_ID_MAP.put("ac_inverterblock", Reference.MOD_ID + ":inverterblock");
        OLD_TO_NEW_ID_MAP.put("ac_memoryblock", Reference.MOD_ID + ":memoryblock");
        OLD_TO_NEW_ID_MAP.put("ac_triggerfilterblock", Reference.MOD_ID + ":triggerfilterblock");
        OLD_TO_NEW_ID_MAP.put("ac_delayblock", Reference.MOD_ID + ":delayblock");
        OLD_TO_NEW_ID_MAP.put("ac_urlblock", Reference.MOD_ID + ":urlblock");
        OLD_TO_NEW_ID_MAP.put("ac_summonblock", Reference.MOD_ID + ":summonblock");
        OLD_TO_NEW_ID_MAP.put("ac_musicblock", Reference.MOD_ID + ":musicblock");
        OLD_TO_NEW_ID_MAP.put("ac_camerablock", Reference.MOD_ID + ":camerablock");
    }

    public int getFixVersion() {
        return 1;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        String oldID = compound.getString("id");
        String newID = OLD_TO_NEW_ID_MAP.get(oldID);

        if (newID != null) {
            AdventureCraft.logger.info("Converted tile entity ID. Old ID: " + oldID + ", new ID: " + newID);
            compound.setString("id", newID);
        }

        return compound;
    }
}