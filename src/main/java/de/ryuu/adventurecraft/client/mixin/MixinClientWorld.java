package de.ryuu.adventurecraft.client.mixin;

import de.ryuu.adventurecraft.client.block.LightBlock;
import de.ryuu.adventurecraft.client.registry.LightBlocks;
import de.ryuu.adventurecraft.client.registry.LightParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
@Environment(EnvType.CLIENT)
public abstract class MixinClientWorld extends World {

    @Shadow
    @Final
    private List<AbstractClientPlayerEntity> players;

    @Shadow
    @Final
    private MinecraftClient client;

    protected MixinClientWorld(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey,
                               RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    @Inject(method = "randomBlockDisplayTick", at = @At("RETURN"))
    private void randomBlockDisplayTick(int xCenter, int yCenter, int zCenter, int radius, Random random, boolean spawnBarrierParticles, Mutable pos, CallbackInfo ci) {
        int i = xCenter + this.random.nextInt(radius) - this.random.nextInt(radius);
        int j = yCenter + this.random.nextInt(radius) - this.random.nextInt(radius);
        int k = zCenter + this.random.nextInt(radius) - this.random.nextInt(radius);
        pos.set(i, j, k);
        BlockState blockState = this.getBlockState(pos);
        assert this.client.player != null;
        this.client.player.getItemsHand().forEach(itemStack -> {
            if (itemStack.isItemEqual(LightBlocks.blockLight_0.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_1.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_2.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_3.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_4.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_5.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_6.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_7.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_8.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_9.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_10.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_11.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_12.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_13.asItem().getStackForRender()) ||
                    itemStack.isItemEqual(LightBlocks.blockLight_14.asItem().getStackForRender()) || itemStack.isItemEqual(LightBlocks.blockLight_15.asItem().getStackForRender())) {
                if (blockState.getBlock() == LightBlocks.blockLight_0 || blockState.getBlock() == LightBlocks.blockLight_1 ||
                        blockState.getBlock() == LightBlocks.blockLight_2 || blockState.getBlock() == LightBlocks.blockLight_3 ||
                        blockState.getBlock() == LightBlocks.blockLight_4 || blockState.getBlock() == LightBlocks.blockLight_5 ||
                        blockState.getBlock() == LightBlocks.blockLight_6 || blockState.getBlock() == LightBlocks.blockLight_7 ||
                        blockState.getBlock() == LightBlocks.blockLight_8 || blockState.getBlock() == LightBlocks.blockLight_9 ||
                        blockState.getBlock() == LightBlocks.blockLight_10 || blockState.getBlock() == LightBlocks.blockLight_11 ||
                        blockState.getBlock() == LightBlocks.blockLight_12 || blockState.getBlock() == LightBlocks.blockLight_13 ||
                        blockState.getBlock() == LightBlocks.blockLight_14 || blockState.getBlock() == LightBlocks.blockLight_15) {
                    switch (blockState.get(LightBlock.lightLevel)) {
                        case 0:
                            this.addParticle(LightParticles.lightParticle_0, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 1:
                            this.addParticle(LightParticles.lightParticle_1, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 2:
                            this.addParticle(LightParticles.lightParticle_2, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 3:
                            this.addParticle(LightParticles.lightParticle_3, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 4:
                            this.addParticle(LightParticles.lightParticle_4, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 5:
                            this.addParticle(LightParticles.lightParticle_5, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 6:
                            this.addParticle(LightParticles.lightParticle_6, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 7:
                            this.addParticle(LightParticles.lightParticle_7, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 8:
                            this.addParticle(LightParticles.lightParticle_8, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 9:
                            this.addParticle(LightParticles.lightParticle_9, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 10:
                            this.addParticle(LightParticles.lightParticle_10, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 11:
                            this.addParticle(LightParticles.lightParticle_11, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 12:
                            this.addParticle(LightParticles.lightParticle_12, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 13:
                            this.addParticle(LightParticles.lightParticle_13, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 14:
                            this.addParticle(LightParticles.lightParticle_14, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                        case 15:
                            this.addParticle(LightParticles.lightParticle_15, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, 0.0D, 0.0D, 0.0D);
                            break;
                    }
                }
            }
        });
    }
}