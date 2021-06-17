package de.ryuu.adventurecraft.util;

import de.ryuu.adventurecraft.AdventureCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;

public class ACMusicTicker extends MusicTicker {

    private final Minecraft mc;

    public ACMusicTicker(Minecraft mc) {
        super(mc);
        this.mc = mc;
    }

    @Override
    public void playMusic(MusicType song) {
        if (mc.world != null && mc.player != null && (mc.player.getEntityData().getBoolean("no-music")
                || !AdventureCraft.proxy.asClient().gamerules.getBoolean("ac_playDefaultMusic"))) {
            return;
        }
        super.playMusic(song);
    }

    @Override
    public void update() {
        if (mc.world != null && mc.player != null && (mc.player.getEntityData().getBoolean("no-music")
                || !AdventureCraft.proxy.asClient().gamerules.getBoolean("ac_playDefaultMusic"))) {
            // stopMusic(); TODO
        }
        super.update();
    }

}