package de.ryuu.adventurecraft.client.gui.misc;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.gui.misc.GuiItemTypeSelection.ItemTypeDataLink;
import de.ryuu.adventurecraft.client.gui.qad.*;
import de.ryuu.adventurecraft.client.gui.qad.QADNumberTextField.NumberType;
import de.ryuu.adventurecraft.client.gui.qad.model.nbtcompound.NBTByteTextFieldModel;
import de.ryuu.adventurecraft.client.gui.qad.model.nbtcompound.NBTShortTextFieldModel;
import de.ryuu.adventurecraft.client.gui.qad.model.nbtcompound.NBTStringTextFieldModel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;

public class GuiItemStackEditor extends QADGuiScreen {
    QADButton buttonDone;
    QADTextField fieldType;
    QADNumberTextField fieldCount;
    QADNumberTextField fieldDamage;
    private NBTTagCompound stack;

    public GuiItemStackEditor(NBTTagCompound slot) {
        this.stack = slot;
    }

    @Override
    public void buildGui() {
        {
            QADPanel panel = new QADPanel();
            panel.setPosition(0, 0);
            panel.setSize(9999, 22);
            panel.setBackgroundColor(0);
            addComponent(panel);
        }

        addComponent(QADFACTORY.createLabel("Item(-stack) Editor", 2, 2));

        buttonDone = addComponent(QADFACTORY.createButton("Done", 0, 0, 40, new Runnable() {
            @Override
            public void run() {
                if (getBehind() != null && getBehind() instanceof QADGuiScreen) {
                    ((QADGuiScreen) getBehind()).resetGuiScreen();
                }
                displayGuiScreen(getBehind());
            }
        }));

        // builder.append(slot.getString("id")).append("/");
        // builder.append(slot.getShort("Damage")).append(" x");
        // builder.append(slot.getByte("Count"));

        {
            addComponent(QADFACTORY.createLabel("Type", 2, 24 + 24 * 0 + 6));
            fieldType = new QADTextField(fontRenderer, 80, 24 + 24 * 0, 140, 20);
            fieldType.setText(stack.getString("id"));
            fieldType.setModel(new NBTStringTextFieldModel("id", stack));
            addComponent(fieldType);

            addComponent(QADFACTORY.createButton("?", 2 + 80 + 2 + 140 + 2, 24 + 24 * 0, 20, new Runnable() {
                @Override
                public void run() {
                    final GuiScreen behindPre = GuiItemStackEditor.this.getBehind();
                    final GuiScreen returnScreen = GuiItemStackEditor.this.returnScreen;
                    GuiItemStackEditor.this.setBehind(null);
                    GuiItemStackEditor.this.returnScreen = null;

                    displayGuiScreen(new GuiItemTypeSelection(GuiItemStackEditor.this, new ItemTypeDataLink() {
                        @Override
                        public void setType(String identifier) {
                            fieldType.setText(identifier);
                        }
                    }));

                    AdventureCraft.proxy.asClient().scheduleClientTickTask(new Runnable() {
                        @Override
                        public void run() {
                            GuiItemStackEditor.this.setBehind(behindPre);
                            GuiItemStackEditor.this.returnScreen = returnScreen;
                        }
                    });
                }
            }));
        }

        {
            addComponent(QADFACTORY.createLabel("Count", 2, 24 + 24 * 1 + 6));
            Number stackCount = stack.getByte("Count");
            QADNumberTextField fieldCount = new QADNumberTextField(fontRenderer, 80, 24 + 24 * 1, 140, 20, stackCount,
                    NumberType.INTEGER);
            fieldCount.setRange(0, 64 + 1);
            fieldCount.setModel(new NBTByteTextFieldModel("Count", stack));
            addComponent(fieldCount);
        }
        {
            addComponent(QADFACTORY.createLabel("Damage", 2, 24 + 24 * 2 + 6));
            Number stackDamage = stack.getShort("Damage");
            QADNumberTextField fieldDamage = new QADNumberTextField(fontRenderer, 80, 24 + 24 * 2, 140, 20, stackDamage,
                    NumberType.INTEGER);
            fieldDamage.setRange(Short.MIN_VALUE - 1, Short.MAX_VALUE + 1);
            fieldDamage.setModel(new NBTShortTextFieldModel("Damage", stack));
            addComponent(fieldDamage);
        }

    }

    @Override
    public void layoutGui() {
        buttonDone.setX(width - 40);
    }

}