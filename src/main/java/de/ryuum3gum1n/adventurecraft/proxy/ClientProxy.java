package de.ryuum3gum1n.adventurecraft.proxy;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;
import de.ryuum3gum1n.adventurecraft.client.ClientKeyboardHandler;
import de.ryuum3gum1n.adventurecraft.client.ClientNetworkHandler;
import de.ryuum3gum1n.adventurecraft.client.ClientRenderer;
import de.ryuum3gum1n.adventurecraft.client.ClientSettings;
import de.ryuum3gum1n.adventurecraft.client.InfoBar;
import de.ryuum3gum1n.adventurecraft.client.InvokeTracker;
import de.ryuum3gum1n.adventurecraft.client.commands.AdventureCraftClientCommands;
import de.ryuum3gum1n.adventurecraft.client.environment.Environments;
import de.ryuum3gum1n.adventurecraft.client.gui.entity.npc.GuiNPCMerchant;
import de.ryuum3gum1n.adventurecraft.client.render.metaworld.CustomPaintingRender;
import de.ryuum3gum1n.adventurecraft.client.render.metaworld.PasteItemRender;
import de.ryuum3gum1n.adventurecraft.client.render.renderables.SelectionBoxRenderer;
import de.ryuum3gum1n.adventurecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.ryuum3gum1n.adventurecraft.clipboard.ClipboardItem;
import de.ryuum3gum1n.adventurecraft.entity.NPC.NPCShop;
import de.ryuum3gum1n.adventurecraft.util.ReflectionUtil;

public class ClientProxy extends CommonProxy implements ISelectiveResourceReloadListener {
	// All the singletons!
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final ClientSettings settings = new ClientSettings();
	public static ClientProxy proxy = (ClientProxy) AdventureCraft.proxy;

	// ac internals
	private ClipboardItem currentClipboardItem;
	private InfoBar infoBarInstance;
	private InvokeTracker invokeTracker;
	private ClientNetworkHandler clientNetworkHandler;
	private ClientKeyboardHandler clientKeyboardHandler;
	private ClientRenderer clientRenderer;
	private ConcurrentLinkedDeque<Runnable> clientTickQeue;

	// Client settings
	public GameRules gamerules;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		settings.init();

		MinecraftForge.EVENT_BUS.register(this);

		clientKeyboardHandler = new ClientKeyboardHandler(this);
		AdventureCraftClientCommands.init();

		IReloadableResourceManager resManager = (IReloadableResourceManager) mc.getResourceManager();
		resManager.registerReloadListener(this);

		clientTickQeue = new ConcurrentLinkedDeque<Runnable>();
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

	private NPCShop lastOpened;

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
			// Save the AdventureCraft settings on World unload.
			// Just to be sure...
			settings.save();
		}
	}

	/**
	 * @return TRUE, if the client is in build-mode (aka: creative-mode), FALSE if
	 *         not.
	 **/
	@Override
	public boolean isBuildMode() {
		return mc.playerController != null && mc.playerController.isInCreativeMode();
	}

	@Override
	public void tick(TickEvent event) {
		super.tick(event);

		if (event instanceof ClientTickEvent) {
			while (!clientTickQeue.isEmpty())
				clientTickQeue.poll().run();
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

	/***********************************/
	/**                               **/
	/**                               **/
	/**                               **/
	/***********************************/

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
	public void setClipboard(ClipboardItem item) {
		currentClipboardItem = item;
	}

	/****/
	public ClipboardItem getClipboard() {
		return currentClipboardItem;
	}

	/****/
	public static final boolean isInBuildMode() {
		if (proxy == null)
			proxy = AdventureCraft.proxy.asClient();

		return proxy.isBuildMode();
	}

	/****/
	public void sendChatMessage(String message) {
		mc.player.sendChatMessage(message);
	}

	public void sheduleClientTickTask(Runnable runnable) {
		this.clientTickQeue.push(runnable);
	}

	public static void shedule(Runnable runnable) {
		proxy.sheduleClientTickTask(runnable);
	}

	public ClientKeyboardHandler getKeyboardHandler() {
		return clientKeyboardHandler;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		// TODO Auto-generated method stub

	}

}