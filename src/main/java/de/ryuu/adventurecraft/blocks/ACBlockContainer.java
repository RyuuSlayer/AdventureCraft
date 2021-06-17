package de.ryuu.adventurecraft.blocks;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.AdventureCraftTabs;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ACBlockContainer extends BlockContainer {

    protected ACBlockContainer() {
        super(ACAdminiumMaterial.instance);
        setDefaultState(getBlockState().getBaseState());
        setCreativeTab(AdventureCraftTabs.tab_AdventureCraftTab);
        setBlockUnbreakable();
        setResistance(6000001.0F);
        setSoundType(SoundType.STONE);
        setTickRandomly(false);
        setLightOpacity(0);
        disableStats();
        translucent = true;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        world.removeTileEntity(pos);
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Deprecated
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Deprecated
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Deprecated
    @Override
    @SideOnly(Side.CLIENT)
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        return AdventureCraft.proxy.isBuildMode() ? super.collisionRayTrace(state, world, pos, start, end) : null;
    }

    @Deprecated
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Deprecated
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (AdventureCraft.proxy.isBuildMode())
            return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1,
                    pos.getZ() + 1);
        else
            return null;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Deprecated
    @Override
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return 1.0F;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

}