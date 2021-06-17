package de.ryuu.adventurecraft.blocks.util;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.blocks.ACBlockContainer;
import de.ryuu.adventurecraft.blocks.ACITriggerableBlock;
import de.ryuu.adventurecraft.client.gui.blocks.GuiClockBlock;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.tileentity.ClockBlockTileEntity;
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

public class ClockBlock extends ACBlockContainer implements ACITriggerableBlock {

    public ClockBlock() {
        super();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ClockBlockTileEntity();
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
        mc.displayGuiScreen(new GuiClockBlock((ClockBlockTileEntity) worldIn.getTileEntity(pos)));

        return true;
    }

    @Override
    public void trigger(World world, BlockPos position, EnumTriggerState triggerState) {
        ClockBlockTileEntity tEntity = (ClockBlockTileEntity) world.getTileEntity(position);
        if (tEntity != null) {
            switch (triggerState) {
                case ON:
                    tEntity.clockStart();
                    break;
                case OFF:
                    tEntity.clockStop();
                    break;
                case INVERT:
                    tEntity.clockPause();
                    break;
                case IGNORE:
                    if (tEntity.isClockRunning())
                        tEntity.clockStop();
                    else
                        tEntity.clockStart();
                    break;
            }
        }
    }

}