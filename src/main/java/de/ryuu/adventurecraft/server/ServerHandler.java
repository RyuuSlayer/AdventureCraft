package de.ryuu.adventurecraft.server;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.blocks.ACIBlockCommandReceiver;
import de.ryuu.adventurecraft.invoke.EnumTriggerState;
import de.ryuu.adventurecraft.invoke.IInvoke;
import de.ryuu.adventurecraft.invoke.IInvokeSource;
import de.ryuu.adventurecraft.invoke.Invoke;
import de.ryuu.adventurecraft.network.packets.PlayerNBTDataMergePacket;
import de.ryuu.adventurecraft.network.packets.StringNBTCommandPacket;
import de.ryuu.adventurecraft.util.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ServerHandler {

    private static final HashMap<MinecraftServer, ServerMirror> serverMirrorsMap = new HashMap<>();

    public static void handleEntityJoin(World world, Entity entity) {
        // If this is a player, send the player the persistent EntityData.
        if (entity instanceof EntityPlayerMP) {
            AdventureCraft.network.sendTo(new PlayerNBTDataMergePacket(entity.getEntityData()),
                    (EntityPlayerMP) entity);
            // PlayerList.playerJoin((EntityPlayerMP)entity);
        }
    }

    public static void handleSNBTCommand(NetHandlerPlayServer serverHandler, StringNBTCommandPacket message) {
        EntityPlayerMP playerEntity = serverHandler.player;
        WorldServer worldServer = (WorldServer) playerEntity.getEntityWorld();
        handleSNBTCommand(playerEntity, worldServer, message);
    }

    /**
     * This method actually handles the SNBT-command.
     **/
    private static void handleSNBTCommand(EntityPlayerMP player, World world, StringNBTCommandPacket message) {
        if (world.isRemote) {
            AdventureCraft.logger.error("FATAL ERROR: ServerHandler method was called on client-side!");
            return;
        }

        if (message.command.equals("server.client.connection.state.change:join_acknowledged")) {
            AdventureCraft.logger.info("join acknowledged : " + message.data);
            getServerMirror(null).playerList().getPlayer(player).construct(message.data);
            return;
        }

        if (message.command.equals("server.client.settings.update")) {
            AdventureCraft.logger.info("updating settings " + message.data);
            getServerMirror(null).playerList().getPlayer(player).updateSettings(message.data);
            return;
        }

        if (message.command.equals("server.data.entity.merge")) {
            if (!PlayerHelper.isOp(player)) {
                player.sendMessage(
                        new TextComponentString("Error: 'server.data.entity.merge' is a operator only command."));
                return;
            }

            String uuidStr = message.data.getString("entityUUID");
            UUID uuid = UUID.fromString(uuidStr);

            Entity theEntity = null;

            for (Entity entityObject : world.loadedEntityList) {
                Entity entity = entityObject;

                if (entity.getUniqueID().equals(uuid)) {
                    theEntity = entity;
                    break;
                }
            }

            if (theEntity == null) {
                player.sendMessage(new TextComponentString("Error: Entity not found. (Possibly dead)"));
                return;
            }

            // Clean data
            NBTTagCompound entityData = new NBTTagCompound();
            NBTTagCompound mergeData = message.data.getCompoundTag("entityData");

            mergeData.removeTag("UUIDMost");
            mergeData.removeTag("UUIDLeast");
            mergeData.removeTag("Dimension");
            mergeData.removeTag("Pos");

            theEntity.writeToNBT(entityData);
            entityData.merge(mergeData);
            theEntity.readFromNBT(entityData);

            if (entityData.hasKey("AC_Width"))
                theEntity.width = entityData.getFloat("AC_Width");
            if (entityData.hasKey("AC_Height"))
                theEntity.height = entityData.getFloat("AC_Height");
            if (entityData.hasKey("AC_StepHeight"))
                theEntity.stepHeight = entityData.getFloat("AC_StepHeight");
            if (entityData.hasKey("AC_NoClip"))
                theEntity.noClip = entityData.getBoolean("AC_NoClip");

            // Done!
            return;
        }

        if (message.command.startsWith("server.data.block.merge:")) {
            if (!PlayerHelper.isOp(player)) {
                player.sendMessage(new TextComponentString("Error: 'blockdatamerge' is a operator only command."));
                return;
            }

            String positionString = message.command.substring(24);
            String[] posStrings = positionString.split(" ");
            BlockPos position = new BlockPos(Integer.parseInt(posStrings[0]), Integer.parseInt(posStrings[1]),
                    Integer.parseInt(posStrings[2]));

            TileEntity entity = world.getTileEntity(position);

            if (entity != null) {
                if (message.data.hasKey("command") && message.data.getString("command").equals("trigger")) {
                    if (entity instanceof IInvokeSource) {
                        IInvokeSource iis = (IInvokeSource) entity;
                        List<IInvoke> invokes = new ArrayList<IInvoke>();
                        iis.getInvokes(invokes);
                        for (IInvoke in : invokes) {
                            Invoke.invoke(in, iis, null, EnumTriggerState.ON);
                        }
                    }
                    return;
                } else {
                    AdventureCraft.logger.info("(datamerge) " + position + " -> " + message.data);
                    mergeTileEntityData(entity, message.data);
                    return;
                }
            } else {
                player.sendMessage(
                        new TextComponentString("Error: Failed to merge block data: TileEntity does not exist."));
                return;
            }
        }

        if (message.command.startsWith("server.data.block.command:")) {
            if (!PlayerHelper.isOp(player)) {
                player.sendMessage(new TextComponentString("Error: 'blockcommand' is a operator only command."));
                return;
            }

            String positionString = message.command.substring(26);
            String[] posStrings = positionString.split(" ");
            BlockPos position = new BlockPos(Integer.parseInt(posStrings[0]), Integer.parseInt(posStrings[1]),
                    Integer.parseInt(posStrings[2]));

            TileEntity entity = world.getTileEntity(position);

            if (entity != null) {
                // AdventureCraft.logger.info("(block command) " + position + " -> " +
                // commandPacket.data);

                if (entity instanceof ACIBlockCommandReceiver) {
                    ((ACIBlockCommandReceiver) entity).commandReceived(message.data.getString("command"), message.data);
                    return;
                }
            } else {
                player.sendMessage(
                        new TextComponentString("Error: Failed to run block-command: TileEntity does not exist."));
                return;
            }
        }

        AdventureCraft.logger
                .error("Received unknown StringNBTCommand from client: " + message.command + " : " + message.data);
    }

    /**
     * Merges the given {@link NBTTagCompound} into the given {@link TileEntity}
     * data.
     **/
    private static void mergeTileEntityData(TileEntity entity, NBTTagCompound data) {
        BlockPos pos = entity.getPos();
        World world = entity.getWorld();
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBT(compound);

        compound.merge(data);
        compound.setInteger("x", pos.getX());
        compound.setInteger("y", pos.getY());
        compound.setInteger("z", pos.getZ());

        entity.readFromNBT(compound);
        entity.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0); // TODO Confirm
    }

    public static ServerMirror getServerMirror(MinecraftServer server) {
        if (server == null) {
            server = FMLCommonHandler.instance().getMinecraftServerInstance();
        }

        ServerMirror mirror = serverMirrorsMap.get(server);

        if (mirror == null) {
            mirror = new ServerMirror();
            mirror.create(server);
            serverMirrorsMap.put(server, mirror);
        }

        return mirror;
    }

    public static void destroyServerMirror(MinecraftServer server) {
        if (server == null) {
            for (ServerMirror mirror : serverMirrorsMap.values()) {
                mirror.destroy();
            }
            serverMirrorsMap.clear();
            return;
        }

        ServerMirror mirror = serverMirrorsMap.remove(server);

        if (mirror != null) {
            mirror.destroy();
        }
    }

}