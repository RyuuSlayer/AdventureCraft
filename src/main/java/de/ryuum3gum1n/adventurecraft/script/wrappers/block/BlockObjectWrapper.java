package de.ryuum3gum1n.adventurecraft.script.wrappers.block;

import java.util.List;

import net.minecraft.block.Block;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.script.wrappers.IObjectWrapper;

public class BlockObjectWrapper implements IObjectWrapper {
	private Block block;

	public BlockObjectWrapper(Block block) {
		this.block = block;
	}

	public String getName() {
		return block.getLocalizedName();
	}

	public String getInternalName() {
		return block.getUnlocalizedName();
	}

	public BlockStateObjectWrapper getDefaultBlockState() {
		return new BlockStateObjectWrapper(block.getDefaultState());
	}

	@Override
	public Block internal() {
		return block;
	}

	@Override
	public boolean equals(Object obj) {
		return block.equals(obj);
	}

	@Override
	public int hashCode() {
		return block.hashCode();
	}

	@Override
	public List<String> getOwnPropertyNames() {
		return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
	}

}