package de.ryuu.adventurecraft.blocks.util;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.blocks.ACBlockContainer;
import de.ryuu.adventurecraft.blocks.ACITriggerableBlock;
import de.ryuu.adventurecraft.client.gui.blocks.GuiTriggerFilterBlock;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.tileentity.TriggerFilterBlockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TriggerFilterBlock extends ACBlockContainer implements ACITriggerableBlock {

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TriggerFilterBlockTileEntity();
    }

    @Override
    public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
        if (world.isRemote)
            return;

        TileEntity tileentity = world.getTileEntity(position);

        if (tileentity instanceof TriggerFilterBlockTileEntity) {
            ((TriggerFilterBlockTileEntity) tileentity).trigger(triggerState);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
            return true;
        if (!AdventureCraft.proxy.isBuildMode())
            return false;
        if (playerIn.isSneaking())
            return true;

        Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen(new GuiTriggerFilterBlock((TriggerFilterBlockTileEntity) worldIn.getTileEntity(pos)));

        return true;
    }

}