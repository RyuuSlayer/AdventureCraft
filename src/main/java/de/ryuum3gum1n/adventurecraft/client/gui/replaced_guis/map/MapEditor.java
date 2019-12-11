package de.ryuum3gum1n.adventurecraft.client.gui.replaced_guis.map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADButton;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADColorTextField;
import de.ryuum3gum1n.adventurecraft.client.gui.qad.QADGuiScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Modified version of the Edit World gui<br>
 * It now has an color-text-field and a "Set Icon" button
 *
 * @author ErdbeerbaerLP
 */
public class MapEditor extends QADGuiScreen {
	private final GuiScreen lastScreen;
	private final String worldId;
	private QADColorTextField nameEdit;
	private QADButton btnCancel;
	private QADButton btnSave;
	private QADButton btnOpenFolder;
	private BufferedImage newImage = null;
	private QADButton btnResetIcon;
	private QADButton btnSetIcon;
	private final String path;

	public MapEditor(GuiScreen p_i46593_1_, String p_i46593_2_, String path) {
		this.lastScreen = p_i46593_1_;
		this.worldId = p_i46593_2_;
		this.path = path;
	}

	@Override
	public void buildGui() {
		this.drawCursorLines = false;
		this.nameEdit = new QADColorTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);

		addComponent(this.nameEdit);
		ISaveFormat isaveformat = new AnvilSaveConverter(new File(mc.mcDataDir, path), mc.getDataFixer());
		WorldInfo worldinfo = isaveformat.getWorldInfo(this.worldId);
		String s = worldinfo == null ? "" : worldinfo.getWorldName();
		this.nameEdit.setText(s);
		btnResetIcon = this.addComponent(new QADButton(this.width / 2 - 100, this.height / 4 + 24 + 12, 120,
				I18n.format("selectWorld.edit.resetIcon")));
		btnOpenFolder = this.addComponent(new QADButton(this.width / 2 - 100, this.height / 4 + 48 + 12, 120,
				I18n.format("selectWorld.edit.openFolder")));
		this.addComponent(btnSave = new QADButton(this.width / 2 - 100, this.height / 4 + 96 + 12, 200,
				I18n.format("selectWorld.edit.save")));
		this.addComponent(btnCancel = new QADButton(this.width / 2 - 100, this.height / 4 + 120 + 12, 200,
				I18n.format("gui.cancel")));
		btnResetIcon.setEnabled(new AnvilSaveConverter(new File(mc.mcDataDir, path), mc.getDataFixer())
				.getFile(this.worldId, "icon.png").isFile());
		btnSetIcon = addComponent(new QADButton(btnResetIcon.getX() + btnResetIcon.getButtonWidth(),
				btnResetIcon.getY(), 80, "Set Icon"));
		btnSetIcon.setAction(() -> {
			// TODO Auto-generated method stub
			try {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fc.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return "World icon files (.png)";
					}

					@Override
					public boolean accept(File f) {
						// TODO Auto-generated method stub
						if (f.isDirectory())
							return true;
						return f.getName().endsWith(".png");
					}
				});
				JFrame j = new JFrame();
				j.setUndecorated(true);
				j.setTitle("Select icon");
				j.setVisible(true);
				int res = fc.showOpenDialog(j);
				j.setVisible(false);
				if (res == JFileChooser.CANCEL_OPTION)
					return;

				BufferedImage img = ImageIO.read(fc.getSelectedFile());
				int w = img.getWidth();
				int h = img.getHeight();
				BufferedImage dimg = new BufferedImage(64, 64, img.getType());
				Graphics2D g = dimg.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
						RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				g.drawImage(img, 0, 0, 64, 64, 0, 0, w, h, null);
				g.dispose();
				newImage = dimg;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		btnCancel.setAction(() -> {
			// TODO Auto-generated method stub
			mc.displayGuiScreen(lastScreen);
		});
		btnSave.setAction(() -> {
			// TODO Auto-generated method stub
			ISaveFormat isaveformat12 = new AnvilSaveConverter(new File(mc.mcDataDir, path), mc.getDataFixer());
			isaveformat12.renameWorld(worldId, nameEdit.getText().trim());
			if (newImage != null) {
				File world = isaveformat12.getFile(worldId, "level.dat").getParentFile();
				File icon = new File(world, "icon.png");
				if (icon.exists())
					icon.delete();
				try {
					FileOutputStream fo = new FileOutputStream(icon);

					ImageIO.write(newImage, "png", fo);
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				btnResetIcon.setEnabled(true);
			}
			mc.displayGuiScreen(lastScreen);
		});
		btnResetIcon.setAction(() -> {
			// TODO Auto-generated method stub
			ISaveFormat isaveformat1 = new AnvilSaveConverter(new File(mc.mcDataDir, path), mc.getDataFixer());
			FileUtils.deleteQuietly(isaveformat1.getFile(worldId, "icon.png"));
			btnResetIcon.setEnabled(false);
		});
		btnOpenFolder.setAction(() -> {
			// TODO Auto-generated method stub
			ISaveFormat isaveformat2 = new AnvilSaveConverter(new File(mc.mcDataDir, path), mc.getDataFixer());
			OpenGlHelper.openFile(isaveformat2.getFile(worldId, "level.dat").getParentFile());

		});

	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override
	public void handleKeyboardInput() throws IOException {
		// TODO Auto-generated method stub
		char typedChar = Keyboard.getEventCharacter();
		int keyCode = Keyboard.getEventKey();
		btnSave.setEnabled(!this.nameEdit.getText().trim().isEmpty());

		if (keyCode == 28 || keyCode == 156) {
			this.actionPerformed(this.buttonList.get(2));
		}
		if (keyCode == 1)
			return;
		super.handleKeyboardInput();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, I18n.format("selectWorld.edit.title"), this.width / 2, 20, 16777215);
		this.drawString(this.fontRenderer, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
		this.nameEdit.setPosition(this.width / 2 - 100, 60);
		this.btnResetIcon.setPosition(this.width / 2 - 100, this.height / 4 + 24 + 12);
		this.btnOpenFolder.setPosition(this.width / 2 - 100, this.height / 4 + 48 + 12);
		this.btnSave.setPosition(this.width / 2 - 100, this.height / 4 + 96 + 12);
		this.btnCancel.setPosition(this.width / 2 - 100, this.height / 4 + 120 + 12);
		this.btnSetIcon.setPosition(btnResetIcon.getX() + btnResetIcon.getButtonWidth(), btnResetIcon.getY());

	}
}
