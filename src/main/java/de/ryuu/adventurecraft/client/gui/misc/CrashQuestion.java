package de.ryuu.adventurecraft.client.gui.misc;

import de.ryuu.adventurecraft.client.gui.replaced_guis.CustomMainMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CrashQuestion extends GuiScreen {
    @Override
    public void initGui() {
        // TODO Auto-generated method stub
        addButton(new GuiButtonExt(0, width / 2 - 200, (height / 4) * 3, "Reload World"));
        addButton(new GuiButtonExt(1, width / 2, (height / 4) * 3,
                TextFormatting.RED + "" + TextFormatting.BOLD + "Delete Folder(s)"));
        addButton(new GuiButtonExt(2, width / 2 - 100, (height / 4) * 3 + 20, "Update"));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // TODO Auto-generated method stub

        this.drawBackground(0);
        drawCenteredString(fontRenderer, TextFormatting.UNDERLINE + "Crash?", width / 2, 30, 16777215);
        drawCenteredString(fontRenderer, "There is still a temporary world in your saves folder...", width / 2, 50,
                16777215);
        drawCenteredString(fontRenderer, "If it was an crash you could attempt to reload the world", width / 2, 60,
                16777215);
        drawCenteredString(fontRenderer, "If not it may be just not deleted properly, try joining and leaving it again",
                width / 2, 70, 16777215);
        drawCenteredString(fontRenderer, "By clicking \"Delete Folder(s)\" you remove all folders ", width / 2, 90,
                16777215);
        drawCenteredString(fontRenderer, "from the saves folder if they are not created by AdventureCraft", width / 2,
                100, 16777215);
        drawCenteredString(fontRenderer, "If you did just update, click the Update button!", width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // TODO Auto-generated method stub

        File savesDir = new File(this.mc.mcDataDir, "saves");
        final String[] savesList = savesDir.list();
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                return;
            case 1:
                // noinspection ConstantConditions
                for (String s : savesList) {
                    if (s.startsWith(".AC"))
                        continue;
                    FileUtils.deleteDirectory(new File(savesDir, s));
                }
                break;
            case 2:
                // Save world back to their folders!
                for (String s : savesList) {
                    if (s.startsWith(".AC"))
                        continue;
                    if (s.equals("AC_TEST")) {
                        FileUtils.deleteDirectory(new File(savesDir, s));
                        continue;
                    }
                    boolean type = !s.contains("@SAV");
                    FileUtils.copyFileToDirectory(new File(savesDir, s),
                            new File(savesDir, type ? ".AC_MAPS" : ".AC_SAVES"));
                    FileUtils.deleteDirectory(new File(savesDir, s));
                }
                break;
        }
        this.mc.displayGuiScreen(new CustomMainMenu());
    }

    @Override
    public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/blocks/tnt_side.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double) this.height, 0.0D)
                .tex(0.0D, (double) ((float) this.height / 32.0F + (float) tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double) this.width, (double) this.height, 0.0D)
                .tex((double) ((float) this.width / 32.0F), (double) ((float) this.height / 32.0F + (float) tint))
                .color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double) this.width, 0.0D, 0.0D).tex((double) ((float) this.width / 32.0F), (double) tint)
                .color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
}