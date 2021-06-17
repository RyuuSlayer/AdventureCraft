package de.ryuu.adventurecraft.voxelator.params;

import de.ryuu.adventurecraft.voxelator.BrushParameter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public final class BlockstateBrushParameter extends BrushParameter {
    private final IBlockState _default;

    public BlockstateBrushParameter(String name, IBlockState _default) {
        super(name);
        this._default = _default;
    }

    public BlockstateBrushParameter(String name, Block _default) {
        super(name);
        this._default = _default.getDefaultState();
    }

    @Override
    public BPType getType() {
        return BPType.BLOCKSTATE;
    }

    public IBlockState getDefault() {
        return this._default;
    }
}