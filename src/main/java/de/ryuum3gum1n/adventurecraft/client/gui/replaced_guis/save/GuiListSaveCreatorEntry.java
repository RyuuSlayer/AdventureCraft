package de.ryuum3gum1n.adventurecraft.client.gui.replaced_guis.save;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.ryuum3gum1n.adventurecraft.Reference;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SuppressWarnings("ConstantConditions")
public class GuiListSaveCreatorEntry implements GuiListExtended.IGuiListEntry {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
	private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
	private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation(
			"textures/gui/world_selection.png");
	private final Minecraft client;
	private final SaveCreator worldSelScreen;
	private final WorldSummary worldSummary;
	private final ResourceLocation iconLocation;
	private final GuiListSaveCreator containingListSel;
	private File iconFile;
	private DynamicTexture icon;
	private long lastClickTime;
	private final String worldPathName;

	public GuiListSaveCreatorEntry(GuiListSaveCreator newGuiListWorldSelection, WorldSummary worldSummaryIn,
			ISaveFormat saveFormat) {
		this.containingListSel = newGuiListWorldSelection;
		this.worldPathName = containingListSel.worldPathName;
		this.worldSelScreen = newGuiListWorldSelection.getGuiWorldSelection();
		this.worldSummary = worldSummaryIn;
		this.client = Minecraft.getMinecraft();
		this.iconLocation = new ResourceLocation("worlds/" + worldSummaryIn.getFileName() + "/icon");
		this.iconFile = saveFormat.getFile(worldSummaryIn.getFileName(), "icon.png");
		if (!this.iconFile.isFile()) {
			this.iconFile = null;
		}

		this.loadServerIcon();
	}

	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) {
		String s = this.worldSummary.getDisplayName();
		final File worldDat = new File("./" + this.containingListSel.worldPathName + "/"
				+ this.worldSummary.getFileName() + "/adventurecraft/info.dat");
		String s1;
		try {
			final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);

			s1 = worldComp.hasKey("description") ? TextFormatting.WHITE + worldComp.getString("description")
					: "No description provided!";
			if (worldComp.getString("description").isEmpty())
				s1 = "No description provided!";
		} catch (Exception e) {
			// TODO: handle exception
			s1 = "Error";
		}
		String s2 = "";
		if (worldDat.exists()) {
			if (StringUtils.isEmpty(s)) {
				s = I18n.format("selectWorld.world") + " " + (slotIndex + 1);
			}

			if (this.worldSummary.requiresConversion()) {
				s2 = I18n.format("selectWorld.conversion") + " " + s2;
			} else {
				s2 = "";

				String s3 = "<VERSION>";
				try {
					final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
					if (worldComp.hasKey("author") && !worldComp.getString("author").trim().isEmpty()) {
						s2 = s2 + "Author: " + worldComp.getString("author") + ", ";
					} else
						s2 = s2 + "Author: unknown, ";
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
					if (worldComp.hasKey("version")) {
						if (worldComp.getString("version").equals("vanilla"))
							s2 = s2 + TextFormatting.GOLD + "Vanilla";
						else
							s2 = s2 + "Adventurecraft Version: "
									+ (Reference.MOD_VERSION.equals(worldComp.getString("version")) ? ""
											: TextFormatting.RED)
									+ worldComp.getString("version");
					} else {
						s2 = s2 + "Adventurecraft Version: " + TextFormatting.RED + "Unknown";
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					s2 = s2 + "Adventurecraft Version: " + TextFormatting.RED + "ERROR";
				}

			}

		} else {
			s1 = TextFormatting.RED + "No world information file!";
			s2 = TextFormatting.RED + "To fix open this world once";
		}

		if (s1.contains("\\n")) {
			s1 = s1.split("\\\\n")[0] + TextFormatting.WHITE + " <...>";
		} else if (s1.length() > 42) {
			s1 = s1.substring(0, 42) + TextFormatting.WHITE + " <...>";
		}

		this.client.fontRenderer.drawString(s, x + 32 + 3, y + 1, 16777215);
		this.client.fontRenderer.drawString(s1, x + 32 + 3, y + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
		this.client.fontRenderer.drawString(s2, x + 32 + 3,
				y + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(this.icon != null ? this.iconLocation : ICON_MISSING);
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
		GlStateManager.disableBlend();
		// x + 32 + 3, y + this.client.fontRenderer.FONT_HEIGHT + 3

		if (this.client.gameSettings.touchscreen || isSelected) {
			this.client.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
			Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int j = mouseX - x;
			int i = j < 32 ? 32 : 0;

			if (this.worldSummary.markVersionInList()) {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, (float) i, 32, 32, 256.0F, 256.0F);

				if (this.worldSummary.askToOpenWorld()) {
					Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, (float) i, 32, 32, 256.0F, 256.0F);

					if (j < 32) {
						this.worldSelScreen.setVersionTooltip(
								TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion1") + "\n"
										+ TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion2"));
					}
				} else {
					Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, (float) i, 32, 32, 256.0F, 256.0F);

					if (j < 32) {
						this.worldSelScreen
								.setVersionTooltip(TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot1")
										+ "\n" + TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot2"));
					}
				}
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float) i, 32, 32, 256.0F, 256.0F);
			}

		}
	}

	/**
	 * Called when the mouse is clicked within this entry. Returning true means that
	 * something within this entry was clicked and the list should not be dragged.
	 */
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		this.containingListSel.selectWorld(slotIndex);

		if (relativeX <= 32 && relativeX < 32) {
			this.joinWorld();
			return true;
		} else if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
			this.joinWorld();
			return true;
		} else {
			this.lastClickTime = Minecraft.getSystemTime();
			return false;
		}
	}

	public void joinWorld() {
		final File worldDat = new File("./" + this.containingListSel.worldPathName + "/"
				+ this.worldSummary.getFileName() + "/adventurecraft/info.dat");
		NBTTagCompound worldComp = null;
		try {
			worldComp = CompressedStreamTools.read(worldDat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.worldSummary.askToOpenWorld()) {
			this.client.displayGuiScreen(new GuiYesNo((result, id) -> {
				if (result) {
					GuiListSaveCreatorEntry.this.loadWorld();
				} else {
					GuiListSaveCreatorEntry.this.client.displayGuiScreen(GuiListSaveCreatorEntry.this.worldSelScreen);
				}
			}, I18n.format("selectWorld.versionQuestion"),
					I18n.format("selectWorld.versionWarning", this.worldSummary.getVersionName()),
					I18n.format("selectWorld.versionJoinButton"), I18n.format("gui.cancel"), 0));
		} else if (worldComp != null && worldComp.hasKey("version")
				&& !worldComp.getString("version").equals(Reference.MOD_VERSION)
				&& !worldComp.getString("version").equals("vanilla")) {
			this.client.displayGuiScreen(new GuiYesNo((result, id) -> {
				if (result) {
					GuiListSaveCreatorEntry.this.loadWorld();
				} else {
					GuiListSaveCreatorEntry.this.client.displayGuiScreen(GuiListSaveCreatorEntry.this.worldSelScreen);
				}
			}, I18n.format("selectWorld.versionQuestion"),
					"This world was loaded in Adventurecraft version " + worldComp.getString("version")
							+ " and loading it in this version could cause corruption!",
					I18n.format("selectWorld.versionJoinButton"), I18n.format("gui.cancel"), 0));

		} else {
			this.loadWorld();
		}

	}

	private void loadWorld() {
		this.client.getSoundHandler()
				.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

		if (new AnvilSaveConverter(new File(this.client.mcDataDir, this.worldPathName), this.client.getDataFixer())
				.canLoadWorld(this.worldSummary.getFileName())) {
			int attempt = 0;
			try {
				File outDir = new File(new File(this.client.mcDataDir, "saves/.AC_SAVES"),
						this.worldSummary.getFileName() + "@SAV0");

				while (outDir.exists()) {
					attempt++;
					outDir = new File(new File(this.client.mcDataDir, "saves/.AC_SAVES"),
							this.worldSummary.getFileName() + "@SAV" + attempt);
				}

				FileUtils.copyDirectory(
						new File(new File(this.client.mcDataDir, "saves/.AC_MAPS"), this.worldSummary.getFileName()),
						outDir);
				FileInputStream inStream = new FileInputStream(new File(outDir, "level.dat"));
				NBTTagCompound worldTag = CompressedStreamTools.readCompressed(inStream);

				NBTTagCompound worldDataTag = worldTag.getCompoundTag("Data");
				worldDataTag.setInteger("GameType", 2);
				worldDataTag.setBoolean("allowCommands", false);
				worldDataTag.setBoolean("DifficultyLocked", true);
				NBTTagCompound playerDataTag = worldDataTag.getCompoundTag("Player");
				playerDataTag.setInteger("playerGameType", 2);
				NBTTagList posList = new NBTTagList();
				posList.appendTag(new NBTTagDouble(worldDataTag.getInteger("SpawnX")));
				posList.appendTag(new NBTTagDouble(worldDataTag.getInteger("SpawnY")));
				posList.appendTag(new NBTTagDouble(worldDataTag.getInteger("SpawnZ")));
				playerDataTag.setTag("Pos", posList);
				worldDataTag.setTag("Player", playerDataTag);
				worldTag.setTag("Data", worldDataTag);
				inStream.close();
				FileOutputStream outStream = new FileOutputStream(new File(outDir, "level.dat"));
				CompressedStreamTools.writeCompressed(worldTag, outStream);
				outStream.close();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
//			try {
//				final File outDir = new File(new File(this.client.mcDataDir, "saves"),this.worldSummary.getFileName()+"@SAV"+attempt);
//				FileUtils.copyDirectory(new File(new File(this.client.mcDataDir, "saves/.AC_SAVES" ),this.worldSummary.getFileName()+"@SAV"+attempt), outDir);
//				try {
//					System.out.println(this.worldSummary.getClass().getSuperclass().getFields());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				net.minecraftforge.fml.client.FMLClientHandler.instance().tryLoadExistingWorld(new GuiWorldSelection(worldSelScreen), );
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			client.displayGuiScreen(this.worldSelScreen.prevScreen);
		}
	}

	private void loadServerIcon() {
		// System.out.println("Loading icon");
		boolean flag = this.iconFile != null && this.iconFile.isFile();

		if (flag) {
			BufferedImage bufferedimage;

			try {
				bufferedimage = ImageIO.read(this.iconFile);
				Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
				Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
			} catch (Throwable throwable) {
				LOGGER.error("Invalid icon for world {}", this.worldSummary.getFileName(), throwable);
				this.iconFile = null;
				return;
			}

			if (this.icon == null) {
				this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
				this.client.getTextureManager().loadTexture(this.iconLocation, this.icon);
			}

			bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.getTextureData(),
					0, bufferedimage.getWidth());
			this.icon.updateDynamicTexture();
		} else if (!flag) {
			this.client.getTextureManager().deleteTexture(this.iconLocation);
			this.icon = null;
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

}
