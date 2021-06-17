package de.ryuu.adventurecraft.network.packets;

import de.ryuu.adventurecraft.network.handlers.client.NPCOpenPacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NPCOpenPacket implements IMessage {

    public int id;

    public NPCOpenPacket() {
    }

    public NPCOpenPacket(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
    }

    public static class Handler implements IMessageHandler<NPCOpenPacket, IMessage> {
        @Override
        public IMessage onMessage(NPCOpenPacket message, MessageContext ctx) {
            NPCOpenPacketHandler.handle(message);
            return null;
        }
    }
}