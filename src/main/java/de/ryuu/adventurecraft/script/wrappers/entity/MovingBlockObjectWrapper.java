package de.ryuu.adventurecraft.script.wrappers.entity;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.entity.EntityMovingBlock;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class MovingBlockObjectWrapper extends EntityObjectWrapper {
    private EntityMovingBlock moving;

    public MovingBlockObjectWrapper(EntityMovingBlock moving) {
        super(moving);
        this.moving = moving;
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

    public void setBlock(String resourcelocation) {
        setBlock(resourcelocation, 0);
    }

    public void setBlock(String resourcelocation, int meta) {
        NBTTagCompound tag = getCurrentData();
        tag.setString("Block", resourcelocation);
        tag.setByte("Data", (byte) meta);
        moving.updateData(tag);
    }

    @Override
    public void setInvisible(boolean bool) {
        setFieldBoolean("invisible", bool);
    }

    public void setPushable(boolean bool) {
        setFieldBoolean("pushable", bool);
    }

    public void setCollision(boolean bool) {
        setFieldBoolean("collision", bool);
    }

    public void setNoGravity(boolean bool) {
        setFieldBoolean("no_gravity", bool);
    }

    public void setMountYOffset(float amount) {
        NBTTagCompound tag = getCurrentData();
        tag.setFloat("mount_y_offset", amount);
        moving.updateData(tag);
    }

    public void setFieldBoolean(String field, boolean bool) {
        NBTTagCompound tag = getCurrentData();
        tag.setBoolean(field, bool);
        moving.updateData(tag);
    }

    private NBTTagCompound getCurrentData() {
        NBTTagCompound tag = new NBTTagCompound();
        moving.writeEntityToNBT(tag);
        return tag;
    }

}