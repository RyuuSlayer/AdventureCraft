package de.ryuum3gum1n.adventurecraft.client.gui.replaced_guis.save;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Modified version of GuiWorldSelection<br>
 * this was required because of reflection not working<br>
 * TODO: Finish {@linkplain adventurecraft.client.gui.misc.GuiWorldInfo
 * GuiWorldInfo}
 *
 * @author ErdbeerbaerLP
 */
public class SaveCreator extends GuiScreen {

	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * The screen to return to when this closes (always Main Menu).
	 */
	protected final GuiScreen prevScreen;
	protected String title = "Select Map";
	/**
	 * Tooltip displayed a world whose version is different from this client's
	 */
	private String worldVersTooltip;
	private GuiButtonExt selectButton;
	private GuiListSaveCreator selectionList;
	/**
	 * Used to determine the world´s folder
	 */
	private final String worldPathName = "saves/.AC_MAPS";

	public SaveCreator(GuiScreen screenIn) {
		this.prevScreen = screenIn;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when
	 * the GUI is displayed and when the window resizes, the buttonList is cleared
	 * beforehand.
	 */
	public void initGui() {
		this.title = I18n.format("Select Map");
		this.selectionList = new GuiListSaveCreator(this, this.mc, this.width, this.height, 32, this.height - 64, 36,
				this.worldPathName);
		this.postInit();
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.selectionList.handleMouseInput();
	}

	public void postInit() {
		this.selectButton = this.addButton(
				new GuiButtonExt(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("Play selected Map")));
		this.addButton(new GuiButtonExt(0, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("gui.cancel")));
		this.selectButton.enabled = false;

	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			GuiListSaveCreatorEntry guilistworldselectionentry = this.selectionList.getSelectedWorld();

			if (button.id == 1) {
				if (guilistworldselectionentry != null) {
					guilistworldselectionentry.joinWorld();
				}
			} else if (button.id == 0) {
				this.mc.displayGuiScreen(this.prevScreen);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.worldVersTooltip = null;
		this.selectionList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.worldVersTooltip != null) {
			this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.worldVersTooltip)), mouseX, mouseY);
		}

	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
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

	public void selectWorld(@Nullable GuiListSaveCreatorEntry newGuiListWorldSelectionEntry) {
		this.selectButton.enabled = newGuiListWorldSelectionEntry != null;
	}

}
