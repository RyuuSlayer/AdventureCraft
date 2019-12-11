package de.ryuum3gum1n.adventurecraft.client.gui.replaced_guis;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.io.FileUtils;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.client.gui.misc.GuiCopyingWorld;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewGameOverScreen extends GuiGameOver {

	public NewGameOverScreen(ITextComponent causeOfDeathIn) {
		super(causeOfDeathIn);
		// TODO Auto-generated constructor stub

	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void confirmClicked(boolean result, int id) {
		if (result) {
			File savesDir = new File(this.mc.mcDataDir, "saves");
			if (this.mc.world != null) {
				this.mc.world.sendQuittingDisconnectingPacket();
			}

			this.mc.loadWorld(null);

			Thread t = new Thread(() -> {
				try {
					if (AdventureCraft.lastVisitedWorld == null
							|| (AdventureCraft.lastVisitedWorld != null && !AdventureCraft.lastVisitedWorld
									.getSaveHandler().getWorldDirectory().getName().equals("AC_TEST")))
						mc.displayGuiScreen(new GuiCopyingWorld("Saving...."));
					else
						mc.displayGuiScreen(new GuiCopyingWorld("Deleting Test..."));
					Thread.sleep(TimeUnit.SECONDS.toMillis(1));
					final String[] savesList = savesDir.list();
					// Save worlds back to their folders!
					for (String s : savesList) {
						if (s.startsWith(".AC"))
							continue;
						if (s.equals("AC_TEST")) {
							FileUtils.deleteDirectory(new File(savesDir, s));
							continue;
						}
						boolean type = !s.contains("@SAV");
						FileUtils.copyDirectoryToDirectory(new File(savesDir, s),
								new File(savesDir, type ? ".AC_MAPS" : ".AC_SAVES"));
						FileUtils.deleteDirectory(new File(savesDir, s));
					}
					NewGameOverScreen.this.mc.addScheduledTask(() -> mc.displayGuiScreen(new CustomMainMenu()));
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			});
			t.start();
		} else {
			this.mc.player.respawnPlayer();
			this.mc.displayGuiScreen(null);
		}
	}
}
