package de.ryuu.adventurecraft.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vec3f(final float X, final float Y, final float Z) {
        x = X;
        y = Y;
        z = Z;
    }

    public Vec3f(final Vec3f vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vec3f(final Vec3f a, final Vec3f b) {
        x = a.x + b.x;
        y = a.y + b.y;
        z = a.z + b.z;
    }

    public Vec3f(Vec3d vec) {
        x = (float) vec.x;
        y = (float) vec.y;
        z = (float) vec.z;
    }

    public Vec3f copy() {
        return new Vec3f(x, y, z);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public Vec3f setX(final float X) {
        x = X;
        return this;
    }

    public Vec3f setY(final float Y) {
        y = Y;
        return this;
    }

    public Vec3f setZ(final float Z) {
        z = Z;
        return this;
    }

    public Vec3f set(final float X, final float Y, final float Z) {
        x = X;
        y = Y;
        z = Z;
        return this;
    }

    public Vec3f set(final Vec3f vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public Vec3f multiply(final Vec3f vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
        return this;
    }

    public Vec3f move(final float offset_x, final float offset_y, final float offset_z) {
        x += offset_x;
        y += offset_y;
        z += offset_z;
        return this;
    }

    public Vec3f move(final Vec3f offset) {
        x += offset.x;
        y += offset.y;
        z += offset.z;
        return this;
    }

    public MutableBlockPos getAsBlockPos() {
        return new MutableBlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

}