package de.ryuu.adventurecraft.client.gui.replaced_guis;

import de.ryuu.adventurecraft.client.gui.replaced_guis.map.MapCreator;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiReplacer { // It Rhymes! (No anymore, sry)

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (e.getGui() != null) {
            if (e.getGui().getClass() == GuiIngameMenu.class) {
                e.setGui(new NewIngameMenu());
            }
            if (e.getGui().getClass() == GuiCreateWorld.class) {
                try {
                    GuiScreen parentScreen = ObfuscationReflectionHelper.getPrivateValue(GuiCreateWorld.class,
                            (GuiCreateWorld) e.getGui(), "field_146332_f", "parentScreen");
                    if (parentScreen instanceof MapCreator) {
                        ((MapCreator) parentScreen).chunkProviderSettingsJson = ((GuiCreateWorld) e
                                .getGui()).chunkProviderSettingsJson;
                        e.setGui(parentScreen);
                    } else
                        e.setGui(new MapCreator());
                } catch (SecurityException | IllegalArgumentException e1) {
                    e1.printStackTrace();
                    e.setGui(new MapCreator());
                }
            }
            if (e.getGui() instanceof GuiMainMenu) {
                e.setGui(new CustomMainMenu());
            }
            if (e.getGui().getClass() == GuiGameOver.class) {
                try {
                    e.setGui(new NewGameOverScreen(ObfuscationReflectionHelper.getPrivateValue(GuiGameOver.class,
                            (GuiGameOver) e.getGui(), "field_184871_f", "causeOfDeathIn")));
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (e.getGui().getClass() == GuiMultiplayer.class) {
                System.err.println("Redirecting to somehow opened multiplayer menu to main menu");
                e.setGui(new CustomMainMenu());
            }
        }

//		System.out.println(e.getGui());
    }

}
