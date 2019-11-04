package de.ryuum3gum1n.adventurecraft.voxelator.params;

import de.ryuum3gum1n.adventurecraft.voxelator.BrushParameter;

public final class ListBrushParameter extends BrushParameter {
	private final int maxEntries;
	private final BPType entryType;
	
	@Override public BPType getType() {
		return BPType.LIST;
	}
	
	public ListBrushParameter(String name, int maxEntries, BPType entryType) {
		super(name);
		this.maxEntries = maxEntries;
		this.entryType = entryType;
	}
	
	public int getMaxEntries() {
		return this.maxEntries;
	}
	
	public BPType getEntryType() {
		return this.entryType;
	}
}