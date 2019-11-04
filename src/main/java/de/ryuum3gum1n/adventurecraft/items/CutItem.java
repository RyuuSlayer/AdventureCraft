package de.ryuum3gum1n.adventurecraft.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.clipboard.ClipboardItem;
import de.ryuum3gum1n.adventurecraft.server.ServerClipboard;
import de.ryuum3gum1n.adventurecraft.server.ServerHandler;
import de.ryuum3gum1n.adventurecraft.server.ServerMirror;
import de.ryuum3gum1n.adventurecraft.util.WorldHelper;

public class CutItem extends ACItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			rightClickClient(world, player);
		} else {
			rightClickServer(world, player);
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0);
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			rightClickClient(world, player);
		else
			rightClickServer(world, player);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	public static void rightClickServer(World world, EntityPlayer player) {
		ServerMirror mirror = ServerHandler.getServerMirror(null);
		ServerClipboard clipboard = mirror.getClipboard();

		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);
		String keyString = "player."+player.getGameProfile().getId().toString();

		if(bounds == null)
			return;

		ClipboardItem item = ClipboardItem.copyRegion(bounds, world, keyString, player);

		if(item != null) {
			clipboard.put(keyString, item);
			WorldHelper.fill(world, bounds, Blocks.AIR.getDefaultState());
		}
	}

	public static void rightClickClient(World world, EntityPlayer player) {
		int[] bounds = WandItem.getBoundsFromPlayerOrNull(player);

		if(bounds == null)
			return;

		ClipboardItem item = ClipboardItem.copyRegion(bounds, world, "player.self", player);

		if(item != null) {
			AdventureCraft.asClient().setClipboard(item);
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		// cut entity to clipboard!

		// ClipboardItem.copyEntity();

		// by returning TRUE, we prevent damaging the entity being hit.
		return true;
	}

}