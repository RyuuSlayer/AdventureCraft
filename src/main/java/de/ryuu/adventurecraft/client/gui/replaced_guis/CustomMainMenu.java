package de.ryuu.adventurecraft.client.gui.replaced_guis;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.client.gui.misc.CrashQuestion;
import de.ryuu.adventurecraft.client.gui.misc.GuiCertError;
import de.ryuu.adventurecraft.client.gui.replaced_guis.map.MapSelector;
import de.ryuu.adventurecraft.client.gui.replaced_guis.map.download.GuiMapList;
import de.ryuu.adventurecraft.client.gui.replaced_guis.save.SaveSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.util.glu.Project;

import java.io.File;
import java.io.IOException;

/**
 * This is the custom main menu for adventurecraft<br>
 * After it is complete it will also allow you to create/load saves (semilar to
 * AdventureCraft)
 *
 * @author ErdbeerbaerLP
 */
public class CustomMainMenu extends GuiScreen {

    private static final ResourceLocation ADVENTURECRAFT_TITLE_TEXTURES = new ResourceLocation(
            "adventurecraft:textures/adventurecraft-titlelogo.png");
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[]{
            new ResourceLocation("textures/gui/title/background/panorama_0.png"),
            new ResourceLocation("textures/gui/title/background/panorama_1.png"),
            new ResourceLocation("textures/gui/title/background/panorama_2.png"),
            new ResourceLocation("textures/gui/title/background/panorama_3.png"),
            new ResourceLocation("textures/gui/title/background/panorama_4.png"),
            new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private final ResourceLocation backgroundTexture;
    /**
     * Texture allocated for the current viewport of the main menu's panorama
     * background.
     */
    private final DynamicTexture viewportTexture;
    /**
     * Timer used to rotate the panorama, increases every tick.
     */
    private float panoramaTimer;

    public CustomMainMenu() {

        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("background",
                this.viewportTexture);
        AdventureCraft.lastVisitedWorld = null;
    }

    // GuiMainMenu
    @Override
    public void initGui() {
        File savesDir = new File(this.mc.mcDataDir, "saves");
        final String[] savesList = savesDir.list();
        // noinspection ConstantConditions
        for (String s : savesList) {
            if (s.startsWith(".AC"))
                continue;
            s.contains("@SAV");
            mc.displayGuiScreen(new CrashQuestion());
            return;
        }
        int j = this.height / 4 + 48;
        this.addButton(new GuiButtonExt(1, this.width / 2 - 100, j, "Create new Map"));
        this.addButton(new GuiButtonExt(2, this.width / 2 - 100, j + 24, "Play Map"));
        this.addButton(new GuiButtonExt(3, this.width / 2, j + 24 * 2, 100, 20, "Download Maps"));
        this.addButton(new GuiButtonExt(4, this.width / 2, j + 24 * 3, 100, 20, I18n.format("menu.quit")));
        this.addButton(new GuiButtonExt(5, this.width / 2 - 100, j + 24 * 3, 100, 20, I18n.format("menu.options")));
        this.addButton(new GuiButtonExt(6, this.width / 2 - 100, j + 24 * 2, 100, 20, I18n.format("fml.menu.mods")));
        // downloadMapButton.enabled = false;
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        for (int j = 0; j < 3; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float) (j - 1) / 256.0F;
            bufferbuilder.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int mouseX, int mouseY, float partialTicks) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(mouseX, mouseY, partialTicks);
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = 120.0F / (float) (this.width > this.height ? this.width : this.height);
        float f1 = (float) this.height * f / 256.0F;
        float f2 = (float) this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int mouseX, int mouseY, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        for (int j = 0; j < 64; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % 8) / 8.0F - 0.5F) / 64.0F;
            float f1 = ((float) (j / 8) / 8.0F - 0.5F) / 64.0F;
            GlStateManager.translate(f, f1, 0.0F);
            GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-this.panoramaTimer * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                if (k == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                bufferbuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                bufferbuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // TODO Auto-generated method stub
        super.actionPerformed(button);
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(new MapSelector(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new SaveSelector(this));
                break;
            case 3:
                if (Loader.isModLoaded("letsencryptcraft"))
                    mc.displayGuiScreen(new GuiMapList(this));
                else
                    mc.displayGuiScreen(new GuiCertError());
                break;
            case 4:
                this.mc.shutdown();
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 6:
                this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub
        this.panoramaTimer += partialTicks;
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        int j = this.width / 2 - 120;
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(ADVENTURECRAFT_TITLE_TEXTURES);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(j + 17, 30, 0, 0, 212, 48);
        // this.drawTexturedModalRect(j + 155, 30, 0, 44, 155, 44);
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

}
