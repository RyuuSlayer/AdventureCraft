package de.ryuum3gum1n.adventurecraft.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.blocks.ACTileEntity;
import de.ryuum3gum1n.adventurecraft.invoke.EnumTriggerState;
import de.ryuum3gum1n.adventurecraft.invoke.FileScriptInvoke;
import de.ryuum3gum1n.adventurecraft.invoke.IInvoke;
import de.ryuum3gum1n.adventurecraft.invoke.IScriptInvoke;
import de.ryuum3gum1n.adventurecraft.invoke.Invoke;

public class ScriptBlockTileEntity extends ACTileEntity {
	IScriptInvoke scriptInvoke;

	public ScriptBlockTileEntity() {
		scriptInvoke = new FileScriptInvoke();
	}

	@Override
	public void init() {

	}

	public void triggerInvokeScript() {
		Invoke.invoke(scriptInvoke, this, null, EnumTriggerState.IGNORE);
	}

	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if (command.equals("reload")) {
			scriptInvoke.reloadScript();
			return;
		}

		if (command.equals("execute")) {
			triggerInvokeScript();
			return;
		}

		if (command.equals("reloadexecute")) {
			scriptInvoke.reloadScript();
			triggerInvokeScript();
			return;
		}

		super.commandReceived(command, data);
	}

	@Override
	public String getName() {
		return "ScriptBlock@" + pos;
	}

	@Override
	public String toString() {
		return "ScriptBlockTileEntity:{" + scriptInvoke + ", " + getInvokeScriptScope() + "}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(scriptInvoke);
	}

	public IScriptInvoke getInvoke() {
		return scriptInvoke;
	}

	@Override
	public void readFromNBT_do(NBTTagCompound compound) {
		scriptInvoke = IInvoke.Serializer.readSI(compound.getCompoundTag("scriptInvoke"));
		scriptInvoke.reloadScript();
	}

	@Override
	public NBTTagCompound writeToNBT_do(NBTTagCompound compound) {
		compound.setTag("scriptInvoke", IInvoke.Serializer.write(scriptInvoke));
		return compound;
	}

	public String getScriptName() {
		AdventureCraft.logger.info("getScriptName() " + scriptInvoke.getScriptName());
		return scriptInvoke == null ? "" : scriptInvoke.getScriptName();
	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 1.0f;
		color[1] = 0.5f;
		color[2] = 0.0f;
	}

}