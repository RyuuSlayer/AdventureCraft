package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator.FilterFactory;
import de.ryuu.adventurecraft.voxelator.params.IntegerBrushParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public final class VXPredicateAverageSmooth extends VXPredicate {
    private static final BrushParameter[] PARAMS = new BrushParameter[]{
            new IntegerBrushParameter("range", 2, 15, 7)};

    public static FilterFactory FACTORY = new FilterFactory() {
        @Override
        public String getName() {
            return "avgsmooth";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateAverageSmooth(filterData.getInteger("range"));
        }

        @Override
        public NBTTagCompound newFilter(String[] parameters) {
            if (parameters.length == 1) {
                NBTTagCompound filterData = new NBTTagCompound();
                filterData.setString("type", getName());
                filterData.setInteger("range", Integer.parseInt(parameters[0]));
                return filterData;
            }
            return null;
        }

        @Override
        public BrushParameter[] getParameters() {
            return PARAMS;
        }
    };

    private final Vec3i vec;

    public VXPredicateAverageSmooth(int size) {
        this.vec = new Vec3i(size, size, size);
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        int count = 0;
        int total = 0;
        float value = 0;
        Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.getAllInBoxMutable(pos.subtract(vec), pos.add(vec));
        for (final BlockPos checkpos : iterable) {
            if (!fworld.isAirBlock(checkpos)) {
                count++;
            }
            total++;
        }

        value = ((float) count / (float) total);

        return value > 0.5f;
    }

}