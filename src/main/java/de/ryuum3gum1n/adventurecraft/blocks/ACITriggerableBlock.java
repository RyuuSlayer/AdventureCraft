package de.ryuum3gum1n.adventurecraft.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import de.ryuum3gum1n.adventurecraft.invoke.EnumTriggerState;

public interface ACITriggerableBlock {
	void trigger(World world, BlockPos position, EnumTriggerState triggerState);
}