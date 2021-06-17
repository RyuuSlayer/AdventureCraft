package de.ryuu.adventurecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.ryuu.adventurecraft.client.gui.replaced_guis.GuiReplacer;
import de.ryuu.adventurecraft.managers.ACWorldsManager;
import de.ryuu.adventurecraft.network.AdventureCraftNetwork;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import de.ryuu.adventurecraft.proxy.CommonProxy;
import de.ryuu.adventurecraft.script.GlobalScriptManager;
import de.ryuu.adventurecraft.server.ServerHandler;
import de.ryuu.adventurecraft.util.ACDataFixer;
import de.ryuu.adventurecraft.util.ConfigurationManager;
import de.ryuu.adventurecraft.util.GuiHandler;
import de.ryuu.adventurecraft.util.TimedExecutor;
import de.ryuu.adventurecraft.versionchecker.SendMessage;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Random;

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
    public static World lastVisitedWorld = null;
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY, modId = Reference.MOD_ID)
    public static CommonProxy proxy;

    @SideOnly(Side.CLIENT)
    public static ClientProxy asClient() {
        return proxy.asClient();
    }

    public static NBTTagCompound getSettings(EntityPlayer player) {
        return proxy.getSettings(player);
    }

    // public TaleCraft() {
//		// TODO Auto-generated constructor stub
//		System.out.println(System.getProperty("java.class.path"));
//		System.exit(0);
//	}
    @Mod.EventHandler
    public void modConstructing(FMLConstructionEvent event) {
        Loader.isModLoaded("discordrpc");
    }

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
            MinecraftForge.EVENT_BUS.register(new GuiReplacer());
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
        // noinspection ConstantConditions
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

}