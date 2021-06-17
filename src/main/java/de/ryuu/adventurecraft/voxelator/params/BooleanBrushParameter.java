package de.ryuu.adventurecraft.voxelator.params;

import de.ryuu.adventurecraft.voxelator.BrushParameter;

public final class BooleanBrushParameter extends BrushParameter {
    private final boolean _default;

    public BooleanBrushParameter(String name, boolean _default) {
        super(name);
        this._default = _default;
    }

    @Override
    public BPType getType() {
        return BPType.BOOLEAN;
    }

    public boolean getDefault() {
        return this._default;
    }
}