package de.ryuu.adventurecraft.client.gui;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.gui.vcui.VCUIComponent;
import de.ryuu.adventurecraft.client.gui.vcui.VCUIRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * A class that is to be used to simplify GUIScreen creation.
 **/
@SideOnly(Side.CLIENT)
public class ACGuiScreen extends GuiScreen {
    public static final VCUIRenderer instance = new VCUIRenderer();
    private VCUIComponent root;

    public ACGuiScreen() {
        super.allowUserInput = false;
        root = new VCUIComponent();
        buildGui(root);
    }

    public void buildGui(VCUIComponent root) {
        // Don't do anything, let the extended classes do their thing!
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    /** ********************************* **/
    /**                                   **/
    /** Everything following is final and **/
    /** should not be overriden. **/
    /**                                   **/
    /**
     * ********************************
     **/

    @Override
    public final void initGui() {
        AdventureCraft.logger.info("Gui.init() -> " + this.getClass().getName());
        layoutGui();
    }

    private final void layoutGui() {
        root.position.x = 0;
        root.position.y = 0;
        root.size.x = width;
        root.size.y = height;

        if (root.layoutManager != null) {
            root.layoutManager.accept(root);
        }
    }

    @Override
    public final void onGuiClosed() {
        AdventureCraft.logger.info("Gui.close() -> " + this.getClass().getName());
    }

    @Override
    protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        root.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected final void mouseReleased(int mouseX, int mouseY, int state) {
        root.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        root.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public final void keyTyped(char typedChar, int typedCode) {
        if (typedCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            mc.inGameHasFocus = true;
            return;
        }

        root.keyTyped(typedChar, typedCode);
    }

    @Override
    public final void updateScreen() {
        root.update();
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.fontRenderer != null)
            instance.setCurrentScreen(this, this.zLevel, this.fontRenderer, this.itemRender);
        instance.drawDefaultBackground();

        if (Mouse.isInsideWindow() && Boolean.TRUE.booleanValue()) {
            final int color = 0x1ACCEEFF;
            instance.drawHorizontalLine(0, width, mouseY, color);
            instance.drawVerticalLine(mouseX, -1, height, color);
        }

        if (Boolean.FALSE.booleanValue()) {
            final int H = 128;
            instance.drawGradientRectangle(0, 0, width, H, 0xFF000000, 0);
            instance.drawGradientRectangle(0, height - H, width, height, 0, 0xFF000000);
        }

        root.draw();
    }

}