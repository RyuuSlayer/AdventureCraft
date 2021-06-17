package de.ryuu.adventurecraft.client.gui.misc;

import de.ryuu.adventurecraft.client.gui.qad.QADButton;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADLabel;
import de.ryuu.adventurecraft.client.gui.qad.QADScrollPanel;
import de.ryuu.adventurecraft.client.gui.replaced_guis.CustomMainMenu;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSummary;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"CollectionAddAllCanBeReplacedWithConstructor", "FieldCanBeLocal", "ConstantConditions"})
public class GuiWorldInfo extends QADGuiScreen {

    private final WorldSummary ws;
    private final GuiScreen worldSel;
    private final String pathName;
    private final String worldDescription;
    private final String mapAuthorStr;
    private boolean requiresMoreMods = false;
    private boolean hasScripts = false;
    private String mapUrlStr = "";
    private QADButton btnBack;
    private QADLabel mapName;
    private QADLabel mapAuthor;
    private QADLabel mapInfo;
    private QADButton mapURL;
    private QADScrollPanel descPanel;

    public GuiWorldInfo(WorldSummary worldSummary, GuiScreen worldSelScreen, String pathName) {
        // TODO Auto-generated constructor stub
        this.ws = worldSummary;
        this.worldSel = worldSelScreen;
        this.pathName = pathName;

        final File worldDat = new File("./" + pathName + "/" + this.ws.getFileName() + "/assets/adventurecraft/info.dat");
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
        try {
            final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
            if (worldComp.hasKey("author") && !worldComp.getString("author").trim().isEmpty()) {
                s2 = worldComp.getString("author");
            } else
                s2 = "unknown";
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
            if (worldComp.hasKey("mapURL") && !worldComp.getString("mapURL").trim().isEmpty()) {
                this.mapUrlStr = worldComp.getString("mapURL");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
            if (worldComp.hasKey("usesMods")) {
                this.requiresMoreMods = worldComp.getBoolean("usesMods");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            final NBTTagCompound worldComp = CompressedStreamTools.read(worldDat);
            if (worldComp.hasKey("usesScripts")) {
                this.hasScripts = worldComp.getBoolean("usesScripts");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        this.mapAuthorStr = s2;
        this.worldDescription = s1;
    }

    @Override
    public void buildGui() {

        // TODO Auto-generated method stub
        addComponent(btnBack = new QADButton("BACK"));
        addComponent(mapName = new QADLabel(ws.getDisplayName()));
        addComponent(mapURL = new QADButton("Visit Website"));
        addComponent(descPanel = new QADScrollPanel());
        addComponent(mapAuthor = new QADLabel("By: " + this.mapAuthorStr));
        addComponent(mapInfo = new QADLabel((this.hasScripts ? "Has Scripts" : "Has no scripts") + " | "
                + (this.requiresMoreMods ? "Requires additional mods" : "No mods needed")));
        descPanel.setSize(width / 2, height / 2);
        descPanel.setPosition((width / 2) - (descPanel.getWidth() / 2), (height / 2) - (descPanel.getHeight() / 2));

        mapURL.setAction(() -> {
            // TODO Auto-generated method stub
            GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(GuiWorldInfo.this, mapUrlStr, 13, false);
            guiconfirmopenlink.disableSecurityWarning();
            mc.displayGuiScreen(guiconfirmopenlink);
        });
        btnBack.setAction(() -> {
            // TODO Auto-generated method stub
            mc.displayGuiScreen(worldSel);
        });

        mapURL.setWidth(80);
        mapName.setCentered();
        mapAuthor.setCentered();
        mapInfo.setCentered();
        descPanel.allowLeftMouseButtonScrolling = true;
        updateDescription();

    }

    @Override
    public void onScreenResized() {
        updateDescription();
    }

    private void updateDescription() {
        // TODO Auto-generated method stub
        String desc = this.worldDescription;
        desc = desc.replace("\\\\n", "\n");
        desc = desc.replace("\\n", "\n");
        List<String> descLines1 = new ArrayList<>();
        descLines1.addAll(this.fontRenderer.listFormattedStringToWidth(desc, descPanel.getContainerWidth() - 15));
        descPanel.removeAllComponents();
        int y = 10;
        descPanel.setViewportHeight(0);
        for (String line : descLines1) {
            descPanel.setViewportHeight(y + 10);
            descPanel.addComponent(new QADLabel(line, 10, y));
            y = y + 10;
        }
    }

    @Override
    public void updateGui() {
        // TODO Auto-generated method stub
        btnBack.setWidth(30);
        btnBack.setPosition(width / 2 - 30, height - 40);
        mapName.setPosition(width / 2, 20);
        descPanel.setSize(width / 2, height / 2);
        descPanel.setPosition((width / 2) - (descPanel.getWidth() / 2), (height / 2) - (descPanel.getHeight() / 2));
        mapAuthor.setPosition(width / 2, mapName.getY() + 20);
        mapInfo.setPosition(width / 2, descPanel.getY() + descPanel.getHeight() + 10);
        mapURL.setPosition(btnBack.getX() + 40, btnBack.getY());
        mapURL.setEnabled(!this.mapUrlStr.trim().isEmpty());
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        // TODO Auto-generated method stub
        if (id == 13) {
            if (result) {
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.mapUrlStr));
                } catch (Throwable throwable) {
                    System.err.println("Couldn't open link: " + throwable.getMessage());
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        // TODO Auto-generated method stub
        char typedChar = Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();

        if (keyCode == 1) {
            mc.displayGuiScreen(new CustomMainMenu());
            return;
        }
        super.handleKeyboardInput();
    }

}
