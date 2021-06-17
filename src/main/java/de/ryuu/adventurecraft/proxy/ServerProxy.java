package de.ryuu.adventurecraft.proxy;

import de.ryuu.adventurecraft.server.ServerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ServerProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public boolean isBuildMode() {
        // A bit inaccurate but it should work if player did not hack into creative
        return FMLCommonHandler.instance().getMinecraftServerInstance().getGameType().isCreative();
    }

    // XXX: THIS METHOD IS NEVER CALLED
    @Override
    public void tick(TickEvent event) {
        super.tick(event);
    }

    // XXX: THIS METHOD IS NEVER CALLED
    @Override
    public void tickWorld(WorldTickEvent event) {
        super.tickWorld(event);
    }

    @Override
    public NBTTagCompound getSettings(EntityPlayer playerIn) {
        return ServerHandler.getServerMirror(null).playerList().getPlayer((EntityPlayerMP) playerIn).settings;
    }

    public WorldInfo getWorldInfo() {
        // TODO Auto-generated method stub
        return Minecraft.getMinecraft().world.getWorldInfo();
    }

}