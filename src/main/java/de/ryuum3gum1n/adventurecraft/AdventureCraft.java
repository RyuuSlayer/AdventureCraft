package de.ryuum3gum1n.adventurecraft;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.ryuum3gum1n.adventurecraft.client.gui.worlddesc.WorldSelectorInjector;
import de.ryuum3gum1n.adventurecraft.managers.ACWorldsManager;
import de.ryuum3gum1n.adventurecraft.network.AdventureCraftNetwork;
import de.ryuum3gum1n.adventurecraft.proxy.ClientProxy;
import de.ryuum3gum1n.adventurecraft.proxy.CommonProxy;
import de.ryuum3gum1n.adventurecraft.script.GlobalScriptManager;
import de.ryuum3gum1n.adventurecraft.server.ServerHandler;
import de.ryuum3gum1n.adventurecraft.util.ConfigurationManager;
import de.ryuum3gum1n.adventurecraft.util.GuiHandler;
import de.ryuum3gum1n.adventurecraft.util.ACDataFixer;
import de.ryuum3gum1n.adventurecraft.util.TimedExecutor;
import de.ryuum3gum1n.adventurecraft.versionchecker.SendMessage;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class AdventureCraft {
	@Mod.Instance(Reference.MOD_ID)
	public static AdventureCraft instance;
	public static ModContainer container;
	public static ACWorldsManager worldsManager;
	public static AdventureCraftEventHandler eventHandler;
	public static GlobalScriptManager globalScriptManager;
	public static SimpleNetworkWrapper network;
	public static TimedExecutor timedExecutor;
	public static Logger logger;
	public static Random random;
	public static Gson gson;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY, modId = Reference.MOD_ID)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		container = FMLCommonHandler.instance().findContainerFor(instance);
		logger.info("AdventureCraft initialization...");
		logger.info("AdventureCraft Version: " + Reference.MOD_VERSION);
		logger.info("AdventureCraft ModContainer: " + container);
		logger.info("Creating/Reading configuration file!");
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigurationManager.init(config);
		config.save();
		logger.info("Configuration loaded!");
		MinecraftForge.EVENT_BUS.register(this);

		AdventureCraftNetwork.preInit();
		random = new Random(42);

		worldsManager = new ACWorldsManager(this);
		timedExecutor = new TimedExecutor();
		globalScriptManager = new GlobalScriptManager();
		globalScriptManager.init(this, proxy);

		gson = new GsonBuilder().setPrettyPrinting().create();

		// Print debug information
		logger.info("AdventureCraft CoreManager @" + worldsManager.hashCode());
		logger.info("AdventureCraft TimedExecutor @" + timedExecutor.hashCode());
		logger.info("AdventureCraft NET SimpleNetworkWrapper @" + network.hashCode());

		// Create and register the event handler
		eventHandler = new AdventureCraftEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		MinecraftForge.EVENT_BUS.register(new SendMessage());

		if (ConfigurationManager.USE_VERSION_CHECKER && event.getSide() == Side.CLIENT)
			MinecraftForge.EVENT_BUS.register(new WorldSelectorInjector());
		logger.info("AdventureCraft Event Handler @" + eventHandler.hashCode());
		// Initialize all the Tabs/Blocks/Items/Commands etc.
		logger.info("Loading Tabs, Blocks, Items, Entities and Commands");
		AdventureCraftTabs.init();
		AdventureCraftEntities.init();
		AdventureCraftCommands.init();

		// Initialize the Proxy
		logger.info("Initializing Proxy...");
		proxy.adventureCraft = this;
		proxy.preInit(event);

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(AdventureCraft.instance, new GuiHandler());

		// Convert tile entity IDs
		CompoundDataFixer compoundDataFixer = FMLCommonHandler.instance().getDataFixer();
		ModFixs dataFixer = compoundDataFixer.init(Reference.MOD_ID, 1);
		dataFixer.registerFix(FixTypes.BLOCK_ENTITY, new ACDataFixer());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		logger.info("AdventureCraft initialized, all systems ready.");
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		MinecraftServer server = event.getServer();

		ICommandManager cmdmng = server.getCommandManager();
		if (cmdmng instanceof ServerCommandManager && cmdmng instanceof CommandHandler) {
			CommandHandler cmdhnd = (CommandHandler) cmdmng;
			AdventureCraftCommands.register(cmdhnd);
		}

		// By calling this method, we create the ServerMirror for the given server.
		ServerHandler.getServerMirror(server);
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		// Calling this method destroys all server instances that exist,
		// because the 'event' given above does NOT give us the server-instance that is
		// being stopped.
		ServerHandler.destroyServerMirror(null);
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		logger.info("Server started: " + event + " [ACINFO]");
		AdventureCraftGameRules
				.registerGameRules(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules());
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		// logger.info("Server stopped: " + event + " [ACINFO]");
	}

	@SideOnly(Side.CLIENT)
	public static ClientProxy asClient() {
		return proxy.asClient();
	}

	public static NBTTagCompound getSettings(EntityPlayer player) {
		return proxy.getSettings(player);
	}

}