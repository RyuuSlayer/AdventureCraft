package de.ryuu.adventurecraft.client.render;

import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;

public interface IRenderable {

    void render(Minecraft mc, ClientProxy clientProxy, Tessellator tessellator, BufferBuilder vertexbuffer,
                double partialTicks);

}