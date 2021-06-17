package de.ryuu.adventurecraft.client;

import de.ryuu.adventurecraft.AdventureCraftBlocks;
import de.ryuu.adventurecraft.AdventureCraftItems;
import de.ryuu.adventurecraft.client.entity.PointEntityRenderer;
import de.ryuu.adventurecraft.client.entity.RenderNPC;
import de.ryuu.adventurecraft.client.environment.Environments;
import de.ryuu.adventurecraft.client.render.IRenderable;
import de.ryuu.adventurecraft.client.render.ITemporaryRenderable;
import de.ryuu.adventurecraft.client.render.RenderModeHelper;
import de.ryuu.adventurecraft.client.render.renderables.SelectionBoxRenderer;
import de.ryuu.adventurecraft.client.render.renderers.CustomSkyRenderer;
import de.ryuu.adventurecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.ryuu.adventurecraft.client.render.specialrender.LockedDoorRenderer;
import de.ryuu.adventurecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.ryuu.adventurecraft.client.render.tileentity.ImageHologramBlockTileEntityEXTRenderer;
import de.ryuu.adventurecraft.client.render.tileentity.StorageBlockTileEntityEXTRenderer;
import de.ryuu.adventurecraft.client.render.tileentity.SummonBlockTileEntityEXTRenderer;
import de.ryuu.adventurecraft.entity.EntityMovingBlock;
import de.ryuu.adventurecraft.entity.EntityPoint;
import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import de.ryuu.adventurecraft.entity.projectile.*;
import de.ryuu.adventurecraft.proxy.ClientProxy;
import de.ryuu.adventurecraft.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

@EventBusSubscriber
public class ClientRenderer {
    public static ClientFadeEffect fadeEffect = null;
    private final ClientProxy proxy;
    private final Minecraft mc;
    private final ConcurrentLinkedDeque<ITemporaryRenderable> temporaryRenderers;
    private final ConcurrentLinkedDeque<IRenderable> staticRenderers;
    private VisualMode visualizationMode;
    private float partialTicks;

    public ClientRenderer(ClientProxy clientProxy) {
        proxy = clientProxy;
        mc = Minecraft.getMinecraft();

        visualizationMode = VisualMode.Default;
        partialTicks = 1f;

        temporaryRenderers = new ConcurrentLinkedDeque<>();
        staticRenderers = new ConcurrentLinkedDeque<>();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        init_render_item();
        init_render_block();
        init_render_tilentity();
    }

