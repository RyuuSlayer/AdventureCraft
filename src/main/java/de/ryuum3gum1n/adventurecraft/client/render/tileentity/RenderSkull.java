package de.ryuum3gum1n.adventurecraft.client.render.tileentity;

import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class RenderSkull extends FastTESR<TileEntitySkull>{
	
	
	public void renderTileEntityFast(double x, double y, double z) {
		
		TileEntitySkull SKULL = new TileEntitySkull();
		SKULL.setPlayerProfile(Minecraft.getMinecraft().getSession().getProfile());
		OpenGlHelper.glGenFramebuffers();
		super.render(SKULL, x, y, z, 0F, 0, 0F);	
	}
	
}

