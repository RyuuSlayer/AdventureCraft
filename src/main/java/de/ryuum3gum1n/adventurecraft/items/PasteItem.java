package de.ryuum3gum1n.adventurecraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.clipboard.ClipboardItem;
import de.ryuum3gum1n.adventurecraft.clipboard.ClipboardTagNames;
import de.ryuum3gum1n.adventurecraft.server.ServerHandler;
import de.ryuum3gum1n.adventurecraft.server.ServerMirror;
import de.ryuum3gum1n.adventurecraft.util.BlockRegion;
import de.ryuum3gum1n.adventurecraft.util.UndoRegion;
import de.ryuum3gum1n.adventurecraft.util.UndoTask;

public class PasteItem extends ACItem {

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		onItemRightClick(world, player, hand);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		NBTTagCompound settings = AdventureCraft.getSettings(player);

		float lenMul = settings.getInteger("item.paste.reach");
		Vec3d plantPos = player.getLook(1);
		plantPos = new Vec3d(plantPos.x * lenMul, plantPos.y * lenMul, plantPos.z * lenMul)
				.add(new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ));

		// PLANT!
		String keyString = "player." + player.getGameProfile().getId().toString();
		ClipboardItem item = ServerHandler.getServerMirror(null).getClipboard().get(keyString);

		if (item != null) {

			if (item.getData().hasKey(ClipboardTagNames.$OFFSET, item.getData().getId())) {
				NBTTagCompound offset = item.getData().getCompoundTag(ClipboardTagNames.$OFFSET);
				plantPos = new Vec3d(plantPos.x + offset.getFloat("x"), plantPos.y + offset.getFloat("y"),
						plantPos.z + offset.getFloat("z"));
			}

			float snap = ServerMirror.instance().playerList().getPlayer((EntityPlayerMP) player).settings
					.getInteger("item.paste.snap");
			if (snap > 1) {
				plantPos = new Vec3d(Math.floor(plantPos.x / snap) * snap, Math.floor(plantPos.y / snap) * snap,
						Math.floor(plantPos.z / snap) * snap);
			}

			if (item.getData().hasKey(ClipboardTagNames.$REGION)) {
				NBTTagCompound regionTag = item.getData().getCompoundTag(ClipboardTagNames.$REGION);
				int width = regionTag.getInteger(ClipboardTagNames.$REGION_WIDTH);
				int height = regionTag.getInteger(ClipboardTagNames.$REGION_HEIGHT);
				int length = regionTag.getInteger(ClipboardTagNames.$REGION_LENGTH);
				BlockPos pos = new BlockPos(plantPos);
				System.out.println("pos: " + pos + "; width: " + width + "; height: " + height + "; length: " + length);
				UndoRegion before = new UndoRegion(new BlockRegion(pos, width, height, length), world);
				ClipboardItem.pasteRegion(item, pos, world, player);
				UndoRegion after = new UndoRegion(new BlockRegion(pos, width, height, length), world);
				UndoTask.TASKS.add(new UndoTask(before, after, "Paste", player.getName()));
			}

			if (item.getData().hasKey(ClipboardTagNames.$ENTITY)) {
				ClipboardItem.pasteEntity(item, plantPos, world, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}