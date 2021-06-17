package de.ryuu.adventurecraft.voxelator.predicates;

import de.ryuu.adventurecraft.util.GObjectTypeHelper;
import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXPredicate;
import de.ryuu.adventurecraft.voxelator.Voxelator.FilterFactory;
import de.ryuu.adventurecraft.voxelator.params.BlockstateBrushParameter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class VXPredicateIsType extends VXPredicate {
    private static final BrushParameter[] PARAMS = new BrushParameter[]{
            new BlockstateBrushParameter("state", Blocks.AIR)};

    public static FilterFactory FACTORY = new FilterFactory() {
        @Override
        public String getName() {
            return "is_type";
        }

        @Override
        public VXPredicate newFilter(NBTTagCompound filterData) {
            return new VXPredicateIsType(GObjectTypeHelper.findBlock(filterData.getString("state")));
        }

        @Override
        public NBTTagCompound newFilter(String[] parameters) {
            if (parameters.length == 1) {
                NBTTagCompound filterData = new NBTTagCompound();
                filterData.setString("type", getName());
                filterData.setString("state", parameters[0]);
                return filterData;
            }
            return null;
        }

        @Override
        public BrushParameter[] getParameters() {
            return PARAMS;
        }
    };

    private final Block type;

    public VXPredicateIsType(Block type) {
        this.type = type;
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        return fworld.getBlockState(pos).getBlock().equals(type);
    }

}