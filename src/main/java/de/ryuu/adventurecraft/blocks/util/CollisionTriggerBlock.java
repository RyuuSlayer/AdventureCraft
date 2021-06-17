package de.ryuu.adventurecraft.blocks.util;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.blocks.ACBlockContainer;
import de.ryuu.adventurecraft.client.gui.blocks.GuiCollisionTriggerBlock;
import de.ryuu.adventurecraft.tileentity.CollisionTriggerBlockTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CollisionTriggerBlock extends ACBlockContainer {

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CollisionTriggerBlockTileEntity();
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityPlayerMP) {
            EntityPlayerMP p = (EntityPlayerMP) entityIn;
            if (p.capabilities.isCreativeMode)
                return;
        } else
            return;
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null && tileEntity instanceof CollisionTriggerBlockTileEntity) {
            ((CollisionTriggerBlockTileEntity) tileEntity).collision(entityIn);
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
        mc.displayGuiScreen(new GuiCollisionTriggerBlock((CollisionTriggerBlockTileEntity) worldIn.getTileEntity(pos)));

        return true;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

}