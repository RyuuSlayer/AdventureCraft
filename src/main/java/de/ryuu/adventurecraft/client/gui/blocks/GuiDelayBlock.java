package de.ryuu.adventurecraft.client.gui.blocks;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.ClientNetworkHandler;
import de.ryuu.adventurecraft.client.gui.invoke.BlockInvokeHolder;
import de.ryuu.adventurecraft.client.gui.invoke.InvokePanelBuilder;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.client.gui.qad.QADSlider;
import de.ryuu.adventurecraft.client.gui.qad.model.DefaultSliderModel;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.tileentity.DelayBlockTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class GuiDelayBlock extends QADGuiScreen {
    DelayBlockTileEntity tileEntity;
    QADSlider slider;

    public GuiDelayBlock(DelayBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void buildGui() {
        final BlockPos position = tileEntity.getPos();

        addComponent(
                new QADLabel("Delay Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
        InvokePanelBuilder.build(this, this, 2, 16, tileEntity.getInvoke(),
                new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);

        final int maximum = 20 * 10;
        slider = addComponent(new QADSlider(new DefaultSliderModel(tileEntity.getDelayValue(), maximum)));
        slider.setWidth(width - 2 - 2);
        slider.setX(2);
        slider.setY(16 + 2 + 20 + 2);
        slider.setTooltip("ylock", "Delay in ticks.", "Max: " + maximum);
        slider.setSliderAction(new Runnable() {
            @Override
            public void run() {
                int newValue = (int) (slider.getSliderValue() * maximum);
                String commandString = ClientNetworkHandler.makeBlockCommand(position);
                NBTTagCompound commandData = new NBTTagCompound();
                commandData.setString("command", "set");
                commandData.setInteger("delay", MathHelper.clamp(newValue, 1, maximum));
                AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
            }
        });
    }

    @Override
    public void layoutGui() {
        slider.setWidth(width - 2 - 2);
    }

}