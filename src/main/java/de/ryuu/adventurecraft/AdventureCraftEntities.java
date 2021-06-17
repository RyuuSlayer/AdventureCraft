package de.ryuu.adventurecraft;

import de.ryuu.adventurecraft.entity.EntityMovingBlock;
import de.ryuu.adventurecraft.entity.EntityPoint;
import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import de.ryuu.adventurecraft.entity.projectile.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class AdventureCraftEntities {
    private static int ID = 0;

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

    private static void register(String name, Class<? extends Entity> entity, int trackingrange, int updatefrequency,
                                 boolean sendVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation("assets/adventurecraft", name), entity, "ac_" + name, ID++,
                "assets/adventurecraft", trackingrange, updatefrequency, sendVelocityUpdates);
    }
}
