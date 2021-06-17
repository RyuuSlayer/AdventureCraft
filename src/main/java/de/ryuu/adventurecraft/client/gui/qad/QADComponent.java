package de.ryuu.adventurecraft.client.gui.qad;

import com.google.common.collect.Lists;
import de.ryuu.adventurecraft.client.gui.vcui.VCUIRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;

import java.util.List;

public abstract class QADComponent {
    private String componentName;
    private List<String> tooltip;

    public List<String> getTooltip() {
        return tooltip;
    }

    public QADComponent setTooltip(String... strings) {
        tooltip = Lists.newArrayList(strings);
        return this;
    }

    public QADComponent setTooltip(List<String> strings) {
        tooltip = strings;
        return this;
    }

    public String getName() {
        return this.componentName;
    }

    public QADComponent setName(String name) {
        this.componentName = name;
        return this;
    }

    public abstract QADEnumComponentClass getComponentClass();

    public abstract int getX();

    public abstract void setX(int x);

    public abstract int getY();

    public abstract void setY(int y);

    public abstract void setPosition(int x, int y);

    public abstract void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer);

    public abstract void onMouseClicked(int localMouseX, int localMouseY, int mouseButton);

    public abstract void onMouseReleased(int localMouseX, int localMouseY, int state);

    public abstract void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton,
                                          long timeSinceLastClick);

    public abstract void onKeyTyped(char typedChar, int typedCode);

    public abstract void onTickUpdate();

    public abstract boolean isPointInside(int mouseX, int mouseY);

    public List<String> getTooltip(int mouseX, int mouseY) {
        return isPointInside(mouseX, mouseY) ? tooltip : null;
    }

    public void postDraw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {

    }

    public void playPressSound(float pitch) {
        // TODO: Extract the playing of sounds from this class into a QADSoundHandler
        // class.
        // That should be done so in the future custom gui screens can have custom
        // sound.
        SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, pitch));
    }

    public boolean focusInput() {
        return false;
    }

    public abstract boolean transferFocus();

    public abstract boolean isFocused();

    public abstract void removeFocus();

    public void handleMouseInput() {

    }

}