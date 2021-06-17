package de.ryuu.adventurecraft.voxelator.actions;

import de.ryuu.adventurecraft.util.MutableBlockPos;
import de.ryuu.adventurecraft.voxelator.BrushParameter;
import de.ryuu.adventurecraft.voxelator.CachedWorldDiff;
import de.ryuu.adventurecraft.voxelator.VXAction;
import de.ryuu.adventurecraft.voxelator.Voxelator;
import de.ryuu.adventurecraft.voxelator.params.ListBrushParameter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class VXActionVariationsReplace extends VXAction {
    private static final BrushParameter[] PARAMS = new BrushParameter[]{
            new ListBrushParameter("variants", 255, BrushParameter.BPType.BLOCKSTATE)};

    public static final Voxelator.ActionFactory FACTORY = new Voxelator.ActionFactory() {
        @Override
        public String getName() {
            return "varreplace";
        }

        @Override
        public VXAction newAction(NBTTagCompound actionData) {
            NBTTagList list = actionData.getTagList("variants", 8);

            int l = list.tagCount();

            IBlockState[] a = new IBlockState[l];

            for (int i = 0; i < l; i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                ItemStack stack = new ItemStack(tag);
                Block block = Block.getBlockFromItem(stack.getItem());
                a[i] = block.getStateFromMeta(stack.getMetadata());
            }

            return new VXActionVariationsReplace(a);
        }

        @Override
        public NBTTagCompound newAction(String[] parameters) {
            NBTTagCompound actionData = new NBTTagCompound();
            actionData.setString("type", getName());

            NBTTagList list = new NBTTagList();
            for (String p : parameters)
                list.appendTag(new NBTTagString(p));

            actionData.setTag("variants", list);
            return actionData;
        }

        @Override
        public BrushParameter[] getParameters() {
            return PARAMS;
        }
    };

    private final IBlockState[] states;
    private final Random random;

    public VXActionVariationsReplace(IBlockState... states) {
        this.states = states;
        this.random = new Random();
    }

    @Override
    public void apply(BlockPos pos, BlockPos center, MutableBlockPos offset, CachedWorldDiff fworld) {
        fworld.setBlockState(pos, states[random.nextInt(states.length)]);
    }
}