package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import de.ryuu.adventurecraft.voxelator.Voxelator.FilterFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateNOT extends VXPredicate {
    public static FilterFactory FACTORY = new FilterFactory() {
        @Override
        public String getName() {
            return "not";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            VXPredicate filter = Voxelator.newFilter(filterData.getCompoundTag("filter"));

            return new VXPredicateNOT(filter);
        }

        @Override
        public NBTTagCompound newFilter(String[] parameters) {
            throw new UnsupportedOperationException("Not Yet Implemented!");
        }

        @Override
        public BrushParameter[] getParameters() {
            return BrushParameter.NO_PARAMETERS;
        }
    };

    private final VXPredicate predicate;

    public VXPredicateNOT(VXPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        return !predicate.test(pos, center, offset, fworld);
    }
}