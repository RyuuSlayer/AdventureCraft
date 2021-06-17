package de.ryuu.adventurecraft.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// TODO: Redo the findBlock methods to accept the new metadata parsing from MC 1.10.X!
public class GObjectTypeHelper {

    public static List<String> getParticleNameList() {
        List<String> list = Lists.newArrayList();
        EnumParticleTypes[] types = EnumParticleTypes.values();

        for (EnumParticleTypes type : types) {
            list.add(type.getParticleName());
        }

        return list;
    }

    public static EnumParticleTypes findParticleType(String name) {
        EnumParticleTypes[] types = EnumParticleTypes.values();

        for (EnumParticleTypes type : types)
            if (type.getParticleName().equalsIgnoreCase(name))
                return type;

        throw new IllegalArgumentException("Given name is not a particle name.");
    }

    public static Item findItem(String fully_qualified_name) {

        int indexOfDot = fully_qualified_name.indexOf(':');

        // String typeMod = null;
        String typeID = null;

        if (indexOfDot == -1) {
            // typeMod = "minecraft";
            typeID = fully_qualified_name;
        } else {
            // typeMod = typeMID.substring(0, indexOfDot);
            typeID = fully_qualified_name.substring(indexOfDot + 1);
        }

        return Item.getByNameOrId(typeID);
    }

    public static String[] findBlockState_retAStr(String fully_qualified_name) {
        int indexOfSlash = fully_qualified_name.indexOf('/');

        String typeMID = null;
        int typeMeta = 0;

        if (indexOfSlash == -1) {
            typeMID = fully_qualified_name;
        } else {
            typeMID = fully_qualified_name.substring(0, indexOfSlash);
            typeMeta = Integer.parseInt(fully_qualified_name.substring(indexOfSlash + 1));
        }

        int indexOfDot = typeMID.indexOf(':');

        String typeMod = null;
        String typeID = null;

        if (indexOfDot == -1) {
            typeMod = "minecraft";
            typeID = typeMID;
        } else {
            typeMod = typeMID.substring(0, indexOfDot);
            typeID = typeMID.substring(indexOfDot + 1);
        }

        ResourceLocation location = new ResourceLocation(typeMod + ":" + typeID);
        Block block = Block.REGISTRY.getObject(location);

        if (!Block.REGISTRY.containsKey(location)) {
            System.err.println("Block type mismatch: " + fully_qualified_name + " | " + typeMod + " " + typeID + " "
                    + typeMeta + " GOT " + block.getUnlocalizedName());
            return null; // This is the wrong block! D: (Probably minecraft:air)
        }

        return new String[]{typeMID, typeMod, typeID, Integer.toString(typeMeta)};

    }

    public static String findBlockState_type(String fully_qualified_name) {
        int indexOfSlash = fully_qualified_name.indexOf('/');

        String typeMID = null;

        if (indexOfSlash == -1) {
            typeMID = fully_qualified_name;
        } else {
            typeMID = fully_qualified_name.substring(0, indexOfSlash);
        }

        int indexOfDot = typeMID.indexOf(':');

        // String typeMod = null;
        String typeID = null;

        if (indexOfDot == -1) {
            // typeMod = "minecraft";
            typeID = typeMID;
        } else {
            // typeMod = typeMID.substring(0, indexOfDot);
            typeID = typeMID.substring(indexOfDot + 1);
        }

        return typeID;
    }

    public static int findBlockState_meta(String fully_qualified_name) {
        int indexOfSlash = fully_qualified_name.indexOf('/');

        // String typeMID = null;
        int typeMeta = 0;

        if (indexOfSlash == -1) {
            // typeMID = typeString;
        } else {
            // typeMID = typeString.substring(0, indexOfSlash);
            typeMeta = Integer.parseInt(fully_qualified_name.substring(indexOfSlash + 1));
        }

        return typeMeta;
    }

    public static IBlockState findBlockState(String fully_qualified_name) {
        int indexOfSlash = fully_qualified_name.indexOf('/');

        String typeMID = null;
        int typeMeta = 0;

        if (indexOfSlash == -1) {
            typeMID = fully_qualified_name;
        } else {
            typeMID = fully_qualified_name.substring(0, indexOfSlash);
            typeMeta = Integer.parseInt(fully_qualified_name.substring(indexOfSlash + 1));
        }

        int indexOfDot = typeMID.indexOf(':');

        String typeMod = null;
        String typeID = null;

        if (indexOfDot == -1) {
            typeMod = "minecraft";
            typeID = typeMID;
        } else {
            typeMod = typeMID.substring(0, indexOfDot);
            typeID = typeMID.substring(indexOfDot + 1);
        }

        ResourceLocation location = new ResourceLocation(typeMod + ":" + typeID);
        Block block = Block.REGISTRY.getObject(location);

        if (block.getUnlocalizedName().equals("tile.air") && !typeID.contains("air")) {
            return null; // This is the wrong block! D: (Probably minecraft:air)
        }

        return block.getStateFromMeta(typeMeta);

    }

    public static Block findBlock(String fully_qualified_name) {
        int indexOfSlash = fully_qualified_name.indexOf('/');

        String typeMID = null;
        // int typeMeta = 0;

        if (indexOfSlash == -1) {
            typeMID = fully_qualified_name;
        } else {
            typeMID = fully_qualified_name.substring(0, indexOfSlash);
            // typeMeta = Integer.valueOf(typeString.substring(indexOfSlash+1));
        }

        int indexOfDot = typeMID.indexOf(':');

        String typeMod = null;
        String typeID = null;

        if (indexOfDot == -1) {
            typeMod = "minecraft";
            typeID = typeMID;
        } else {
            typeMod = typeMID.substring(0, indexOfDot);
            typeID = typeMID.substring(indexOfDot + 1);
        }

        ResourceLocation location = new ResourceLocation(typeMod + ":" + typeID);
        Block block = Block.REGISTRY.getObject(location);

        if (block.getUnlocalizedName().equals("tile.air") && !typeID.contains("air")) {
            return null; // This is the wrong block! D: (Probably minecraft:air)
        }

        return block;

    }

    public static Potion findPotion(String name) {
        return Potion.getPotionFromResourceLocation(name);
    }

    public static Set<ResourceLocation> getItemNameList() {
        return Item.REGISTRY.getKeys();
    }

    public static Collection<ResourceLocation> getEntityNameList() {
        return Lists.newArrayList(EntityList.getEntityNameList());
    }

}