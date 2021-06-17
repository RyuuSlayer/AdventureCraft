package de.ryuu.adventurecraft.client.gui.qad.model.nbtcompound;

import de.ryuu.adventurecraft.client.gui.qad.QADTextField.TextFieldModel;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTByteTextFieldModel implements TextFieldModel {
    String text;
    String tagKey;
    NBTTagCompound tagParent;
    boolean valid = true;

    public NBTByteTextFieldModel(String tagKey, NBTTagCompound tagParent) {
        this.tagKey = tagKey;
        this.tagParent = tagParent;
        this.text = Byte.toString(tagParent.getByte(tagKey));
    }

    @Override
    public int getTextLength() {
        return text.length();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String newText) {
        text = newText;
        try {
            byte value = Byte.parseByte(text);
            tagParent.setByte(tagKey, value);
            valid = true;
        } catch (NumberFormatException e) {
            valid = false; // :(
        }
    }

    @Override
    public char getCharAt(int i) {
        return text.charAt(i);
    }

    @Override
    public int getTextColor() {
        return valid ? 0xFFFFFFFF : 0xFFFF7070;
    }

    @Override
    public void setTextColor(int color) {
        // nope
    }
}