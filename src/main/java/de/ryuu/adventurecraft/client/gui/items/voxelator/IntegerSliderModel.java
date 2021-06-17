package de.ryuu.adventurecraft.client.gui.items.voxelator;

import de.ryuu.adventurecraft.client.gui.qad.QADSlider.SliderModel;
import de.ryuu.adventurecraft.voxelator.params.IntegerBrushParameter;
import net.minecraft.nbt.NBTTagCompound;

public class IntegerSliderModel implements SliderModel<Integer> {

    private final int min;
    private final int max;
    private final String name;
    private int current;
    private float value;
    private NBTTagCompound tag;

    public IntegerSliderModel(NBTTagCompound compound, IntegerBrushParameter param) {
        this.tag = compound;
        min = param.getMinimum();
        max = param.getMaximum();
        name = param.getName();
        setValue(param.getDefault());
        if (compound.hasKey(name))
            current = compound.getInteger(name);
    }

    @Override
    public Integer getValue() {
        return current;
    }

    @Override
    public void setValue(Integer value) {
        current = value;
        tag.setInteger(name, value);
    }

    @Override
    public String getValueAsText() {
        return name + ": " + current;
    }

    @Override
    public float getSliderValue() {
        return value;
    }

    @Override
    public void setSliderValue(float sliderValue) {
        this.value = sliderValue;
        setValue((int) (sliderValue * (max + min) + min));
    }

}