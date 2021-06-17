package de.ryuu.adventurecraft.voxelator;

import de.ryuu.adventurecraft.voxelator.params.*;

public abstract class BrushParameter {
    public static final BrushParameter[] NO_PARAMETERS = new BrushParameter[0];
    // name
    private String name;

    public BrushParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    // type

    // Method to get type.
    public abstract BrushParameter.BPType getType();

    public IntegerBrushParameter asIntegerParameter() {
        return (IntegerBrushParameter) this;
    }

    // Methods for quick casting...

    public FloatBrushParameter asFloatParameter() {
        return (FloatBrushParameter) this;
    }

    public BlockstateBrushParameter asBlockstateParameter() {
        return (BlockstateBrushParameter) this;
    }

    public BooleanBrushParameter asBooleanParameter() {
        return (BooleanBrushParameter) this;
    }

    public ListBrushParameter asListParameter() {
        return (ListBrushParameter) this;
    }

    // The type of the parameter...
    public static enum BPType {
        // primitives
        INTEGER, FLOAT, BOOLEAN,

        // collections
        LIST,

        // minecrafts
        BLOCKSTATE,

        // adventurecraft
        xSHAPE, xFILTER, xACTION;
    }

}