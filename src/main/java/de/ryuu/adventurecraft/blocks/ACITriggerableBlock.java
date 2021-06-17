package de.ryuu.adventurecraft.blocks;

import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ACITriggerableBlock {
    void trigger(World world, BlockPos position, EnumTriggerState triggerState);
}