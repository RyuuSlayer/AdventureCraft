package de.ryuu.adventurecraft.client;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.Reference;
import de.ryuu.adventurecraft.client.gui.nbt.GuiNBTEditor;
import de.ryuu.adventurecraft.items.weapon.ACGunItem;
import de.ryuu.adventurecraft.network.packets.GunReloadPacket;
import de.ryuu.adventurecraft.network.packets.TriggerItemPacket;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class ClientKeyboardHandler {
    private static final String category = "key.categories." + Reference.MOD_ID;
    private final ClientProxy proxy;
    private final Minecraft mc;
    private final KeyBinding mapSettingsBinding;
    private final KeyBinding buildModeBinding;
    private final KeyBinding visualizationBinding;
    private final KeyBinding nbt;
    private final KeyBinding reload;
    private final KeyBinding triggerItem;

    public ClientKeyboardHandler(ClientProxy clientProxy) {
        proxy = clientProxy;
        mc = Minecraft.getMinecraft();

        mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, category);
        buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, category);
        visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, category);
        nbt = new KeyBinding("key.nbt", Keyboard.KEY_N, category);
        reload = new KeyBinding("key.reload", Keyboard.KEY_R, category);
        triggerItem = new KeyBinding("key.triggeritem", Keyboard.KEY_V, category);

        // register all keybindings
        ClientRegistry.registerKeyBinding(mapSettingsBinding);
        ClientRegistry.registerKeyBinding(buildModeBinding);
        ClientRegistry.registerKeyBinding(visualizationBinding);
        // ClientRegistry.registerKeyBinding(nbt);
        ClientRegistry.registerKeyBinding(reload);
        ClientRegistry.registerKeyBinding(triggerItem);
    }

    public void on_key(KeyInputEvent event) {
        // opens the NBT editor
        if (nbt.isPressed() && nbt.isKeyDown() && mc.player != null && !mc.isGamePaused()) {
            InventoryPlayer player = mc.player.inventory;
            player.getCurrentItem();
            FMLCommonHandler.instance().showGuiScreen(new GuiNBTEditor(player.getCurrentItem().getTagCompound()));
            return;
        }
        if (triggerItem.isPressed() && triggerItem.isKeyDown() && mc.world != null && mc.player != null
                && !mc.isGamePaused()) {
            AdventureCraft.network.sendToServer(new TriggerItemPacket(mc.player.getUniqueID()));
            return;
        }
        // this toggles between the various visualization modes
        if (visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
            proxy.getRenderer().setVisualizationMode(proxy.getRenderer().getVisualizationMode().next());
        }
        if (reload.isPressed() && reload.isKeyDown()) {
            ItemStack stack = mc.player.inventory.getCurrentItem();
            if (stack.getItem() instanceof ACGunItem) {
                ACGunItem gun = (ACGunItem) stack.getItem();
                int index = gun.getClipInInventory(mc.player.inventory);
                if (index != -1) {
                    if (stack.getItemDamage() > 0) {
                        mc.world.playSound(mc.player, mc.player.getPosition(), gun.reloadSound(), SoundCategory.AMBIENT,
                                5F, 1F);
                        AdventureCraft.network.sendToServer(new GunReloadPacket(mc.player.getUniqueID()));
                    }
                }
            }
        }
        // this toggles between buildmode and adventuremode
        if (buildModeBinding.isPressed() && buildModeBinding.isKeyDown() && mc.world != null && mc.player != null
                && !mc.isGamePaused()) {
            AdventureCraft.logger.info("Switching GameMode using the buildmode-key.");
            mc.player.sendChatMessage("/gamemode " + (proxy.isBuildMode() ? "2" : "1"));
            if (mapSettingsBinding.isPressed() && mapSettingsBinding.isKeyDown()) {
                proxy.isBuildMode();
            }// XXX: Disabled functionality.
// mc.displayGuiScreen(new GuiMapControl());

        }
    }
}