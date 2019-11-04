package de.ryuum3gum1n.adventurecraft.voxelator.params;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import de.ryuum3gum1n.adventurecraft.voxelator.BrushParameter;

public final class BlockstateBrushParameter extends BrushParameter {
	private final IBlockState _default;

	@Override
	public BPType getType() {
		return BPType.BLOCKSTATE;
	}

	public BlockstateBrushParameter(String name, IBlockState _default) {
		super(name);
		this._default = _default;
	}

	public BlockstateBrushParameter(String name, Block _default) {
		super(name);
		this._default = _default.getDefaultState();
	}

	public IBlockState getDefault() {
		return this._default;
	}
}