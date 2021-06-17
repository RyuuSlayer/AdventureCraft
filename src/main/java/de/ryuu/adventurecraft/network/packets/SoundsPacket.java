package de.ryuu.adventurecraft.network.packets;

import de.ryuu.adventurecraft.AdventureCraftSounds;
import de.ryuu.adventurecraft.network.handlers.client.SoundsPacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoundsPacket implements IMessage {

    public boolean mute;
    public boolean repeat;
    public boolean constant;
    public int delay;
    public AdventureCraftSounds.SoundEnum sound;

    public SoundsPacket() {
        mute = true;
    }

    public SoundsPacket(AdventureCraftSounds.SoundEnum sound, boolean repeat, int delay, boolean constant) {
        mute = false;
        this.sound = sound;
        this.repeat = repeat;
        this.delay = delay;
        this.constant = constant;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (!(mute = buf.readBoolean())) {
            sound = AdventureCraftSounds.SoundEnum.values()[buf.readInt()];
            repeat = buf.readBoolean();
            delay = buf.readInt();
            constant = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(mute);
        if (!mute) {
            buf.writeInt(sound.ordinal());
            buf.writeBoolean(repeat);
            buf.writeInt(delay);
            buf.writeBoolean(constant);
        }
    }

    public static class Handler implements IMessageHandler<SoundsPacket, IMessage> {

        @Override
        public IMessage onMessage(SoundsPacket message, MessageContext ctx) {
            SoundsPacketHandler.handle(message);
            return null;
        }
    }
}