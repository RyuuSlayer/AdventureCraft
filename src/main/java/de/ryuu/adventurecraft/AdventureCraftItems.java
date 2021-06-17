package de.ryuu.adventurecraft;

import com.google.common.collect.Lists;
import de.ryuu.adventurecraft.items.*;
import de.ryuu.adventurecraft.items.weapon.*;
import de.ryuu.adventurecraft.items.world.ACInstrumentItem;
import de.ryuu.adventurecraft.items.world.ACWorldItem;
import de.ryuu.adventurecraft.items.world.HeartItem;
import de.ryuu.adventurecraft.items.world.KeyItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Instrument;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

@EventBusSubscriber
public class AdventureCraftItems {
    public static final List<Item> ALL_AC_ITEMS = Lists.newArrayList();
    public static WandItem wand;
    public static FillerItem filler;
    public static EraserItem eraser;
    public static TeleporterItem teleporter;
    public static VoxelatorItem voxelbrush;
    public static NudgeItem nudger;
    public static CopyItem copy;
    public static PasteItem paste;
    public static CutItem cut;
    public static MetaSwapperItem metaswapper;
    public static SpawnPointItem spawnpoint;
    public static NPCEditorItem npceditor;
    public static EntityCloneItem entityclone;
    public static CameraItem camera;
    public static DecoratorItem decorator;
    public static TriggerItem trigger;
    public static CustomPaintingItem custompainting;
    public static MovingBlockCreator movingblockcreator;
    public static KeyItem silverKey;
    public static KeyItem goldKey;
    public static HeartItem heart;
    public static BombItem bomb;
    public static ACWorldItem goldCoin;
    public static ACWorldItem silverCoin;
    public static ACWorldItem emeraldCoin;
    public static PistolItem pistol;
    public static PistolClipItem pistolClip;
    public static RifleItem rifle;
    public static RifleClipItem rifleClip;
    public static ShotgunItem shotgun;
    public static ShotgunClipItem shotgunClip;
    public static BombArrowItem bombArrow;
    public static BoomerangItem boomerang;
    public static KnifeItem knife;
    public static ACInstrumentItem harp;
    public static ACInstrumentItem guitar;
    public static ACInstrumentItem drums;
    private static IForgeRegistry<Item> registry;

    @SubscribeEvent
    public static void init(final RegistryEvent.Register<Item> event) {
        registry = event.getRegistry();

        wand = register(new WandItem(), "wand");
        filler = register(new FillerItem(), "filler");
        eraser = register(new EraserItem(), "eraser");
        teleporter = register(new TeleporterItem(), "teleporter");
        voxelbrush = register(new VoxelatorItem(), "voxelbrush");
        nudger = register(new NudgeItem(), "nudger");
        copy = register(new CopyItem(), "copy");
        paste = register(new PasteItem(), "paste");
        cut = register(new CutItem(), "cut");
        metaswapper = register(new MetaSwapperItem(), "metaswapper");
        spawnpoint = register(new SpawnPointItem(), "spawnpoint");
        npceditor = register(new NPCEditorItem(), "npceditor");
        entityclone = register(new EntityCloneItem(), "npcclone");
        camera = register(new CameraItem(), "camera");
        decorator = register(new DecoratorItem(), "decorator");
        trigger = register(new TriggerItem(), "trigger");
        custompainting = register(new CustomPaintingItem(), "custompainting");
        movingblockcreator = register(new MovingBlockCreator(), "movingblockcreator");

        silverKey = register(new KeyItem(), "silverkey");
        goldKey = register(new KeyItem(), "goldkey");
        bomb = register(new BombItem(), "bomb");
        heart = register(new HeartItem(), "heart");
        goldCoin = register(new ACWorldItem(), "goldcoin");
        silverCoin = register(new ACWorldItem(), "silvercoin");
        emeraldCoin = register(new ACWorldItem(), "emeraldcoin");

        pistolClip = register(new PistolClipItem(), "pistolclip");
        pistol = register(new PistolItem(), "pistol");
        rifleClip = register(new RifleClipItem(), "rifleclip");
        rifle = register(new RifleItem(), "rifle");
        shotgunClip = register(new ShotgunClipItem(), "shotgunclip");
        shotgun = register(new ShotgunItem(), "shotgun");
        bombArrow = register(new BombArrowItem(), "bombarrow");
        boomerang = register(new BoomerangItem(), "boomerang");
        knife = register(new KnifeItem(), "knife");

        harp = register(new ACInstrumentItem(Instrument.PIANO), "harp");
        guitar = register(new ACInstrumentItem(Instrument.BASSGUITAR), "guitar");
        drums = register(new ACInstrumentItem(Instrument.BASSDRUM), "drums");

        MinecraftForge.EVENT_BUS.register(boomerang);

        registerItemBlocks();
    }

    private static void registerItemBlocks() {
        for (Block block : AdventureCraftBlocks.blocks) {
            if (AdventureCraftBlocks.customItemBlocks.contains(block)) {
                registry.register(
                        new AdventureCraftBlocks.ItemBlockBlankBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
            } else {
                registry.register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
            }
        }
    }

    private static <T extends Item> T register(T item, String name) {
        item.setUnlocalizedName(name);
        registry.register(item.getRegistryName() == null ? item.setRegistryName(name) : item);
        ALL_AC_ITEMS.add(item);
        return item;
    }
}