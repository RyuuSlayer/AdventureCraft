package de.ryuu.adventurecraft.items;

import de.ryuu.adventurecraft.client.gui.entity.MovingBlockGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MovingBlockCreator extends ACItem {
    @Override
    @SideOnly(Side.CLIENT)
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side,
                                      float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
                    new MovingBlockGui(pos.getX() + hitX,
                            pos.getY() + hitY, pos.getZ() + hitZ));
        }
        return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    }
}