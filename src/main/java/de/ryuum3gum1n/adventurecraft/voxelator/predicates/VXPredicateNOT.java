package de.ryuum3gum1n.adventurecraft.voxelator.predicates;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.util.MutableBlockPos;
import de.ryuum3gum1n.adventurecraft.voxelator.BrushParameter;
import de.ryuum3gum1n.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuum3gum1n.adventurecraft.voxelator.VXPredicate;
import de.ryuum3gum1n.adventurecraft.voxelator.Voxelator;
import de.ryuum3gum1n.adventurecraft.voxelator.Voxelator.FilterFactory;

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