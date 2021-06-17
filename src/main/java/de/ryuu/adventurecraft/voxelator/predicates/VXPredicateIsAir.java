package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateIsAir extends VXPredicate {
    public static Voxelator.FilterFactory FACTORY = new Voxelator.FilterFactory() {
        @Override
        public String getName() {
            return "is_air";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateIsAir();
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

    public VXPredicateIsAir() {
        // no op
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        return fworld.getBlockState(pos).equals(Blocks.AIR.getDefaultState());
    }
}