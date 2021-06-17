package de.ryuu.adventurecraft.items.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent.Instrument;

public class ACInstrumentItem extends ACWorldItem {

    private Instrument instrument;

    public ACInstrumentItem(Instrument instrument) {
        setMaxStackSize(1);
        this.instrument = instrument;
    }

    public static SoundEvent getInstrument(Instrument instrument) {
        switch (instrument) {
            case BASSDRUM:
                return SoundEvents.BLOCK_NOTE_BASEDRUM;
            case BASSGUITAR:
                return SoundEvents.BLOCK_NOTE_BASS;
            case CLICKS:
                return SoundEvents.BLOCK_NOTE_HAT;
            case PIANO:
                return SoundEvents.BLOCK_NOTE_HARP;
            case SNARE:
                return SoundEvents.BLOCK_NOTE_SNARE;
            default:
                return SoundEvents.BLOCK_NOTE_HARP;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (world.isRemote)
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        int note = world.rand.nextInt(25);
        byte b = (byte) (note % 25);
        float pitch = (float) Math.pow(2.0D, (double) (b - 12) / 12.0D);
        world.playSound(null, player.getPosition(), getInstrument(instrument), SoundCategory.AMBIENT, 1F, pitch);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

}