package de.ryuu.adventurecraft.voxelator.params;

import de.ryuu.adventurecraft.voxelator.BrushParameter;

public final class ListBrushParameter extends BrushParameter {
    private final int maxEntries;
    private final BPType entryType;

    public ListBrushParameter(String name, int maxEntries, BPType entryType) {
        super(name);
        this.maxEntries = maxEntries;
        this.entryType = entryType;
    }

    @Override
    public BPType getType() {
        return BPType.LIST;
    }

    public int getMaxEntries() {
        return this.maxEntries;
    }

    public BPType getEntryType() {
        return this.entryType;
    }
}