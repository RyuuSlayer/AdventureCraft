package de.ryuu.adventurecraft.decorator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class StaticDecoration implements Decoration {

    protected final Random rand;

    public StaticDecoration() {
        rand = new Random();
    }

    @Override
    public abstract int plant(World world, BlockPos[] positions, NBTTagCompound options);

}