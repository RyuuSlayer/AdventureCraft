package de.ryuu.adventurecraft.voxelator.shapes;

import de.ryuu.adventurecraft.util.BlockRegion;
import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXShape;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import de.ryuu.adventurecraft.voxelator.params.BooleanBrushParameter;
import de.ryuu.adventurecraft.voxelator.params.FloatBrushParameter;
import de.ryuu.adventurecraft.voxelator.params.IntegerBrushParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class VXShapeCylinder extends VXShape {
    private static final BrushParameter[] PARAMS = new BrushParameter[]{new FloatBrushParameter("radius", 1, 64, 5),
            new IntegerBrushParameter("height", 0, 64, 5), new BooleanBrushParameter("hollow", false)};

    public static Voxelator.ShapeFactory FACTORY = new Voxelator.ShapeFactory() {
        @Override
        public String getName() {
            return "cylinder";
        }

        @Override
        public VXShape newShape(NBTTagCompound shapeData, BlockPos origin) {
            int px = shapeData.getInteger("position.x") + origin.getX();
            int py = shapeData.getInteger("position.y") + origin.getY();
            int pz = shapeData.getInteger("position.z") + origin.getZ();

            float r = shapeData.getFloat("radius");
            int h = shapeData.getInteger("height");
            boolean hollow = shapeData.getBoolean("hollow");

            return new VXShapeCylinder(new BlockPos(px, py, pz), r, h, hollow);
        }

        @Override
        public NBTTagCompound newShape(String[] parameters) {
            if (parameters.length == 1) {
                NBTTagCompound shapeData = new NBTTagCompound();
                shapeData.setString("type", getName());
                shapeData.setFloat("radius", Float.parseFloat(parameters[0]));
                shapeData.setInteger("height", Integer.parseInt(parameters[1]));
                shapeData.setBoolean("hollow", Boolean.parseBoolean(parameters[2]));
                return shapeData;
            }

            return null;
        }

        @Override
        public BrushParameter[] getParameters() {
            return PARAMS;
        }
    };

    private final BlockPos position;
    private final float radius;
    private final float radiusSquared;
    private final int height;
    private final boolean hollow;

    public VXShapeCylinder(BlockPos position, float radius, int height, boolean hollow) {
        this.position = position;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.height = height;
        this.hollow = hollow;
    }

    @Override
    public BlockPos getCenter() {
        return position;
    }

    @Override
    public BlockRegion getRegion() {
        int sx = position.getX();
        int sy = position.getY();
        int sz = position.getZ();
        int r = (int) (radius + 0.5d);
        return new BlockRegion(sx - r + 1, sy, sz - r + 1, sx + r - 1, sy + height - 1, sz + r - 1);
    }

    @Override
    public boolean test(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int sx = position.getX();
        int sy = position.getY();
        int sz = position.getZ();
        int dx = x - sx;
        int dy = y - sy;
        int dz = z - sz;
        dy = dy < 0 ? -dy : dy;
        // TODO: 'new VXShapeCylinder' is a waste of memory and should be removed
        // somehow.
        return dy < height && dx * dx + dz * dz <= radiusSquared && (hollow
                ? !(new VXShapeCylinder(position, radius - 1, height - 1, false).test(pos, center, offset, fworld))
                : true);
    }

}