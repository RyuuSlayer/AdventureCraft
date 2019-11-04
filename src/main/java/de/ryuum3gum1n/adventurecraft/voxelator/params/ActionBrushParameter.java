package de.ryuum3gum1n.adventurecraft.voxelator.params;

import de.ryuum3gum1n.adventurecraft.voxelator.BrushParameter;
import de.ryuum3gum1n.adventurecraft.voxelator.Voxelator;
import de.ryuum3gum1n.adventurecraft.voxelator.Voxelator.ActionFactory;

public final class ActionBrushParameter extends BrushParameter {
	
	@Override public BPType getType() {
		return BPType.xACTION;
	}
	
	public ActionBrushParameter(String name) {
		super(name);
	}
	
	public ActionFactory getDefault() {
		return Voxelator.actions.get("replace");
	}
}