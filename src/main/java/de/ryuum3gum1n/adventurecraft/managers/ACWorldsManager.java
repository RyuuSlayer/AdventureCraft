package de.ryuum3gum1n.adventurecraft.managers;

import java.util.HashMap;

import net.minecraft.world.World;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;

public class ACWorldsManager {
	private AdventureCraft adventureCraft;
	public ACWorldsManager(AdventureCraft ac) {
		worldMap = new HashMap<World, ACWorldManager>();
		adventureCraft = ac;
	}

	private HashMap<World, ACWorldManager> worldMap;

	public synchronized void registerWorld(World world) {
		if(worldMap.containsKey(world)) {
			AdventureCraft.logger.error("WorldManager for THIS world is already registered -> " + world.toString());
			return;
		}

		ACWorldManager mng = new ACWorldManager(adventureCraft, world);
		mng.init();
		worldMap.put(world, mng);

		AdventureCraft.proxy.loadWorld(world);
	}

	public synchronized void unregisterWorld(World world) {
		AdventureCraft.proxy.unloadWorld(world);

		if(!worldMap.containsKey(world)) {
			AdventureCraft.logger.error("There is no WorldManager associated with THIS -> " + world.toString());
			return;
		}

		ACWorldManager mng = worldMap.remove(world);
		mng.dispose();
	}

	public ACWorldManager fetchManager(World world) {
		ACWorldManager worldManager = worldMap.get(world);

		if(worldManager == null) {
			registerWorld(world);
			worldManager = worldMap.get(world);
		}

		return worldManager;
	}

}