package de.ryuu.adventurecraft.client.block;

import de.ryuu.adventurecraft.client.registry.LightBlocks;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LightBlock extends BarrierBlock {

    public static final IntProperty lightLevel = IntProperty.of("light", 0, 15);

    public LightBlock(Settings settings, int defaultLight) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(lightLevel, defaultLight));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context.isHolding(LightBlocks.blockLight_0.asItem()) || context.isHolding(LightBlocks.blockLight_1.asItem()) ||
                context.isHolding(LightBlocks.blockLight_2.asItem()) || context.isHolding(LightBlocks.blockLight_3.asItem()) ||
                context.isHolding(LightBlocks.blockLight_4.asItem()) || context.isHolding(LightBlocks.blockLight_5.asItem()) ||
                context.isHolding(LightBlocks.blockLight_6.asItem()) || context.isHolding(LightBlocks.blockLight_7.asItem()) ||
                context.isHolding(LightBlocks.blockLight_8.asItem()) || context.isHolding(LightBlocks.blockLight_9.asItem()) ||
                context.isHolding(LightBlocks.blockLight_10.asItem()) || context.isHolding(LightBlocks.blockLight_11.asItem()) ||
                context.isHolding(LightBlocks.blockLight_12.asItem()) || context.isHolding(LightBlocks.blockLight_13.asItem()) ||
                context.isHolding(LightBlocks.blockLight_14.asItem()) || context.isHolding(LightBlocks.blockLight_15.asItem())) {
            return super.getOutlineShape(state, world, pos, context);
        } else {
            return VoxelShapes.empty();
        }
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(lightLevel);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(lightLevel) + 1 == 16) {
            world.setBlockState(pos, state.with(lightLevel, 0));
        } else {
            world.setBlockState(pos, state.with(lightLevel, state.get(lightLevel) + 1));
        }
        return ActionResult.SUCCESS;
    }
}