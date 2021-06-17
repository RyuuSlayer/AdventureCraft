package de.ryuu.adventurecraft.voxelator.actions;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXAction;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class VXActionGrassify extends VXAction {

    public static final Voxelator.ActionFactory FACTORY = new Voxelator.ActionFactory() {
        @Override
        public String getName() {
            return "grassify";
        }

        @Override
        public VXAction newAction(NBTTagCompound actionData) {
            return new VXActionGrassify();
        }

        @Override
        public NBTTagCompound newAction(String[] parameters) {
            NBTTagCompound actionData = new NBTTagCompound();
            actionData.setString("type", getName());
            return actionData;
        }

        @Override
        public BrushParameter[] getParameters() {
            return BrushParameter.NO_PARAMETERS;
        }
    };

    public VXActionGrassify() {
    }

    @Override
    public void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        fworld.setBlockState(pos, Blocks.GRASS.getDefaultState());
        fworld.setBlockState(pos.down(1), Blocks.DIRT.getDefaultState());
        fworld.setBlockState(pos.down(2), Blocks.DIRT.getDefaultState());
    }

}