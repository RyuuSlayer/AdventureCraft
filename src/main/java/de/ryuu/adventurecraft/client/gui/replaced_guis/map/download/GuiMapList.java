package de.ryuu.adventurecraft.client.gui.replaced_guis.map.download;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;

public class GuiMapList extends GuiScreen {

    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * The screen to return to when this closes (always Main Menu).
     */
    protected final GuiScreen prevScreen;
    protected final String title = "Download Map";
    /**
     * Tooltip displayed a world whose version is different from this client's
     */
    private String worldVersTooltip;
    private GuiButtonExt selectButton;
    private GuiTextField searchField;
    private GuiMapListSelection selectionList;

    public GuiMapList(GuiScreen screenIn) {
        this.prevScreen = screenIn;

    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.searchField = new GuiTextField(2, fontRenderer, this.width / 2 + 40, 15, 100, 20);
        this.selectionList = new GuiMapListSelection(this, this.mc, this.width, this.height, 42, this.height - 64, 36,
                this.searchField);
        this.postInit();
    }

    @Override
    public void updateScreen() {
        // TODO Auto-generated method stub
        this.searchField.updateCursorCounter();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.selectionList.handleMouseInput();
    }

    @Override
    public void onGuiClosed() {
        // TODO Auto-generated method stub
        Keyboard.enableRepeatEvents(false);
    }

    public void postInit() {
        this.selectButton = this.addButton(
                new GuiButtonExt(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("More Details")));
        this.addButton(new GuiButtonExt(0, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("gui.cancel")));
        this.addButton(new GuiButtonExt(2, this.width / 2 - 130, this.height - 32, 100, 20, "Suggest Map"));
        this.selectButton.enabled = false;

    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for
     * buttons)
     */
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(prevScreen);
                break;
            case 1:
                // noinspection ConstantConditions
                selectionList.getSelectedWorld().showWorldInfo();
                break;// GuiMainMenu
            case 2:
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this,
                        "https://docs.google.com/forms/d/e/1FAIpQLSeKj7ADdys2tnl3iGQ7oCGCnrCp5QQhhAcIhDHZWcHFEJMkPg/viewform?usp=sf_link",
                        13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
                break;
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        // TODO Auto-generated method stub
        if (id == 13) {
            if (result) {
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(
                            "https://docs.google.com/forms/d/e/1FAIpQLSeKj7ADdys2tnl3iGQ7oCGCnrCp5QQhhAcIhDHZWcHFEJMkPg/viewform?usp=sf_link"));
                } catch (Throwable throwable) {
                    LOGGER.error("Couldn't open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.worldVersTooltip = null;
        this.selectionList.drawScreen(mouseX, mouseY, partialTicks);
        this.searchField.drawTextBox();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, "Search...", this.searchField.x, this.searchField.y - 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(fontRenderer,
                "Showing " + this.selectionList.entries.size()
                        + (this.selectionList.entries.size() == 1 ? " Map" : " Maps"),
                width / 2 + 30, height - 20, 16777215);

        if (this.worldVersTooltip != null) {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.worldVersTooltip)), mouseX, mouseY);
        }

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);

    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.selectionList.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Called back by selectionList when we call its drawScreen method, from ours.
     */
    public void setVersionTooltip(String p_184861_1_) {
        this.worldVersTooltip = p_184861_1_;
    }

    public void selectWorld(@Nullable GuiMapListEntry newGuiListWorldSelectionEntry) {
        this.selectButton.enabled = newGuiListWorldSelectionEntry != null;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // TODO Auto-generated method stub
        final String txt = searchField.getText();
        this.searchField.textboxKeyTyped(typedChar, keyCode);
        if (!txt.equals(searchField.getText()))
            this.selectionList.refreshList();
    }

}
