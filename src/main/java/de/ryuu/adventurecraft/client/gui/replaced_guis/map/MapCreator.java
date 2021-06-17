package de.ryuu.adventurecraft.client.gui.replaced_guis.map;

import de.ryuu.adventurecraft.client.gui.qad.*;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Random;

/**
 * An improved world creation gui which does not have gamemode options in it<br>
 * (used gamemode is creative)
 *
 * @author ErdbeerbaerLP
 */
public class MapCreator extends QADGuiScreen {

    /**
     * These filenames are known to be restricted on one or more OS's.
     */
    private static final String[] DISALLOWED_FILENAMES = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL",
            "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4",
            "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
    private final MapCreator newgui;
    private final String worldSeed;
    private final String worldName;
    public String chunkProviderSettingsJson = "";
    private QADColorTextField worldNameField;
    private QADTextField worldSeedField;
    private String saveDirName;
    private boolean generateStructuresEnabled = true;
    /**
     * If cheats are allowed
     */
    private boolean allowCheats = true;
    private boolean bonusChestEnabled;
    /**
     * Set to true when "hardcore" is the currently-selected gamemode
     */
    private boolean hardCoreMode;
    private boolean alreadyGenerated;
    private QADButton btnMapFeatures;
    private QADButton btnMapType;
    private QADButton btnAllowCommands;
    private QADButton btnCustomizeType;
    private QADButton btnCreate;
    private QADLabel lblName;
    private QADLabel lblSeed;
    private QADLabel lblSeedInfo;
    private int selectedIndex;
    private QADButton btnBack;

    public MapCreator() {
        newgui = this;
        this.worldSeed = "";
        this.worldName = "New Map";
    }

    /**
     * Ensures that a proposed directory name doesn't collide with existing names.
     * Returns the name, possibly modified to avoid collisions.
     */
    public static String getUncollidingSaveDirName(ISaveFormat saveLoader, String name) {
        name = name.replaceAll("[\\./@\"]", "_");

        for (String s : DISALLOWED_FILENAMES) {
            if (name.equalsIgnoreCase(s)) {
                // noinspection StringConcatenationInLoop
                name = "_" + name + "_";
            }
        }

        while (saveLoader.getWorldInfo(name) != null) {
            name = name + "-";
        }

        return name;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    @Override
    public void buildGui() {

        this.drawCursorLines = false;
        btnCreate = this.addComponent(
                new QADButton(this.width / 2 - 155, this.height - 38, 150, I18n.format("selectWorld.create")));
        btnBack = this
                .addComponent(new QADButton(this.width / 2 + 5, this.height - 38, 150, I18n.format("gui.cancel")));
        this.btnMapFeatures = this
                .addComponent(new QADButton(this.width / 2 - 155, 120, 130, I18n.format("selectWorld.mapFeatures")));

        this.btnMapType = this
                .addComponent(new QADButton(this.width / 2 + 5, 120, 100, I18n.format("selectWorld.mapType")));

        this.btnAllowCommands = this
                .addComponent(new QADButton(this.width / 2 - 155, 145, 130, I18n.format("selectWorld.allowCommands")));

        this.btnCustomizeType = this
                .addComponent(new QADButton(this.width / 2 + 5, 140, 100, I18n.format("selectWorld.customizeType")));

        this.worldNameField = this
                .addComponent(new QADColorTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 10));
        this.worldNameField.setFocused(true);
        this.worldNameField.setText(this.worldName);
        this.worldNameField.setMaxStringLength(30);
        this.worldSeedField = this.addComponent(new QADTextField(this.fontRenderer, this.width / 2 - 100, 80, 200, 10));
        this.worldSeedField.setText(this.worldSeed);
        this.lblName = addComponent(new QADLabel(I18n.format("selectWorld.enterName")));
        this.lblSeed = addComponent(new QADLabel(I18n.format("selectWorld.enterSeed")));
        this.lblSeedInfo = addComponent(new QADLabel(I18n.format("selectWorld.seedInfo")));
        btnCustomizeType.setEnabled(false);
        this.calcSaveDirName();
        this.updateDisplayState();

        btnBack.setAction(() -> {
            mc.displayGuiScreen(new MapSelector(null));
        });
        btnCreate.setAction(() -> {
            mc.displayGuiScreen(null);

            if (alreadyGenerated) {
                return;
            }

            alreadyGenerated = true;
            long i = (new Random()).nextLong();
            String s = worldSeedField.getText();

            if (!StringUtils.isEmpty(s)) {
                try {
                    long j = Long.parseLong(s);

                    if (j != 0L) {
                        i = j;
                    }
                } catch (NumberFormatException var7) {
                    i = s.hashCode();
                }
            }

            WorldType.WORLD_TYPES[selectedIndex].onGUICreateWorldPress();

            WorldSettings worldsettings = new WorldSettings(i, GameType.CREATIVE, generateStructuresEnabled,
                    hardCoreMode, WorldType.WORLD_TYPES[selectedIndex]);
            worldsettings.setGeneratorOptions(chunkProviderSettingsJson);

            if (bonusChestEnabled && !hardCoreMode) {
                worldsettings.enableBonusChest();
            }

            if (allowCheats && !hardCoreMode) {
                worldsettings.enableCommands();
            }

            mc.launchIntegratedServer(saveDirName, worldNameField.getText().trim(), worldsettings);
        });
        btnMapFeatures.setAction(() -> {
            // TODO Auto-generated method stub
            generateStructuresEnabled = !generateStructuresEnabled;
            updateDisplayState();
        });
        btnMapType.setAction(() -> {
            // TODO Auto-generated method stub
            ++selectedIndex;

            if (selectedIndex >= WorldType.WORLD_TYPES.length) {
                selectedIndex = 0;
            }

            while (!canSelectCurWorldType()) {
                ++selectedIndex;

                if (selectedIndex >= WorldType.WORLD_TYPES.length) {
                    selectedIndex = 0;
                }
            }
            btnCustomizeType.setEnabled(WorldType.WORLD_TYPES[selectedIndex].isCustomizable());

            chunkProviderSettingsJson = "";
            updateDisplayState();
        });
        btnAllowCommands.setAction(() -> {
            allowCheats = !allowCheats;
            updateDisplayState();
        });
        btnCustomizeType.setAction(() -> {
            // TODO Auto-generated method stub
            GuiCreateWorld dummy = new GuiCreateWorld(newgui);
            dummy.chunkProviderSettingsJson = MapCreator.this.chunkProviderSettingsJson;
            WorldType.WORLD_TYPES[selectedIndex].onCustomizeButton(mc, dummy);
        });

    }

