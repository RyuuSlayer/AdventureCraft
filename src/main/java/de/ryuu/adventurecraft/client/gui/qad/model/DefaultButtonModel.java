package de.ryuu.adventurecraft.client.gui.qad.model;

import de.ryuu.adventurecraft.client.gui.qad.QADButton.ButtonModel;
import net.minecraft.util.ResourceLocation;

public class DefaultButtonModel implements ButtonModel {
    public String text;
    public ResourceLocation icon;

    public DefaultButtonModel(String txt, ResourceLocation icn) {
        this.text = txt;
        this.icon = icn;
    }

    public DefaultButtonModel(String txt) {
        this.text = txt;
        this.icon = null;
    }

    public DefaultButtonModel(ResourceLocation resourceLocation) {
        this.text = "";
        this.icon = resourceLocation;
    }

    @Override
    public void onClick() {
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String newText) {
        this.text = newText;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public void setIcon(ResourceLocation newIcon) {
        this.icon = newIcon;
    }
}