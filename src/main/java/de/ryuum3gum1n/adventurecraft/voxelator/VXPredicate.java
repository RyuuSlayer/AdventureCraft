package de.ryuum3gum1n.adventurecraft.voxelator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import de.ryuum3gum1n.adventurecraft.util.MutableBlockPos;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateAND;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateAverageSmooth;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateBoxSmooth;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateHasAirAbove;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateHeightLimit;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateIsSolid;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateIsState;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateIsType;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateNOT;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateOR;
import de.ryuum3gum1n.adventurecraft.voxelator.predicates.VXPredicateRandom;

public abstract class VXPredicate {
	/**
	 * A predicate that always returns true.
	 * 
	 * @deprecated Use instead:
	 *             {@code de.longor.adventurecraft.voxelator.predicates.VXPredicateAlways}
	 **/
	@Deprecated
	public static final VXPredicate ALWAYS = new VXPredicate() {
		@Override
		public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
			return true;
		}
	};

	/** @return SEE IMPLEMENTATION. **/
	public abstract boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld);

	public static VXPredicate newAND(VXPredicate... predicates) {
		return new VXPredicateAND(predicates);
	}

	public static VXPredicate newOR(VXPredicate... predicates) {
		return new VXPredicateOR(predicates);
	}

	public static VXPredicate newNOT(VXPredicate predicate) {
		return new VXPredicateNOT(predicate);
	}

	public static VXPredicate newTypeMatch(Block type) {
		return new VXPredicateIsType(type);
	}

	public static VXPredicate newStateMatch(IBlockState state) {
		return new VXPredicateIsState(state);
	}

	public static VXPredicate newHeightLimit(int height) {
		return new VXPredicateHeightLimit(height);
	}

	public static VXPredicate newAverageSmooth(int size) {
		return new VXPredicateAverageSmooth(size);
	}

	public static VXPredicate newBoxSmooth(int size) {
		return new VXPredicateBoxSmooth(size);
	}

	public static VXPredicate newHasAirAbove() {
		return new VXPredicateHasAirAbove();
	}

	public static VXPredicate newIsSolid() {
		return new VXPredicateIsSolid();
	}

	public static VXPredicate newRandom(float chance) {
		return new VXPredicateRandom(chance);
	}

}