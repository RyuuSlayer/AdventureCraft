package de.ryuum3gum1n.adventurecraft.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * This material defines a invisible, indestructible, immovable block.
 */
public class ACAdminiumMaterial extends Material {

	public static final Material instance = new ACAdminiumMaterial();

	public ACAdminiumMaterial() {
		super(MapColor.AIR);
		setImmovableMobility();
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public boolean blocksLight() {
		return true;
	}

	@Override
	public boolean blocksMovement() {
		return true;
	}

	@Override
	public boolean getCanBurn() {
		return false;
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

	@Override
	public boolean isOpaque() {
		return true;
	}

	@Override
	public boolean isToolNotRequired() {
		return false;
	}

}