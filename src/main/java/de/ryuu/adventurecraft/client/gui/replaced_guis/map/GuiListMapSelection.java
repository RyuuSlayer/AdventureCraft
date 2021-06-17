package de.ryuu.adventurecraft.client.gui.replaced_guis.map;

import com.google.common.collect.Lists;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiListMapSelection extends GuiListExtended {
    private static final Logger LOGGER = LogManager.getLogger();
    public final String worldPathName;
    private final MapSelector worldSelection;
    private final List<GuiListMapSelectionEntry> entries = Lists.newArrayList();
    /**
     * Index to the currently selected world
     */
    private int selectedIdx = -1;

    public GuiListMapSelection(MapSelector p_i46590_1_, Minecraft clientIn, int widthIn, int heightIn, int topIn,
                               int bottomIn, int slotHeightIn, String worldPathName) {
        super(clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.worldSelection = p_i46590_1_;
        this.worldPathName = worldPathName;
        this.refreshList();
    }

    public void refreshList() {
        ISaveFormat isaveformat = new AnvilSaveConverter(new File(this.mc.mcDataDir, this.worldPathName),
                this.mc.getDataFixer());
        List<WorldSummary> list;

        try {
            list = isaveformat.getSaveList();
        } catch (AnvilConverterException anvilconverterexception) {
            LOGGER.error("Couldn't load level list", anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"),
                    anvilconverterexception.getMessage()));
            return;
        }

        Collections.sort(list);

        for (WorldSummary worldsummary : list) {
            this.entries.add(new GuiListMapSelectionEntry(this, worldsummary,
                    new AnvilSaveConverter(new File(this.mc.mcDataDir, this.worldPathName), this.mc.getDataFixer())));
        }
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListMapSelectionEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    protected int getSize() {
        return this.entries.size();
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 20;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return super.getListWidth() + 50;
    }

    public void selectWorld(int idx) {
        this.selectedIdx = idx;
        this.worldSelection.selectWorld(this.getSelectedWorld());
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.selectedIdx;
    }

    @Nullable
    public GuiListMapSelectionEntry getSelectedWorld() {
        return this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
    }

    public MapSelector getGuiWorldSelection() {
        return this.worldSelection;
    }

}
