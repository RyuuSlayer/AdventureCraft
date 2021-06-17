package de.ryuu.adventurecraft.script.wrappers.block;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class BlockStateObjectWrapper implements IObjectWrapper {
    public final IBlockState state;

    public BlockStateObjectWrapper(IBlockState state) {
        this.state = state;
    }

    public String getName() {
        return state.getBlock().getLocalizedName();
    }

    public String getInternalName() {
        return state.getBlock().getUnlocalizedName();
    }

    @Override
    public IBlockState internal() {
        return state;
    }

    @Override
    public boolean equals(Object obj) {
        return state.equals(obj);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    public BlockObjectWrapper getBlock() {
        return new BlockObjectWrapper(state.getBlock());
    }

    public boolean isFullCube() {
        return state.isFullCube();
    }

    public boolean isFullBlock() {
        return state.isFullBlock();
    }

    public boolean isOpaqueCube() {
        return state.isOpaqueCube();
    }

    public boolean isNormalCube() {
        return state.isNormalCube();
    }

    @Override
    public List<String> getOwnPropertyNames() {
        return AdventureCraft.globalScriptManager.getOwnPropertyNames(this);
    }

}