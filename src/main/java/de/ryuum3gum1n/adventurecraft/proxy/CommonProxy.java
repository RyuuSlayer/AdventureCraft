package de.ryuum3gum1n.adventurecraft.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.managers.ACWorldManager;

public abstract class CommonProxy {
	@SideOnly(Side.CLIENT)
	public final ClientProxy asClient() {
		return (ClientProxy) this;
	}
	public final ServerProxy asServer() {
		return (ServerProxy) this;
	}

	/** The one and only instance of 'adventurecraft'. **/
	public AdventureCraft adventureCraft;

	public void preInit(FMLPreInitializationEvent event) {};

	public void init(FMLInitializationEvent event) {};

	public void postInit(FMLPostInitializationEvent event) {}

	public boolean isBuildMode() {
		return true; // Just say its true!
	}

	public void tick(TickEvent event) {
		// We don't do anything, but the proxy-implementations do!
		// System.out.println("TICKING -> @" + event);
	}

	public void tickWorld(WorldTickEvent event) {
		ACWorldManager mng = AdventureCraft.worldsManager.fetchManager(event.world);

		if(mng == null) {
			AdventureCraft.logger.error("No WorldManager for @" + event.world.hashCode());
			return;
		}

		mng.tickWorld(event);
	}

	public void unloadWorld(World world) {

	}

	public void loadWorld(World world) {

	}

	public NBTTagCompound getSettings(EntityPlayer playerIn) {
		return new NBTTagCompound();
	}

}