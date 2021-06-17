package de.ryuu.adventurecraft.client.gui.replaced_guis.map.download;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class GuiMapListEntry implements GuiListExtended.IGuiListEntry {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft client;
    private final GuiMapList worldSelScreen;
    private final GuiMapListSelection containingListSel;
    private final DownloadableMap map;
    private long lastClickTime;

    public GuiMapListEntry(GuiMapListSelection newGuiListWorldSelection, DownloadableMap map) {
        this.containingListSel = newGuiListWorldSelection;
        this.worldSelScreen = newGuiListWorldSelection.getGuiWorldSelection();
        this.client = Minecraft.getMinecraft();
        this.map = map;
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
                          boolean isSelected, float partialTicks) {
        String s = map.name;
        if (new File("." + File.separator + "saves" + File.separator + ".AC_DOWNLOADS" + File.separator
                + DownloadZip.getFileName(map.dlURL)).exists()) {
            s = s + TextFormatting.GREEN + " (Already downloaded)";
        }
        String s1 = map.description.split("\n")[0];
        String s2 = "Author: " + map.author + " | Requires additional mods? "
                + (map.additionalModsRequired ? "Yes" : "No");

        this.client.fontRenderer.drawString(s, x + 3, y + 1, 16777215);
        this.client.fontRenderer.drawString(s1, x + 3, y + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
        this.client.fontRenderer.drawString(s2, x + 3,
                y + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
    }

    /**
     * Called when the mouse is clicked within this entry. Returning true means that
     * something within this entry was clicked and the list should not be dragged.
     */
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        this.containingListSel.selectWorld(slotIndex);

        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.showWorldInfo();
            return true;
        } else {
            this.lastClickTime = Minecraft.getSystemTime();
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent,
     * relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
    }

    public void showWorldInfo() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiDLMapInfo(this.worldSelScreen, map));
    }
}
