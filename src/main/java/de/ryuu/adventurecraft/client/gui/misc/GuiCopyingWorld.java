package de.ryuu.adventurecraft.client.gui.misc;

import net.minecraft.client.gui.GuiScreen;

public class GuiCopyingWorld extends GuiScreen {
    private final String text;
    boolean drawn = false;

    public GuiCopyingWorld(String text) {
        this.text = text;
    }

    @Override
    public boolean doesGuiPauseGame() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub
        drawn = true;
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Moving world directory...", width / 2, height / 2, 16777215);
        drawCenteredString(fontRenderer, this.text, width / 2, height / 2 + 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
