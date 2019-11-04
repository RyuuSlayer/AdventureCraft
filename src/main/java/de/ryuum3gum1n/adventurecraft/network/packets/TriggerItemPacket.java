package de.ryuum3gum1n.adventurecraft.network.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import de.ryuum3gum1n.adventurecraft.items.ACITriggerableItem;

public class TriggerItemPacket implements IMessage {

	UUID uuid;

	public TriggerItemPacket() {
	}

	public TriggerItemPacket(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, uuid.toString());
	}

	public static class Handler implements IMessageHandler<TriggerItemPacket, IMessage> {

		@Override
		public IMessage onMessage(TriggerItemPacket message, MessageContext ctx) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(message.uuid);

			ItemStack item = player.inventory.getCurrentItem();
			if (item != null && item.getItem() instanceof ACITriggerableItem) {
				((ACITriggerableItem) item.getItem()).trigger(player.world, player, item);
			}
			return null;
		}
	}
}