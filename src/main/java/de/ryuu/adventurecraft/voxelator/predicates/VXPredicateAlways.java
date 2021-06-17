package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator.FilterFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateAlways extends VXPredicate {
    public static FilterFactory FACTORY = new FilterFactory() {
        @Override
        public String getName() {
            return "always";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateAlways();
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

    public VXPredicateAlways() {
        // no op
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        return true;
    }
}