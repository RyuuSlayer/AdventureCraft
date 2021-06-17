package de.ryuu.adventurecraft.proxy;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.client.*;
import de.ryuu.adventurecraft.client.commands.AdventureCraftClientCommands;
import de.ryuu.adventurecraft.client.environment.Environments;
import de.ryuu.adventurecraft.client.gui.entity.npc.GuiNPCMerchant;
import de.ryuu.adventurecraft.client.render.metaworld.CustomPaintingRender;
import de.ryuu.adventurecraft.client.render.metaworld.PasteItemRender;
import de.ryuu.adventurecraft.client.render.renderables.SelectionBoxRenderer;
import de.ryuu.adventurecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.ryuu.adventurecraft.clipboard.ClipboardItem;
import de.ryuu.adventurecraft.entity.NPC.NPCShop;
import de.ryuu.adventurecraft.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.util.concurrent.ConcurrentLinkedDeque;

@SuppressWarnings("SameReturnValue")
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
    // All the singletons!
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final ClientSettings settings = new ClientSettings();
    public static ClientProxy proxy = (ClientProxy) AdventureCraft.proxy;
    // Client settings
    public GameRules gamerules;
    // ac internals
    private ClipboardItem currentClipboardItem;
    private InfoBar infoBarInstance;
    private InvokeTracker invokeTracker;
    private ClientNetworkHandler clientNetworkHandler;
    private ClientKeyboardHandler clientKeyboardHandler;
    private ClientRenderer clientRenderer;
    private ConcurrentLinkedDeque<Runnable> clientTickQueue;
    private NPCShop lastOpened;

    /****/
    public static boolean isInBuildMode() {
        if (proxy == null)
            proxy = AdventureCraft.proxy.asClient();

        return proxy.isBuildMode();
    }

    public static void schedule(Runnable runnable) {
        proxy.scheduleClientTickTask(runnable);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        settings.init();

        MinecraftForge.EVENT_BUS.register(this);

        clientKeyboardHandler = new ClientKeyboardHandler(this);
        AdventureCraftClientCommands.init();

        IReloadableResourceManager resManager = (IReloadableResourceManager) mc.getResourceManager();
        resManager.registerReloadListener(this);

        clientTickQueue = new ConcurrentLinkedDeque<>();
        clientRenderer = new ClientRenderer(this);
        clientRenderer.preInit();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // create client network'er
        clientNetworkHandler = new ClientNetworkHandler(this);

        // add all static renderers
        clientRenderer.addStaticRenderer(new SelectionBoxRenderer());

        ReflectionUtil.replaceMusicTicker();

    } // init(..){}

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        // Create the InfoBar Instance
        infoBarInstance = new InfoBar();

        // Create the invoke tracker instance
        invokeTracker = new InvokeTracker();

        ItemMetaWorldRenderer.ITEM_RENDERS.put(AdventureCraftItems.paste, new PasteItemRender());
        ItemMetaWorldRenderer.ITEM_RENDERS.put(AdventureCraftItems.custompainting, new CustomPaintingRender());

        gamerules = new GameRules();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Environments.reload(resourceManager);
    }

    @SubscribeEvent
    public void worldPass(RenderWorldLastEvent event) {
        clientRenderer.on_render_world_post(event);
    }

    @SubscribeEvent
    public void npcTradeOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (((GuiMerchant) event.getGui()).getMerchant() instanceof NPCShop) {
                lastOpened = (NPCShop) ((GuiMerchant) event.getGui()).getMerchant();
            } else {
                Minecraft mc = Minecraft.getMinecraft();
                event.setGui(new GuiNPCMerchant(mc.player.inventory, lastOpened, mc.world));
            }
        }
    }

    @SubscribeEvent
    public void worldPostRenderHand(RenderHandEvent event) {
        clientRenderer.on_render_world_hand_post(event);
    }

    /**
     * This method is called when the world is unloaded.
     **/
    @Override
    public void unloadWorld(World world) {
        if (world instanceof WorldClient) {
            // the client is either changing dimensions or leaving the server.
            // reset all temporary world related settings here
            // delete all temporary world related objects here

            clientRenderer.on_world_unload();

            // This is stupid but,
            // Save the TaleCraft settings on World unload.
            // Just to be sure...
            settings.save();
        }
    }

    /********************************/
    /*                               **/
    /*                               **/
    /*                               **/
    /********************************/

    /**
     * @return TRUE, if the client is in build-mode (aka: creative-mode), FALSE if
     * not.
     **/
    @Override
    public boolean isBuildMode() {
        return mc.playerController != null && mc.playerController.isInCreativeMode();
    }

    @Override
    public void tick(TickEvent event) {
        super.tick(event);

        if (event instanceof ClientTickEvent) {
            while (!clientTickQueue.isEmpty())
                clientTickQueue.poll().run();
        }
        if (event instanceof RenderTickEvent) {
            RenderTickEvent revt = (RenderTickEvent) event;

            // Pre-Scene Render
            if (revt.phase == Phase.START) {
                clientRenderer.on_render_world_terrain_pre(revt);
            } else
                // Post-World >> Pre-HUD Render
                if (revt.phase == Phase.END) {
                    clientRenderer.on_render_world_terrain_post(revt);
                }
        }
    }

    /****/
    @Override
    public NBTTagCompound getSettings(EntityPlayer playerIn) {
        return getSettings().getNBT();
    }

    /****/
    public ClientSettings getSettings() {
        return settings;
    }

    public InfoBar getInfoBar() {
        return infoBarInstance;
    }

    public InvokeTracker getInvokeTracker() {
        return invokeTracker;
    }

    public ClientNetworkHandler getNetworkHandler() {
        return clientNetworkHandler;
    }

    public ClientRenderer getRenderer() {
        return clientRenderer;
    }

    /****/
    public ClipboardItem getClipboard() {
        return currentClipboardItem;
    }

    /****/
    public void setClipboard(ClipboardItem item) {
        currentClipboardItem = item;
    }

    /****/
    public void sendChatMessage(String message) {
        mc.player.sendChatMessage(message);
    }

    public void scheduleClientTickTask(Runnable runnable) {
        this.clientTickQueue.push(runnable);
    }

    public ClientKeyboardHandler getKeyboardHandler() {
        return clientKeyboardHandler;
    }

}
