package de.ryuum3gum1n.adventurecraft.voxelator.predicates;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.util.MutableBlockPos;
import de.ryuum3gum1n.adventurecraft.voxelator.BrushParameter;
import de.ryuum3gum1n.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuum3gum1n.adventurecraft.voxelator.VXPredicate;
import de.ryuum3gum1n.adventurecraft.voxelator.Voxelator.FilterFactory;

public final class VXPredicateIsSolid extends VXPredicate {
	public static FilterFactory FACTORY = new FilterFactory() {
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