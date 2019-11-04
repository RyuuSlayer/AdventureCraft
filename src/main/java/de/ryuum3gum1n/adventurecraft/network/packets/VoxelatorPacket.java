package de.ryuum3gum1n.adventurecraft.network.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.AdventureCraftItems;

public class VoxelatorPacket implements IMessage {

	NBTTagCompound data;
	UUID uuid;

	public VoxelatorPacket() {
	}

	public VoxelatorPacket(UUID uuid, NBTTagCompound tag) {
		data = tag;
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<VoxelatorPacket, IMessage> {

		@Override
		public IMessage onMessage(VoxelatorPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.uuid);
			
			ItemStack item = player.inventory.getCurrentItem();
			if(item.getItem() == AdventureCraftItems.voxelbrush){
				item.getTagCompound().setTag("brush_data", message.data);
			}else AdventureCraft.logger.error("Currently Held Item Is Not A VoxelBrush");
			return null;
		}
	}
}