    private static void init_render_tilentity() {
        ClientRegistry.bindTileEntitySpecialRenderer(ClockBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/timer.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(RedstoneTriggerBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/redstoneTrigger.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(RelayBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/relay.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(ScriptBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/script.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(BlockUpdateDetectorTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/bud.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(StorageBlockTileEntity.class, new GenericTileEntityRenderer<>(
                "adventurecraft:textures/blocks/util/storage.png", new StorageBlockTileEntityEXTRenderer()));

        ClientRegistry.bindTileEntitySpecialRenderer(EmitterBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/emitter.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(ImageHologramBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/texture.png",
                        new ImageHologramBlockTileEntityEXTRenderer()));

        ClientRegistry.bindTileEntitySpecialRenderer(CollisionTriggerBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/trigger.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(LightBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/light.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(MessageBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/message.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(InverterBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/inverter.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(MemoryBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/memory.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(TriggerFilterBlockTileEntity.class,
                new GenericTileEntityRenderer<>(
                        "adventurecraft:textures/blocks/util/filter.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(DelayBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/delay.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(URLBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/url.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(SummonBlockTileEntity.class, new GenericTileEntityRenderer<>(
                "adventurecraft:textures/blocks/util/spawner.png", new SummonBlockTileEntityEXTRenderer()));

        ClientRegistry.bindTileEntitySpecialRenderer(LockedDoorTileEntity.class, new LockedDoorRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(URLBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/url.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(MusicBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/music.png"));

        ClientRegistry.bindTileEntitySpecialRenderer(CameraBlockTileEntity.class,
                new GenericTileEntityRenderer<>("adventurecraft:textures/blocks/util/camera.png"));
    }

    private static void init_render_item() {
        for (Item item : AdventureCraftItems.ALL_AC_ITEMS) {
            if (!(item instanceof ItemBlock))
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                        "adventurecraft:" + item.getUnlocalizedName().replace("item.", ""), "inventory"));
        }
    }

    private static void init_render_block() {
        for (String name : AdventureCraftBlocks.blocksMap.keySet()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AdventureCraftBlocks.blocksMap.get(name)),
                    0, new ModelResourceLocation("adventurecraft:" + name, "inventory"));
        }

        // killblock (why?!)
        for (int i = 0; i < 7; i++)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AdventureCraftBlocks.killBlock), i,
                    new ModelResourceLocation("adventurecraft:killblock", "inventory"));

        // Locked Door Block
        for (int i = 0; i < 8; i++)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AdventureCraftBlocks.lockedDoorBlock), i,
                    new ModelResourceLocation("adventurecraft:lockeddoorblock", "inventory"));

        for (int i = 0; i < 12; i++)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(AdventureCraftBlocks.spikeBlock), i,
                    new ModelResourceLocation("adventurecraft:spikeblock", "inventory"));

    }

    @SuppressWarnings("unchecked")
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPoint.class, PointEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, new EntityBullet.EntityBulletRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new EntityBomb.EntityBombRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC.NPCRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityBombArrow.class, new EntityBombArrow.EntityBombArrowRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityBoomerang.class, new EntityBoomerang.EntityBoomerangFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityKnife.class, new EntityKnife.EntityKnifeRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityMovingBlock.class, new EntityMovingBlock.EntityMovingBlockRenderFactory());
    }

    public void addStaticRenderer(SelectionBoxRenderer selectionBoxRenderer) {
        staticRenderers.offer(selectionBoxRenderer);
    }

    /****/
    public void addTemporaryRenderer(ITemporaryRenderable renderable) {
        temporaryRenderers.offer(renderable);
    }

    public void clearTemporaryRenderers() {
        temporaryRenderers.clear();
    }

    public VisualMode getVisualizationMode() {
        return visualizationMode;
    }

    /****/
    public void setVisualizationMode(VisualMode mode) {
        visualizationMode = mode;
    }

    /****/
    public int getTemporablesCount() {
        return temporaryRenderers.size();
    }

    /****/
    public int getStaticCount() {
        return staticRenderers.size();
    }

    /****/
    public float getLastPartialTicks() {
        return partialTicks;
    }

    // some empty space here

    public void on_render_world_post(RenderWorldLastEvent event) {
        RenderModeHelper.DISABLE();
        partialTicks = event.getPartialTicks();

        // Iterate trough all ITemporaryRenderables and remove the ones that can be
        // removed.
        temporaryRenderers.removeIf(ITemporaryRenderable::canRemove);

        // If the world and the player exist, call the worldPostRender-method.
        if (mc.world != null && mc.player != null) {
            GlStateManager.pushMatrix();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();

            on_render_world_post_sub(partialTicks, tessellator, vertexbuffer);

            GlStateManager.popMatrix();
        }

        // Enable textures again, since the GUI-prerender doesn't enable it again by
        // itself.
        GlStateManager.enableTexture2D();
    }

    private void on_render_world_post_sub(double partialTicks, Tessellator tessellator, BufferBuilder vertexbuffer) {

        // Translate into World-Space
        double px = mc.player.prevPosX + (mc.player.posX - mc.player.prevPosX) * partialTicks;
        double py = mc.player.prevPosY + (mc.player.posY - mc.player.prevPosY) * partialTicks;
        double pz = mc.player.prevPosZ + (mc.player.posZ - mc.player.prevPosZ) * partialTicks;
        GL11.glTranslated(-px, -py, -pz);

        GlStateManager.disableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        RenderHelper.enableStandardItemLighting();

        // Render all the renderables
        for (IRenderable renderable : staticRenderers) {
            renderable.render(mc, proxy, tessellator, vertexbuffer, partialTicks);
        }

        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableStandardItemLighting();

        // Render all the temporary renderables
        for (ITemporaryRenderable renderable : temporaryRenderers) {
            renderable.render(mc, proxy, tessellator, vertexbuffer, partialTicks);
        }

        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableStandardItemLighting();

        // Render Item Meta Renderables
        if (mc.player != null) {
            mc.player.getHeldItemMainhand();
            ItemStack stack = mc.player.getHeldItemMainhand();
            Item item = stack.getItem();

            ItemMetaWorldRenderer.tessellator = tessellator;
            ItemMetaWorldRenderer.vertexbuffer = vertexbuffer;
            ItemMetaWorldRenderer.partialTicks = partialTicks;
            ItemMetaWorldRenderer.partialTicksF = (float) partialTicks;
            ItemMetaWorldRenderer.clientProxy = proxy;
            ItemMetaWorldRenderer.world = mc.world;
            ItemMetaWorldRenderer.player = mc.player;
            ItemMetaWorldRenderer.playerPosition = new BlockPos(px, py, pz);
            ItemMetaWorldRenderer.render(item, stack);
        }

        if (mc.player != null) {
            mc.player.getHeldItemOffhand();
            ItemStack stack = mc.player.getHeldItemOffhand();
            Item item = stack.getItem();

            ItemMetaWorldRenderer.tessellator = tessellator;
            ItemMetaWorldRenderer.vertexbuffer = vertexbuffer;
            ItemMetaWorldRenderer.partialTicks = partialTicks;
            ItemMetaWorldRenderer.partialTicksF = (float) partialTicks;
            ItemMetaWorldRenderer.clientProxy = proxy;
            ItemMetaWorldRenderer.world = mc.world;
            ItemMetaWorldRenderer.player = mc.player;
            ItemMetaWorldRenderer.playerPosition = new BlockPos(px, py, pz);
            ItemMetaWorldRenderer.render(item, stack);
        }

        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        RenderHelper.enableStandardItemLighting();
    }

    public void on_world_unload() {
        temporaryRenderers.clear();
        visualizationMode = VisualMode.Default;
    }

    public void on_render_world_terrain_pre(RenderTickEvent revt) {
        if (mc.world != null) {
            // Which VisualMode should we use?
            VisualMode visMode = visualizationMode;

            // Prevent non-creative players from using the visualization modes.
            if (!proxy.isBuildMode()) {
                visMode = VisualMode.Default;
            }

            // this takes care of the CUSTOM SKY RENDERING
            if (mc.world.provider != null) {
                boolean debugSkyActive = visMode != VisualMode.Default;

                if (debugSkyActive) {
                    CustomSkyRenderer.instance.setDebugSky(true);
                    mc.world.provider.setSkyRenderer(CustomSkyRenderer.instance);
                } else if (Environments.isNonDefault()) {
                    CustomSkyRenderer.instance.setDebugSky(false);
                    mc.world.provider.setSkyRenderer(CustomSkyRenderer.instance);
                } else {
                    CustomSkyRenderer.instance.setDebugSky(false);
                    mc.world.provider.setSkyRenderer(null);
                }
            }

            // handle currently active VisualMode
            if (mc.player != null) {
                RenderModeHelper.ENABLE(visMode);
            }
        }
    }

    public void on_render_world_terrain_post(RenderTickEvent revt) {
        if (mc.ingameGUI != null && mc.world != null) {
            if (proxy.getInfoBar().canDisplayInfoBar(mc, proxy)) {
                proxy.getInfoBar().display(mc, mc.world, proxy);

                // XXX: Move this to its own IF
                proxy.getInvokeTracker().display(mc, proxy);
            }
        }
    }

    public void on_render_world_hand_post(RenderHandEvent event) {
        if (fadeEffect != null && mc.ingameGUI != null) {
            fadeEffect.render();

            // Do NOT draw the hand!
            event.setCanceled(true);
        }

        GlStateManager.enableTexture2D();
    }

    public static enum VisualMode {
        Default("default"), Lighting("lighting"), Nightvision("nightvision"), Wireframe("wireframe");

        String name;

        VisualMode(String n) {
            name = n;
        }

        public String getName() {
            return name;
        }

        public VisualMode next() {
            int current = ordinal();
            current++;
            if (current >= values().length)
                current = 0;
            Minecraft.getMinecraft().player.getActivePotionEffects().clear();
            return values()[current];
        }
    }

}