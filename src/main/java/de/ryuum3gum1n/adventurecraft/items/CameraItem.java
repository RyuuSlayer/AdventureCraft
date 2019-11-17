package de.ryuum3gum1n.adventurecraft.items;

import java.util.List;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityStructureRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;
import de.ryuum3gum1n.adventurecraft.client.render.tileentity.ImageHologramBlockTileEntityEXTRenderer;
import de.ryuum3gum1n.adventurecraft.client.render.tileentity.RenderSkull;
import de.ryuum3gum1n.adventurecraft.tileentity.CameraBlockTileEntity;
import de.ryuum3gum1n.adventurecraft.tileentity.CameraBlockTileEntity.CameraPos;
import ibxm.Player;

public class CameraItem extends ACItem {

	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("selected")) {
			player.sendMessage(new TextComponentString(TextFormatting.RED + "No camera block selected!"));
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		} else {
			BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("selected"));
			TileEntity te = world.getTileEntity(pos);
			if (te == null || !(te instanceof CameraBlockTileEntity)) {
				player.sendMessage(new TextComponentString(TextFormatting.RED + "No camera block found!"));
				return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
			} else {
				CameraBlockTileEntity camera = (CameraBlockTileEntity) te;
				CameraPos cpos = camera.addPos(player);
				player.sendMessage(new TextComponentString("Added Camera Pos: " + cpos.toString()));
				//EntityItem ITEM = new EntityItem(Minecraft.getMinecraft().world, cpos.x, cpos.y, cpos.z, new ItemStack(AdventureCraftItems.camera));
				//Minecraft.getMinecraft().getRenderManager().renderEntityStatic(ITEM, 0F, false);
				
				//render(te, x, y, z, partialTicks, destroyStage, partial);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("selected")) {
			BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("selected"));
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			tooltip.add("Selected Camera Block: [x=" + x + ", y=" + y + ", z=" + z + "]");
		}
	}

}