package de.ryuum3gum1n.adventurecraft;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import de.ryuum3gum1n.adventurecraft.blocks.util.BlockUpdateDetector;
import de.ryuum3gum1n.adventurecraft.blocks.util.CameraBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.ClockBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.CollisionTriggerBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.DelayBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.EmitterBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.HiddenBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.ImageHologramBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.InverterBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.KillBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.LightBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.MemoryBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.MessageBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.MusicBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.RedstoneActivatorBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.RedstoneTriggerBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.RelayBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.ScriptBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.StorageBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.SummonBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.TriggerFilterBlock;
import de.ryuum3gum1n.adventurecraft.blocks.util.URLBlock;
import de.ryuum3gum1n.adventurecraft.blocks.world.LockedDoorBlock;
import de.ryuum3gum1n.adventurecraft.blocks.world.SpikeBlock;
import de.ryuum3gum1n.adventurecraft.blocks.world.WorkbenchBlock;
import de.ryuum3gum1n.adventurecraft.tileentity.BlockUpdateDetectorTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.CameraBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.ClockBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.CollisionTriggerBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.DelayBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.EmitterBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.ImageHologramBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.InverterBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.LightBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.LockedDoorTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.MemoryBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.MessageBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.MusicBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.RedstoneTriggerBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.RelayBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.ScriptBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.StorageBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.SummonBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.TriggerFilterBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.URLBlockTileEntity;

@EventBusSubscriber
public class AdventureCraftBlocks {
	public static HashMap<String, Block> blocksMap = Maps.newHashMap();
	public static List<Block> blocks = Lists.newArrayList();
	public static List<Block> customItemBlocks = Lists.newArrayList();
	private static IForgeRegistry<Block> registry;

	// UTILITY
	public static KillBlock killBlock;
	public static ClockBlock clockBlock;
	public static RedstoneTriggerBlock redstoneTrigger;
	public static RedstoneActivatorBlock redstoneActivator;
	public static RelayBlock relayBlock;
	public static ScriptBlock scriptBlock;
	public static BlockUpdateDetector updateDetectorBlock;
	public static StorageBlock storageBlock;
	public static EmitterBlock emitterBlock;
	public static ImageHologramBlock imageHologramBlock;
	public static CollisionTriggerBlock collisionTriggerBlock;
	public static LightBlock lightBlock;
	public static HiddenBlock hiddenBlock;
	public static MessageBlock messageBlock;
	public static InverterBlock inverterBlock;
	public static MemoryBlock memoryBlock;
	public static TriggerFilterBlock triggerFilterBlock;
	public static DelayBlock delayBlock;
	public static URLBlock urlBlock;
	public static SummonBlock summonBlock;
	public static MusicBlock musicBlock;
	public static CameraBlock cameraBlock;
	
	// WORLD
	public static LockedDoorBlock lockedDoorBlock;
	public static SpikeBlock spikeBlock;
	public static WorkbenchBlock workbench;

	@SubscribeEvent
	public static void init(final RegistryEvent.Register<Block> event) {
		registry = event.getRegistry();
		
		blocksMap = Maps.newHashMap();
		blocks = Lists.newArrayList();

		init_utility();
		init_world();
	}

	private static void init_world() {
		lockedDoorBlock = registerWithTE("lockeddoorblock", new LockedDoorBlock(), LockedDoorTileEntity.class);
	}
	
	private static void init_utility() {
		killBlock = register("killblock", new KillBlock());

		clockBlock = registerWithTE("clockblock", new ClockBlock(), ClockBlockTileEntity.class);

		redstoneTrigger = registerWithTE("redstone_trigger", new RedstoneTriggerBlock(), RedstoneTriggerBlockTileEntity.class);

		redstoneActivator = register("redstone_activator", new RedstoneActivatorBlock());

		relayBlock = registerWithTE("relayblock", new RelayBlock(), RelayBlockTileEntity.class);

		scriptBlock = registerWithTE("scriptblock", new ScriptBlock(), ScriptBlockTileEntity.class);

		updateDetectorBlock = registerWithTE("updatedetectorblock", new BlockUpdateDetector(), BlockUpdateDetectorTileEntity.class);

		storageBlock = registerWithTE("storageblock", new StorageBlock(), StorageBlockTileEntity.class);

		emitterBlock = registerWithTE("emitterblock", new EmitterBlock(), EmitterBlockTileEntity.class);

		imageHologramBlock = registerWithTE("imagehologramblock", new ImageHologramBlock(), ImageHologramBlockTileEntity.class);

		collisionTriggerBlock = registerWithTE("collisiontriggerblock", new CollisionTriggerBlock(), CollisionTriggerBlockTileEntity.class);

		lightBlock = registerWithTE("lightblock", new LightBlock(), LightBlockTileEntity.class);

		hiddenBlock = register("hiddenblock", new HiddenBlock());

		messageBlock = registerWithTE("messageblock", new MessageBlock(), MessageBlockTileEntity.class);

		inverterBlock = registerWithTE("inverterblock", new InverterBlock(), InverterBlockTileEntity.class);

		memoryBlock = registerWithTE("memoryblock", new MemoryBlock(), MemoryBlockTileEntity.class);

		triggerFilterBlock = registerWithTE("triggerfilterblock", new TriggerFilterBlock(), TriggerFilterBlockTileEntity.class);

		delayBlock = registerWithTE("delayblock", new DelayBlock(), DelayBlockTileEntity.class);

		urlBlock = registerWithTE("urlblock", new URLBlock(), URLBlockTileEntity.class);

		summonBlock = registerWithTE("summonblock", new SummonBlock(), SummonBlockTileEntity.class);
		
		musicBlock = registerWithTE("musicblock", new MusicBlock(), MusicBlockTileEntity.class);
		
		cameraBlock = registerWithTE("camerablock", new CameraBlock(), CameraBlockTileEntity.class);
		
		spikeBlock = register("spikeblock", new SpikeBlock());
		workbench = register("workbench", new WorkbenchBlock());
	}

	private static <T extends Block> T register(String name, T block) {
		block.setUnlocalizedName("adventurecraft:" + name);
		block.setRegistryName(Reference.MOD_ID, name);
		registry.register(block);
		addToMaps(block, name);
		return block;
	}
	
	private static <T extends Block> T register(String name, T block, boolean customItemBlock) {
		block.setUnlocalizedName("adventurecraft:" + name);
		block.setRegistryName(Reference.MOD_ID, name);
		registry.register(block);
		addToMaps(block, name);
		if(customItemBlock) customItemBlocks.add(block);
		return block;
	}
	
	private static <T extends Block, E extends TileEntity> T registerWithTE(String name, T block, Class<E> tileEntityClass) {
		T returnBlock = register(name, block);
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation("adventurecraft", name));
		return returnBlock;
	}
	
	private static void addToMaps(Block block, String name) {
		blocksMap.put(name, block);
		blocks.add(block);
		Item item = Item.getItemFromBlock(block);
		if(item != null)AdventureCraftItems.ALL_AC_ITEMS.add(item);
	}

	public static class ItemBlockBlankBlock extends ItemMultiTexture {
		public ItemBlockBlankBlock(Block block) {
			super(block, block, new String[] {
					"0",
					"1",
					"2",
					"3",
					"4",
					"5",
					"6",
					"7",
					"8",
					"9",
					"10",
					"11",
					"12",
					"13",
					"14",
					"15"
			});
		}

		@Override
		public int getMetadata(int damage) {
			return damage;
		}
	}
}