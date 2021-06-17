package de.ryuu.adventurecraft.voxelator.params;

import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.Voxelator;

public final class ActionBrushParameter extends BrushParameter {

    public ActionBrushParameter(String name) {
        super(name);
    }

    @Override
    public BPType getType() {
        return BPType.xACTION;
    }

    public Voxelator.ActionFactory getDefault() {
        return Voxelator.actions.get("replace");
    }
}