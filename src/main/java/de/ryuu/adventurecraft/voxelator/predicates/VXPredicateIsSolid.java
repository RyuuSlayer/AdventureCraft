package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateIsSolid extends VXPredicate {
    public static Voxelator.FilterFactory FACTORY = new Voxelator.FilterFactory() {
        @Override
        public String getName() {
            return "is_solid";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateIsSolid();
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
        return fworld.getBlockState(pos).getBlockFaceShape(fworld, pos, EnumFacing.UP) == BlockFaceShape.SOLID;
    }

}