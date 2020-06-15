package de.ryuu.adventurecraft.client.init;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.entities.BombEntity;
import de.ryuu.adventurecraft.client.items.BombItem;
import de.ryuu.adventurecraft.client.items.MineItem;
import de.ryuu.adventurecraft.client.items.PulverisItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AdventureCraftItems {

    public static Item BOMB;
    public static Item GOLDEN_BOMB;
    public static Item DIAMOND_BOMB;
    public static Item PULVERIS;
    public static Item NAVAL_MINE;

    public static void init() {
        BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), AdventureCraftEntities.BOMB), "bomb");
        GOLDEN_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), AdventureCraftEntities.GOLDEN_BOMB), "golden_bomb");
        DIAMOND_BOMB = registerItem(new BombItem(new Item.Settings().group(ItemGroup.TOOLS), AdventureCraftEntities.DIAMOND_BOMB), "diamond_bomb");
        PULVERIS = registerItem(new PulverisItem(new Item.Settings().group(ItemGroup.TOOLS), AdventureCraftEntities.PULVERIS), "pulveris");
        NAVAL_MINE = registerItem(new MineItem(new Item.Settings().group(ItemGroup.TOOLS), AdventureCraftEntities.NAVAL_MINE), "naval_mine");
    }

    public static Item registerItem(Item item, String name) {
        registerItem(item, name, item instanceof BombItem);

        return item;
    }

    public static Item registerItem(Item item, String name, boolean registerDispenserBehavior) {
        Registry.register(Registry.ITEM, AdventureCraft.MOD_ID + ":" + name, item);
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    BombEntity bombEntity = ((BombItem) itemStack.getItem()).getType().create(world);
                    bombEntity.setPos(position.getX(), position.getY(), position.getZ());
                    itemStack.decrement(1);
                    return bombEntity;
                }
            });
        }

        return item;
    }
}