package de.ryuu.adventurecraft.client.gui.qad.model;

import de.ryuu.adventurecraft.client.gui.qad.QADSlider.SliderModel;
import net.minecraft.util.math.MathHelper;

public class DefaultSliderModel implements SliderModel<Integer> {
    private int maximum;
    private int current;
    private float value;

    public DefaultSliderModel(int initial, int maximum) {
        this.current = initial;
        this.maximum = maximum;
        this.value = (float) current / (float) maximum;
    }

    @Override
    public Integer getValue() {
        return Integer.valueOf(current);
    }

    @Override
    public void setValue(Integer value) {
        this.current = value.intValue();

        if (current < 0)
            current = 0;
        if (current > maximum)
            current = maximum;
    }

    @Override
    public String getValueAsText() {
        return Integer.toString(current);
    }

    @Override
    public float getSliderValue() {
        return value;
    }

    @Override
    public void setSliderValue(float newSliderValue) {
        value = Math.round(newSliderValue * maximum) / (float) maximum;
        current = MathHelper.clamp((int) (newSliderValue * maximum + 0.5f), 0, maximum);
    }
}