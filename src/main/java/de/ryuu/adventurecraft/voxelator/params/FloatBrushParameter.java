package de.ryuu.adventurecraft.voxelator.params;

import de.ryuu.adventurecraft.voxelator.BrushParameter;

public final class FloatBrushParameter extends BrushParameter {
    private final float min;
    private final float max;
    private final float _default;

    public FloatBrushParameter(String name, float minimum, float maximum, float _default) {
        super(name);
        this.min = minimum;
        this.max = maximum;
        this._default = _default;
    }

    @Override
    public BPType getType() {
        return BPType.FLOAT;
    }

    public float getMinimum() {
        return this.min;
    }

    public float getMaximum() {
        return this.max;
    }

    public float getDefault() {
        return this._default;
    }
}