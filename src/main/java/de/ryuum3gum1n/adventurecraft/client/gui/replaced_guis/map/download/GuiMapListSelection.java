package de.ryuum3gum1n.adventurecraft.client.gui.replaced_guis.map.download;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.ryuum3gum1n.adventurecraft.Reference;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GuiMapListSelection extends GuiListExtended {
	private static final Logger LOGGER = LogManager.getLogger();
	protected final List<GuiMapListEntry> entries = Lists.newArrayList();
	private final GuiMapList worldSelection;
	/**
	 * Index to the currently selected world
	 */
	private int selectedIdx = -1;
	private final GuiTextField search;
	private boolean loadReady = true;
	private boolean errorToastShown = false;

	public GuiMapListSelection(GuiMapList p_i46590_1_, Minecraft clientIn, int widthIn, int heightIn, int topIn,
			int bottomIn, int slotHeightIn, GuiTextField search) {
		super(clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.worldSelection = p_i46590_1_;
		this.search = search;
		this.refreshList();
	}

	public void refreshList() {
		this.setAmountScrolled();
		this.entries.clear();
		addMapsToList(0, 15);
	}

	public void addMapsToList(int start, int length) {
		final String search = this.search.getText().trim();
		List<DownloadableMap> list = new ArrayList<>();
		try {
			System.setProperty("http.agent", "");
			HttpURLConnection connection = (HttpURLConnection) new URL("https://longor.net/talecraft/maps.php?start="
					+ start + "&length=" + length + "&search=" + URLEncoder.encode(search, "UTF-8")).openConnection();
			connection.setRequestProperty("User-Agent", "AdventureCraft " + Reference.MOD_VERSION);
			try {
				connection.connect();
			} catch (NoRouteToHostException e) {
				if (!errorToastShown)
					mc.getToastGui().add((toastGui, delta) -> {
						// TODO Auto-generated method stub
						GuiMapListSelection.this.errorToastShown = (delta <= 2000);
						toastGui.getMinecraft().getTextureManager().bindTexture(IToast.TEXTURE_TOASTS);
						GlStateManager.color(1.0F, 1.0F, 1.0F);
						toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);

						toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.RED + "Error loading Maps", 5, 7,
								0xff000000);
						toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.DARK_RED + "No connection", 5,
								18, 0xff500050);
						return delta <= 4000 ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
					});
				return;
			}

			if (connection.getResponseCode() != 200
					&& !(connection.getResponseCode() == 404 && !this.search.getText().isEmpty())) {
				if (!errorToastShown)
					mc.getToastGui().add((toastGui, delta) -> {
						// TODO Auto-generated method stub
						GuiMapListSelection.this.errorToastShown = (delta <= 3000);
						toastGui.getMinecraft().getTextureManager().bindTexture(IToast.TEXTURE_TOASTS);
						GlStateManager.color(1.0F, 1.0F, 1.0F);
						toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
						toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.RED + "Error loading Maps", 5, 7,
								0xff000000);
						try {
							toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.DARK_RED + ""
									+ connection.getResponseCode() + " " + connection.getResponseMessage(), 5, 18,
									0xff500050);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return delta <= 3000 ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
					});
				return;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JsonObject j = new JsonParser().parse(in.lines().collect(Collectors.joining())).getAsJsonObject();
			j.entrySet().forEach(t -> {
				// TODO Auto-generated method stub
				if (t.getKey().isEmpty())
					return;
				list.add(new DownloadableMap(t));
			});
			for (DownloadableMap map : list) {
				this.entries.add(new GuiMapListEntry(this, map));

			}
			this.loadReady = true;

		} catch (Exception e) {
			if (!(e instanceof FileNotFoundException))
				e.printStackTrace();
		}
	}

	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
		// Lazy loading
		if (loadReady && getMaxScroll() <= getAmountScrolled() - 3) {
			loadReady = false;
			addMapsToList(this.entries.size(), 10);
		}

		super.drawScreen(mouseXIn, mouseYIn, partialTicks);
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiMapListEntry getListEntry(int index) {
		return this.entries.get(index);
	}

	protected int getSize() {
		return this.entries.size();
	}

	protected int getScrollBarX() {
		return super.getScrollBarX() + 20;
	}

	protected void setAmountScrolled() {
		this.amountScrolled = 0;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return super.getListWidth() + 110;
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
	public GuiMapListEntry getSelectedWorld() {
		return this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
	}

	public GuiMapList getGuiWorldSelection() {
		return this.worldSelection;
	}

}
