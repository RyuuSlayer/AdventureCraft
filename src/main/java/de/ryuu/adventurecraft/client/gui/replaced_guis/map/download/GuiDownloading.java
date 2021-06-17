package de.ryuu.adventurecraft.client.gui.replaced_guis.map.download;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GuiDownloading extends GuiScreen {
    private final GuiDLMapInfo parentGui;
    private DownloadZip zip;
    private boolean unzipOnly = false;
    private String fileName;
    private boolean unzipStarted = false;
    private boolean firstInit = false;

    public GuiDownloading(GuiDLMapInfo parent, DownloadZip zip) {
        this.parentGui = parent;
        this.zip = zip;
    }

    public GuiDownloading(GuiDLMapInfo guiDLMapInfo, URL url) {
        this.unzipOnly = true;
        this.parentGui = guiDLMapInfo;
        this.fileName = DownloadZip.getFileName(url);
    }

    @Override
    public void initGui() {
        // TODO Auto-generated method stub
        buttonList.clear();
        this.buttonList.add(new GuiButtonExt(0, width / 2 - 100, height / 2 + 30, "BACK"));
        buttonList.get(0).visible = false;
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    protected void actionPerformed(GuiButton button) {
        // TODO Auto-generated method stub
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiDLMapInfo((GuiMapList) parentGui.parent, parentGui.map));
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        buttonList.get(0).x = width / 2 - 100;
        buttonList.get(0).y = height / 2 + 30;
        String str1 = "";
        String str2 = "Status: ";
        String str3 = "Do NOT close the game while this is shown!";
        if (firstInit) {
            if (!unzipOnly) {
                if (zip.getStatus() == DownloadZip.DOWNLOADING)
                    str1 = "Downloading map...";
                else if (zip.getStatus() == DownloadZip.ERROR) {
                    str1 = "Error";
                    buttonList.get(0).visible = true;
                } else if (zip.getStatus() == DownloadZip.COMPLETE) {
                    str1 = "Unzipping...";
                    str2 = "Status: Unzipping...";
                } else if (zip.getStatus() == DownloadZip.PAUSED)
                    str1 = "Paused ?! Downloading map...";
                else if (zip.getStatus() == DownloadZip.CANCELLED)
                    str1 = "Cancelled";
                int progress = Math.round(zip.getProgress());
                str2 = "Status: " + progress + "%";
                // System.out.println(zip.getSize());

            } else {
                str1 = "Unzipping...";
                str2 = "Status: Unzipping...";
            }
            drawCenteredString(fontRenderer, str1, width / 2, height / 2 - 30, 16777215);
            drawCenteredString(fontRenderer, str2, width / 2, height / 2 - 10, 16777215);
            drawCenteredString(fontRenderer, str3, width / 2, height / 2, 16777215);
            if ((unzipOnly || zip.getStatus() == DownloadZip.COMPLETE)) {
                if (this.unzipStarted)
                    return;
                new Thread(() -> {
                    unzipStarted = true;
                    byte[] buffer = new byte[1024];
                    try {
                        // create output directory is not exists
                        File folder = new File("./saves/.AC_MAPS/");
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        // get the zip file content
                        ZipInputStream zis = new ZipInputStream(new FileInputStream(
                                unzipOnly ? new File(new File(mc.mcDataDir, "saves/.AC_DOWNLOADS"), fileName)
                                        : zip.getFile()));
                        // get the zipped file list entry

                        ZipEntry ze = zis.getNextEntry();
                        String outDir = ze.getName().split("/")[0];
                        while (ze != null) {

                            String fileName = ze.getName();
                            File newFile = new File("./saves/.AC_MAPS/" + fileName);
                            // System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                            // create all non exists folders
                            // else you will hit FileNotFoundException for compressed folder
                            new File(newFile.getParent()).mkdirs();
                            if (ze.isDirectory())
                                newFile.mkdirs();
                            else {
                                newFile.createNewFile();
                                FileOutputStream fos = new FileOutputStream(newFile);

                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }

                                fos.close();
                            }
                            ze = zis.getNextEntry();
                        }
                        zis.closeEntry();
                        zis.close();
                        System.out.println(outDir);
                        if (!new File("./saves/.AC_MAPS/" + outDir + "/assets/adventurecraft").exists())
                            new File("./saves/.AC_MAPS/" + outDir + "/assets/adventurecraft").mkdirs();
                        File mapInfoFile = new File("./saves/.AC_MAPS/" + outDir + "/assets/adventurecraft/info.dat");
                        if (!mapInfoFile.exists())
                            mapInfoFile.createNewFile();
                        else {
                            mapInfoFile.delete();
                            mapInfoFile.createNewFile();
                        }
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setString("description", parentGui.map.description);
                        tag.setString("author", parentGui.map.author);
                        tag.setString("version", parentGui.map.mapVersion);
                        tag.setString("allowedUUIDs", parentGui.map.mapUUIDs);
                        tag.setBoolean("usesScripts", parentGui.map.hasScripts);
                        tag.setBoolean("usesMods", parentGui.map.additionalModsRequired);
                        tag.setString("mapURL", parentGui.map.mapURL);
                        CompressedStreamTools.write(tag, mapInfoFile);

                        mc.displayGuiScreen(new GuiDLMapInfo((GuiMapList) parentGui.parent, parentGui.map));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        mc.displayGuiScreen(new GuiDLMapInfo((GuiMapList) parentGui.parent, parentGui.map));
                        mc.getToastGui().add((toastGui, delta) -> {
                            toastGui.getMinecraft().getTextureManager().bindTexture(IToast.TEXTURE_TOASTS);
                            GlStateManager.color(1.0F, 1.0F, 1.0F);
                            toastGui.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
                            toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.RED + "Error!", 5, 7,
                                    0xff000000);
                            toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.DARK_RED + ex.toString(), 5,
                                    18, 0xff500050);
                            return delta <= 4000 ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
                        });
                    }
                }).start();
            }
        }
        firstInit = true;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        int keyCode = Keyboard.getEventKey();

        if (keyCode == 1)
            return;
        super.handleKeyboardInput();
    }
}
