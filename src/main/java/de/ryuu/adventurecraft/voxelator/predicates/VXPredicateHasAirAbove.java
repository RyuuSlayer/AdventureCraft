package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateHasAirAbove extends VXPredicate {
    public static Voxelator.FilterFactory FACTORY = new Voxelator.FilterFactory() {
        @Override
        public String getName() {
            return "has_air_above";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateHasAirAbove();
        }

        @Override
        public NBTTagCompound newFilter(String[] parameters) {
            NBTTagCompound filterData = new NBTTagCompound();
            filterData.setString("type", getName());
            return filterData;
        }

        @Override
        public BrushParameter[] getParameters() {
            return BrushParameter.NO_PARAMETERS;
        }
    };

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        return fworld.isAirBlock(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
    }

}