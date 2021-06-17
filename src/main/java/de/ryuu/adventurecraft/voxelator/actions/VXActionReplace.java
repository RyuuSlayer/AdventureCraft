package de.ryuu.adventurecraft.voxelator.actions;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXAction;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import de.ryuu.adventurecraft.voxelator.params.BlockstateBrushParameter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class VXActionReplace extends VXAction {
    private static final BrushParameter[] PARAMS = new BrushParameter[]{
            new BlockstateBrushParameter("state", Blocks.STONE)};

    public static final Voxelator.ActionFactory FACTORY = new Voxelator.ActionFactory() {
        @Override
        public String getName() {
            return "replace";
        }

        @Override
        public VXAction newAction(NBTTagCompound actionData) {
            ItemStack stack = new ItemStack(actionData.getCompoundTag("state"));
            Block block = Block.getBlockFromItem(stack.getItem());
            return new VXActionReplace(block.getStateFromMeta(stack.getMetadata()));
        }

        @Override
        public NBTTagCompound newAction(String[] parameters) {
            NBTTagCompound actionData = new NBTTagCompound();
            actionData.setString("type", getName());
            actionData.setString("state", parameters[0]);
            return actionData;
        }

        @Override
        public BrushParameter[] getParameters() {
            return PARAMS;
        }
    };

    private final IBlockState state;

    public VXActionReplace(IBlockState state) {
        this.state = state;
    }

    @Override
    public void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        fworld.setBlockState(pos, state);
    }
}