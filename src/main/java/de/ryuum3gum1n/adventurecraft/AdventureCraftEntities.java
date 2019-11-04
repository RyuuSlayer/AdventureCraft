package de.ryuum3gum1n.adventurecraft;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import de.ryuum3gum1n.adventurecraft.entity.EntityMovingBlock;
import de.ryuum3gum1n.adventurecraft.entity.EntityPoint;
import de.ryuum3gum1n.adventurecraft.entity.NPC.EntityNPC;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityBomb;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityBombArrow;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityBoomerang;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityBullet;
import de.ryuum3gum1n.adventurecraft.entity.projectile.EntityKnife;

public class AdventureCraftEntities {
	public static void init() {
		register("point", EntityPoint.class, 256, 20, false);
		register("bomb", EntityBomb.class, 128, 1, true);
		register("npc", EntityNPC.class, 128, 1, true);
		register("bullet", EntityBullet.class, 128, 1, true);
		register("bombarrow", EntityBombArrow.class, 128, 1, true);
		register("boomerang", EntityBoomerang.class, 128, 1, true);
		register("knife", EntityKnife.class, 128, 1, true);
		register("movingblock", EntityMovingBlock.class, 128, 1, true);
	}
	
	private static int ID = 0;
	
	private static void register(String name, Class<? extends Entity> entity, int trackingrange, int updatefrequency, boolean sendVelocityUpdates){
		EntityRegistry.registerModEntity(new ResourceLocation("adventurecraft", name), entity, "ac_" + name, ID++, "adventurecraft", trackingrange, updatefrequency, sendVelocityUpdates);
	}
}
