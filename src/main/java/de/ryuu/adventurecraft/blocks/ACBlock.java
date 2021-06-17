package de.ryuu.adventurecraft.blocks;

import de.ryuu.adventurecraft.AdventureCraftTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;

public abstract class ACBlock extends Block {

    protected ACBlock() {
        super(ACAdminiumMaterial.instance);
        setResistance(6000001.0F);
        setBlockUnbreakable();
        setTickRandomly(false);
        setSoundType(SoundType.STONE);
        setCreativeTab(AdventureCraftTabs.tab_AdventureCraftTab);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

}