package de.ryuu.adventurecraft.network.packets;

import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class NPCDataPacket implements IMessage {

    NBTTagCompound data;
    UUID uuid;

    public NPCDataPacket() {
    }

    public NPCDataPacket(UUID uuid, NBTTagCompound tag) {
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

    public static class Handler implements IMessageHandler<NPCDataPacket, IMessage> {

        @Override
        public IMessage onMessage(NPCDataPacket message, MessageContext ctx) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            EntityNPC npc = (EntityNPC) server.getEntityFromUuid(message.uuid);
            npc.setNPCData(message.data);
            return null;
        }
    }
}