package de.ryuu.adventurecraft.client.gui.qad.model.nbtlist;

import de.ryuu.adventurecraft.client.gui.qad.QADTextField.TextFieldModel;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class NBTListStringTextFieldModel implements TextFieldModel {
    NBTTagList list;
    int index;
    String text;

    public NBTListStringTextFieldModel(NBTTagList li, int ix) {
        this.list = li;
        this.index = ix;
        this.text = list.getStringTagAt(index);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.list.set(index, new NBTTagString(text));
    }

    @Override
    public int getTextLength() {
        return text.length();
    }

    @Override
    public char getCharAt(int i) {
        return text.charAt(i);
    }

    @Override
    public int getTextColor() {
        return 0xFFFFFFFF;
    }

    @Override
    public void setTextColor(int color) {
        // nope
    }

}