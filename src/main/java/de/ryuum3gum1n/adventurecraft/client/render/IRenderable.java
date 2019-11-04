package de.ryuum3gum1n.adventurecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;

public interface IRenderable {

	void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, BufferBuilder vertexbuffer,
			double partialTicks);

}