package de.ryuu.adventurecraft.tileentity;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.blocks.ACTileEntity;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.invoke.IInvoke;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacketClient;
import de.ryuu.adventurecraft.util.NBTHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class URLBlockTileEntity extends ACTileEntity {
    String url;
    String selector;

    public URLBlockTileEntity() {
        url = "https://www.reddit.com/r/adventurecraft/";
        selector = "@a";
    }

    @Override
    public void getInvokes(List<IInvoke> invokes) {
        // none
    }

    @Override
    public void getInvokeColor(float[] color) {
        color[0] = 1.0f;
        color[1] = 1.0f;
        color[2] = 0.8f;
    }

    @Override
    public String getName() {
        return "URLBlock@" + this.getPos();
    }

    @Override
    public void readFromNBT_do(NBTTagCompound comp) {
        url = comp.getString("url");
        selector = comp.getString("selector");
    }

    @Override
    public NBTTagCompound writeToNBT_do(NBTTagCompound comp) {
        comp.setString("url", url);
        comp.setString("selector", selector);
        return comp;
    }

    public String getURL() {
        return url;
    }

    public String getSelector() {
        return selector;
    }

    public void trigger(EnumTriggerState triggerState) {
        if (triggerState.getBooleanValue()) {
            List<EntityPlayerMP> players = null;
            try {
                players = EntitySelector.matchEntities(this, selector, EntityPlayerMP.class);
            } catch (CommandException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StringNBTCommandPacketClient command = new StringNBTCommandPacketClient();
            command.command = "client.gui.openurl";
            command.data = NBTHelper.newSingleStringCompound("url", url);

            for (EntityPlayerMP player : players) {
                AdventureCraft.network.sendTo(command, player);
            }
        }
    }
}