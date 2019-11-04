package de.ryuum3gum1n.adventurecraft.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ITemporaryRenderable extends IRenderable {

	boolean canRemove();

}