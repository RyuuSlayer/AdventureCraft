package de.ryuum3gum1n.adventurecraft.blocks;

import net.minecraft.nbt.NBTTagCompound;

public interface ACIBlockCommandReceiver {
	void commandReceived(String command, NBTTagCompound data);
}