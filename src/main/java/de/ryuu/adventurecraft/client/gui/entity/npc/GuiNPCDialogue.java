package de.ryuu.adventurecraft.client.gui.entity.npc;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.entity.NPC.dialogue.NPCDialogue;
import de.ryuu.adventurecraft.network.packets.NPCDialogueOptionPacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

public class GuiNPCDialogue extends GuiScreen {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(
            "adventurecraft:textures/gui/npcdialogue.png");
    private final UUID npcid;
    private NPCDialogue current;
    private List<NPCDialogue> dialogues;
    private int guiLeft;
    private int guiTop;
    private UUID uuid;

    public GuiNPCDialogue(NPCDialogue current, UUID uuid, List<NPCDialogue> dialogues, UUID npcid) {
        this.current = current;
        this.dialogues = dialogues;
        this.uuid = uuid;
        this.npcid = npcid;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        guiLeft = (this.width - 176) / 2;
        guiTop = (this.height - 166) / 2;
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
        this.fontRenderer.drawString(current.getTitle(),
                (guiLeft + 176 / 2) - this.fontRenderer.getStringWidth(current.getTitle()) / 2, guiTop + 7, 4210752);
        this.fontRenderer.drawSplitString(current.getText(), guiLeft + 5, guiTop + 20, 166 - 10, 0x666666);

        int option_y = guiTop + 156 - current.getOptions().size() * 10;
        this.fontRenderer.drawString("Responses:", guiLeft + 176 / 4, option_y - 10, 0x000077);
        for (NPCDialogue.NPCDialogueOption option : current.getOptions()) {
            int color = 0x777777;
            if (mouseX >= guiLeft + 176 / 3 && mouseY >= option_y) {
                if (mouseX <= guiLeft + 176 / 3 + fontRenderer.getStringWidth(option.option)
                        && mouseY <= option_y + 7) {
                    color = 0x00aa00;
                }
            }
            this.fontRenderer.drawString(option.option, guiLeft + 176 / 3, option_y, color);
            option_y += 10;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        int option_y = guiTop + 156 - current.getOptions().size() * 10;
        for (NPCDialogue.NPCDialogueOption option : current.getOptions()) {
            if (mouseX >= guiLeft + 176 / 3 && mouseY >= option_y) {
                if (mouseX <= guiLeft + 176 / 3 + fontRenderer.getStringWidth(option.option)
                        && mouseY <= option_y + 7) {
                    AdventureCraft.network
                            .sendToServer(new NPCDialogueOptionPacket(uuid, current.getName(), option.option, npcid));
                    String dest = option.destination;
                    if (dest == null)
                        mc.displayGuiScreen(null);
                    else {
                        for (NPCDialogue dlog : dialogues) {
                            if (dlog.getName().equals(dest)) {
                                mc.displayGuiScreen(new GuiNPCDialogue(dlog, uuid, dialogues, npcid));
                                return;
                            }
                        }
                    }
                    return;
                }
            }
            option_y += 10;
        }
    }
}