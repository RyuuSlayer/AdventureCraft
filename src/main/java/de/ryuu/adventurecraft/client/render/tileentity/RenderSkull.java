package de.ryuu.adventurecraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class RenderSkull extends FastTESR<TileEntitySkull> {


    public void renderTileEntityFast(double x, double y, double z) {

        TileEntitySkull SKULL = new TileEntitySkull();
        SKULL.setPlayerProfile(Minecraft.getMinecraft().getSession().getProfile());
        OpenGlHelper.glGenFramebuffers();
        super.render(SKULL, x, y, z, 0F, 0, 0F);
    }

}

