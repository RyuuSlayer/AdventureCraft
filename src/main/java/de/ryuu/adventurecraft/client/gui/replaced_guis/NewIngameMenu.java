package de.ryuu.adventurecraft.client.gui.replaced_guis;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.gui.misc.GuiCopyingWorld;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.GuiOldSaveLoadConfirm;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.StartupQuery;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewIngameMenu extends GuiIngameMenu {
    GuiButtonExt testBtn;
    GuiButtonExt testHereButton;
    private World currentWorld;

    public NewIngameMenu() {
        currentWorld = AdventureCraft.lastVisitedWorld;
    }

    @Override
    public void initGui() {
        // TODO Auto-generated method stub
        super.initGui();
        addButton(testBtn = new GuiButtonExt(15, width / 2, height / 2, "Test"));
        addButton(testHereButton = new GuiButtonExt(16, 0, 0, "Test from here"));
        if (AdventureCraft.asClient().isBuildMode() || (currentWorld != null
                && currentWorld.getSaveHandler().getWorldDirectory().getName().equals("AC_TEST")))
            this.buttonList.get(4).visible = false;
        testBtn.visible = false;
        testHereButton.visible = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (currentWorld == null)
            currentWorld = AdventureCraft.lastVisitedWorld;
        this.buttonList.get(4).enabled = false;
        this.buttonList.get(4).displayString = I18n.format("menu.shareToLan") + " (Disabled)";
        if (AdventureCraft.asClient().isBuildMode()) {
            testHereButton.x = this.buttonList.get(4).x;
            testHereButton.y = this.buttonList.get(4).y;
            testBtn.x = this.buttonList.get(4).x + 140;
            testBtn.y = this.buttonList.get(4).y;
            testHereButton.width = 130;
            testBtn.width = 60;
            testBtn.visible = true;
            testHereButton.visible = true;
        }
        if (currentWorld != null && currentWorld.getSaveHandler().getWorldDirectory().getName().equals("AC_TEST")) {
            testBtn.x = this.buttonList.get(4).x;
            testBtn.y = this.buttonList.get(4).y;
            testBtn.width = this.buttonList.get(4).width;
            testBtn.visible = true;
            testBtn.displayString = "Edit";
            testHereButton.visible = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // TODO Auto-generated method stub

        switch (button.id) {
            case 1:
                button.enabled = false;
                File savesDir = new File(this.mc.mcDataDir, "saves");
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                Thread t = new Thread(() -> {
                    try {
                        if (currentWorld == null || (currentWorld != null
                                && !currentWorld.getSaveHandler().getWorldDirectory().getName().equals("AC_TEST")))
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
                        NewIngameMenu.this.mc.addScheduledTask(() -> mc.displayGuiScreen(new CustomMainMenu()));
                    } catch (InterruptedException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
                t.start();

                return;
            case 15:
                test(button, false);
                break;
            case 16:
                test(button, true);
                break;
        }
        super.actionPerformed(button);
    }

    private void test(GuiButton button, boolean currentPos) {
        button.enabled = false;

        File savesDir2 = new File(this.mc.mcDataDir, "saves");
        this.mc.world.sendQuittingDisconnectingPacket();
        final WorldClient world = mc.world;
        this.mc.loadWorld(null);

        Thread t2 = new Thread(() -> {
            try {
                if (currentWorld != null
                        && !currentWorld.getSaveHandler().getWorldDirectory().getName().equals("AC_TEST")) {
                    mc.displayGuiScreen(new GuiCopyingWorld("Saving Map..."));
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    // Save world back to their folders!
                    FileUtils.copyDirectory(
                            new File(savesDir2, currentWorld.getSaveHandler().getWorldDirectory().getName()),
                            new File(new File(mc.mcDataDir, "saves/.AC_MAPS"),
                                    currentWorld.getSaveHandler().getWorldDirectory().getName()));
                    mc.displayGuiScreen(new GuiCopyingWorld("Creating Test-Save..."));
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    final String[] savesList = savesDir2.list();
                    // noinspection ConstantConditions
                    for (String s : savesList) {
                        if (s.startsWith(".AC"))
                            continue;
                        if (s.equals("AC_TEST")) {
                            FileUtils.deleteDirectory(new File(savesDir2, s));
                            continue;
                        }
                        boolean type = !s.contains("@SAV");
                        FileUtils.copyDirectoryToDirectory(new File(savesDir2, s),
                                new File(savesDir2, type ? ".AC_MAPS" : ".AC_SAVES"));
                        FileUtils.deleteDirectory(new File(savesDir2, s));
                    }
                    FileUtils.copyDirectory(
                            new File(new File(mc.mcDataDir, "saves/.AC_MAPS"),
                                    currentWorld.getSaveHandler().getWorldDirectory().getName()),
                            new File(savesDir2, "AC_TEST"));

                    final String s = "AC_TEST";
                    FileInputStream inStream = new FileInputStream(new File(new File(savesDir2, s), "level.dat"));
                    NBTTagCompound worldTag = CompressedStreamTools.readCompressed(inStream);

                    NBTTagCompound worldDataTag = worldTag.getCompoundTag("Data");
                    worldDataTag.setInteger("GameType", 2);
                    worldDataTag.setBoolean("allowCommands", false);
                    worldDataTag.setBoolean("DifficultyLocked", true);
                    NBTTagCompound playerDataTag = worldDataTag.getCompoundTag("Player");
                    playerDataTag.setInteger("playerGameType", 2);
                    if (!currentPos) {
                        NBTTagList l = new NBTTagList();
                        System.out.println(playerDataTag.getTag("Pos"));
                        l.appendTag(new NBTTagDouble(worldDataTag.getLong("SpawnX")));
                        l.appendTag(new NBTTagDouble(worldDataTag.getLong("SpawnY")));
                        l.appendTag(new NBTTagDouble(worldDataTag.getLong("SpawnZ")));
                        playerDataTag.setTag("Pos", l);
                        System.out.println(playerDataTag.getTag("Pos"));
                    }
                    worldDataTag.setTag("Player", playerDataTag);
                    worldTag.setTag("Data", worldDataTag);
                    inStream.close();
                    FileOutputStream outStream = new FileOutputStream(new File(new File(savesDir2, s), "level.dat"));
                    CompressedStreamTools.writeCompressed(worldTag, outStream);
                    outStream.close();
                    FileOutputStream outStream2 = new FileOutputStream(new File(new File(savesDir2, s), "test.dat"));
                    NBTTagCompound cin = new NBTTagCompound();
                    cin.setString("OrigLevelName", worldDataTag.getString("LevelName"));
                    cin.setString("OrigLevelFolder", currentWorld.getSaveHandler().getWorldDirectory().getName());
                    CompressedStreamTools.writeCompressed(cin, outStream2);
                    outStream2.close();

                    mc.addScheduledTask(() -> tryLoadExistingWorld(new File(savesDir2, s), s, "AC_TEST"));
                } else {
                    mc.displayGuiScreen(new GuiCopyingWorld("Deleting Test Save..."));
                    System.out.println(new File(new File(savesDir2, "AC_TEST"), "test.dat").exists());
                    FileInputStream testinStream = new FileInputStream(
                            new File(new File(savesDir2, "AC_TEST"), "test.dat"));
                    NBTTagCompound c = CompressedStreamTools.readCompressed(testinStream);
                    System.out.println(c);
                    String worldFolder = c.getString("OrigLevelFolder");
                    String worldName = c.getString("OrigLevelName");
                    System.out.println(worldFolder);
                    System.out.println(worldName);
                    testinStream.close();
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    final String[] savesList = savesDir2.list();
                    for (String s : savesList) {
                        if (s.startsWith(".AC"))
                            continue;
                        if (s.equals("AC_TEST")) {
                            FileUtils.deleteDirectory(new File(savesDir2, s));
                            continue;
                        }
                        boolean type = !s.contains("@SAV");
                        FileUtils.copyDirectoryToDirectory(new File(savesDir2, s),
                                new File(savesDir2, type ? ".AC_MAPS" : ".AC_SAVES"));
                        FileUtils.deleteDirectory(new File(savesDir2, s));
                    }
                    mc.displayGuiScreen(new GuiCopyingWorld("Re-Loading Map..."));
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    FileUtils.copyDirectory(new File(new File(mc.mcDataDir, "saves/.AC_MAPS"), worldFolder),
                            new File(savesDir2, worldFolder));

                    mc.addScheduledTask(() -> {
                        System.out.println(worldFolder);
                        System.out.println(worldName);
                        System.out.println(new File(savesDir2, worldFolder).getAbsolutePath());
                        tryLoadExistingWorld(new File(savesDir2, worldFolder), worldFolder, worldName);
                    });
                }

            } catch (InterruptedException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        t2.start();

    }

    public void tryLoadExistingWorld(File dir, String fileName, String saveName) {
        NBTTagCompound leveldat;
        try {
            leveldat = CompressedStreamTools.readCompressed(new FileInputStream(new File(dir, "level.dat")));
        } catch (Exception e) {
            try {
                leveldat = CompressedStreamTools.readCompressed(new FileInputStream(new File(dir, "level.dat_old")));
            } catch (Exception e1) {
                FMLLog.log.warn("There appears to be a problem loading the save {}, both level files are unreadable.",
                        fileName);
                return;
            }
        }
        NBTTagCompound fmlData = leveldat.getCompoundTag("FML");
        if (fmlData.hasKey("ModItemData")) {
            mc.displayGuiScreen(new GuiOldSaveLoadConfirm(fileName, saveName, null));
        } else {
            try {
                mc.launchIntegratedServer(fileName, saveName, null);
            } catch (StartupQuery.AbortedException e) {
                // ignore
            }
        }
    }
}