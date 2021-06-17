package de.ryuu.adventurecraft.entity.NPC.quest;

import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class ItemQuest extends NPCQuest {

    private ItemStack request;

    public ItemQuest(ItemStack request, String start_message, String ongoing_message, String end_message) {
        super(start_message, ongoing_message, end_message);
        this.request = request;
    }

    @Override
    public void update(EntityPlayerMP player, EntityNPC npc) {
        setFinished(player.inventory.hasItemStack(request));
    }

}