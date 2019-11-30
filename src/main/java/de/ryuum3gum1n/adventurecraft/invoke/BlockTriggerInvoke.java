package de.ryuum3gum1n.adventurecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class BlockTriggerInvoke implements IInvoke {
    public static final BlockTriggerInvoke ZEROINSTANCE = new BlockTriggerInvoke();
    public static final String TYPE = "BlockTriggerInvoke";
    private static final int[] ZEROBOUNDS = new int[6];
    int[] bounds = ZEROBOUNDS;
    EnumTriggerState triggerState;

    public BlockTriggerInvoke() {
        triggerState = EnumTriggerState.ON;
    }

    public int[] getBounds() {
        return bounds;
    }

    public EnumTriggerState getOnOff() {
        return triggerState;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void getColor(float[] color_out) {
        color_out[0] = 1.0f;
        color_out[1] = 0.5f;
        color_out[2] = 0.0f;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setIntArray("bounds", bounds == null ? new int[6] : bounds);
        compound.setInteger("state", triggerState.getIntValue());
    }

    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        triggerState = EnumTriggerState.get(compound.hasKey("state") ? compound.getInteger("state") : 1);
        bounds = compound.getIntArray("bounds");

        if (bounds == null)
            bounds = new int[6];
        else if (bounds.length != 6)
            bounds = Arrays.copyOf(bounds, 6);
    }

}