    @Override
    public void updateGui() {
        this.btnCreate.setX(this.width / 2 - 155);
        this.btnCreate.setY(this.height - 38);
        this.btnBack.setX(this.width / 2 + 5);
        this.btnBack.setY(this.height - 38);
        this.btnMapFeatures.setX(this.width / 2 - 145);
        this.btnMapFeatures.setY(120);
        this.btnMapType.setX(this.width / 2 + 5);
        this.btnMapType.setY(120);
        this.btnAllowCommands.setX(btnMapFeatures.getX());
        this.btnAllowCommands.setY(145);
        this.worldNameField.setX(this.width / 2 - 100);
        this.worldNameField.setY(50);
        this.worldSeedField.setX(this.width / 2 - 100);
        this.worldSeedField.setY(80);
        this.btnCustomizeType.setX(btnMapType.getX());
        this.btnCustomizeType.setY(btnMapType.getY() + 22);
        lblName.setPosition(worldNameField.getX(), worldNameField.getY() - 10);
        lblSeed.setPosition(worldSeedField.getX(), worldSeedField.getY() - 10);
        lblSeedInfo.setPosition(worldSeedField.getX(), worldSeedField.getY() + 15);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!btnCustomizeType.isEnabeld() && btnCustomizeType.isMouserOver())
            drawHoveringText("This world type can not be customized!", mouseX, mouseY);

    }

    /**
     * Determine a save-directory name from the world name
     */
    private void calcSaveDirName() {
        this.saveDirName = this.worldNameField.getText().trim();

        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS) {
            this.saveDirName = this.saveDirName.replace(c0, '_');
        }

        if (StringUtils.isEmpty(this.saveDirName)) {
            this.saveDirName = "World";
        }

        this.saveDirName = getUncollidingSaveDirName(this.mc.getSaveLoader(), this.saveDirName);
    }

    /**
     * Sets displayed GUI elements according to the current settings state
     */
    private void updateDisplayState() {
//        this.btnGameMode.setText(I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode));
//        this.gameModeDesc1 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1");
//        this.gameModeDesc2 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2");
        this.btnMapFeatures.setText(I18n.format("selectWorld.mapFeatures") + " ");

        if (this.generateStructuresEnabled) {
            this.btnMapFeatures.setText(this.btnMapFeatures.getText() + I18n.format("options.on"));
        } else {
            this.btnMapFeatures.setText(this.btnMapFeatures.getText() + I18n.format("options.off"));
        }

        this.btnMapType.setText(I18n.format("selectWorld.mapType") + " "
                + I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslationKey()));
        this.btnAllowCommands.setText(I18n.format("selectWorld.allowCommands") + " ");

        if (this.allowCheats && !this.hardCoreMode) {
            this.btnAllowCommands.setText(this.btnAllowCommands.getText() + I18n.format("options.on"));
        } else {
            this.btnAllowCommands.setText(this.btnAllowCommands.getText() + I18n.format("options.off"));
        }
    }

    /**
     * Returns whether the currently-selected world type is actually acceptable for
     * selection Used to hide the "debug" world type.
     */
    private boolean canSelectCurWorldType() {
        WorldType worldtype = WorldType.WORLD_TYPES[this.selectedIndex];

        if (worldtype != null && worldtype.canBeCreated()) {
            return worldtype != WorldType.DEBUG_ALL_BLOCK_STATES;
        } else {
            return false;
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the
     * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
     * on the key), keyCode (lwjgl Keyboard key code)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void handleKeyboardInput() throws IOException {
        Keyboard.getEventCharacter();
        int keyCode = Keyboard.getEventKey();
        if (keyCode == 28 || keyCode == 156) {
            btnCreate.getAction().run();
        }

        this.calcSaveDirName();
        if (keyCode == 1)
            return;
        super.handleKeyboardInput();
    }

}
