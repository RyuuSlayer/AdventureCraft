package de.ryuum3gum1n.adventurecraft.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.invoke.IInvoke;
import de.ryuum3gum1n.adventurecraft.invoke.IInvokeSource;
import de.ryuum3gum1n.adventurecraft.network.packets.StringNBTCommandPacket;


public class PlayerMirror {
	public EntityPlayerMP player;
	public NBTTagCompound settings;
	public boolean trackInvokes;

	public PlayerMirror(EntityPlayerMP player) {
		this.player = player;
		this.settings = new NBTTagCompound();
	}

	public void construct(NBTTagCompound data) {

	}

	public void updateSettings(NBTTagCompound data) {
		settings.merge(data);
		trackInvokes = settings.getBoolean("invoke.tracker");
	}

	public void trackInvoke(IInvokeSource source, IInvoke invoke) {
		if(!trackInvokes) return;

		float[] colorS = new float[3];
		float[] colorT = new float[3];

		source.getInvokeColor(colorS);
		invoke.getColor(colorT);

		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setFloat("isr", colorS[0]);
		tagCompound.setFloat("isg", colorS[1]);
		tagCompound.setFloat("isb", colorS[2]);
		tagCompound.setFloat("itr", colorT[0]);
		tagCompound.setFloat("itg", colorT[1]);
		tagCompound.setFloat("itb", colorT[2]);
		AdventureCraft.network.sendTo(new StringNBTCommandPacket("client.debug.track.invoke", tagCompound), player);
	}

}