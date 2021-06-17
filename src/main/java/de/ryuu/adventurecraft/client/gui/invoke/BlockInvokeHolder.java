package de.ryuu.adventurecraft.client.gui.invoke;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.ClientNetworkHandler;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class BlockInvokeHolder implements IInvokeHolder {
    BlockPos blockPosition;
    String invokeName;

    public BlockInvokeHolder(BlockPos position, String invokeName) {
        this.blockPosition = position;
        this.invokeName = invokeName;
    }

    @Override
    public void sendInvokeUpdate(NBTTagCompound newInvokeData) {
        String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);
        NBTTagCompound commandData = new NBTTagCompound();
        commandData.setTag(invokeName, newInvokeData);
        AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
    }

    @Override
    public void sendCommand(String command, NBTTagCompound commandData) {
        String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);

        if (commandData == null)
            commandData = new NBTTagCompound();
        commandData.setString("command", command);

        // Send command
        AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));

        // Close whatever gui is open
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

    @Override
    public void switchInvokeType(String type) {
        String commandString = ClientNetworkHandler.makeBlockDataMergeCommand(blockPosition);
        NBTTagCompound commandData = new NBTTagCompound();
        NBTTagCompound invokeData = new NBTTagCompound();
        invokeData.setString("type", type);
        commandData.setTag(invokeName, invokeData);
        AdventureCraft.network.sendToServer(new StringNBTCommandPacket(commandString, commandData));
    }

}