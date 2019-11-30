package de.ryuum3gum1n.adventurecraft.client.gui.qad;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import de.ryuum3gum1n.adventurecraft.AdventureCraft;
import de.ryuum3gum1n.adventurecraft.client.gui.vcui.VCUIRenderer;
import de.ryuum3gum1n.adventurecraft.util.Vec2i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A class that is to be used to simplify GUIScreen creation.
 **/
@SideOnly(Side.CLIENT)
public class QADGuiScreen extends GuiScreen implements QADComponentContainer {
    public static final VCUIRenderer instance = new VCUIRenderer();
    protected GuiScreen returnScreen;
    protected boolean drawCursorLines = true;
    private ArrayList<QADComponent> components;
    private ArrayList<QADComponent> lastUpdateComponents;
    private QADLayoutManager layout;
    private GuiScreen behindScreen;
    private boolean shouldPauseGame;
    private boolean shouldRelayout;
    private boolean shouldDebugRender;
    private boolean initializing;
    /**
     * Used for onResize()
     */
    private int lastWidth, lastHeigth = 0;

    public QADGuiScreen() {
        super.allowUserInput = false;
        shouldPauseGame = true;
        shouldRelayout = true;
        shouldDebugRender = false;
        layout = null;
        components = null;
        behindScreen = null;
        lastUpdateComponents = new ArrayList<>();

    }

    // FOLLOWING ARE UTILITY METHODS!
    public static long parseLong(String string, long original, long min, long max) {
        try {
            long i = 0;

            if (string.startsWith("0x"))
                i = Long.parseLong(string.toLowerCase().substring(2), 16);
            if (string.startsWith("0b"))
                i = Long.parseLong(string.toLowerCase().substring(2), 2);
            else
                i = Long.parseLong(string);

            if (i < min)
                throw new NumberFormatException(i + " is smaller than " + min + ".");
            if (i > max)
                throw new NumberFormatException(i + " is bigger than " + max + ".");

            return i;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return original;
        }
    }

    public static int parseInt(String string, int original, int min, int max) {
        try {
            int i = 0;

            if (string.startsWith("0x"))
                i = Integer.parseInt(string.toLowerCase().substring(2), 16);
            if (string.startsWith("0b"))
                i = Integer.parseInt(string.toLowerCase().substring(2), 2);
            else
                i = Integer.parseInt(string.toLowerCase());

            if (i < min)
                throw new NumberFormatException(i + " is smaller than " + min + ".");
            if (i > max)
                throw new NumberFormatException(i + " is bigger than " + max + ".");

            return i;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return original;
        }
    }

    /* ********************************* **/
    /*                                   **/
    /* Everything following is final and **/
    /* should not be overriden.          **/
    /*                                   **/
    /**
     * ********************************
     **/

    public static float parseFloat(String string, float original, float min, float max) {
        try {
            float f = Float.parseFloat(string);

            if (f < min)
                throw new NumberFormatException(f + " is smaller than " + min + ".");
            if (f > max)
                throw new NumberFormatException(f + " is bigger than " + max + ".");

            return f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return original;
        }
    }

    public void buildGui() {
        // dont do anything, let the extended classes do their thing!
    }

    public void layoutGui() {
        // setup component sizes and layout parameters here.
    }

    public void updateGui() {

    }

    /****/

    @Override
    public final boolean doesGuiPauseGame() {
        return shouldPauseGame;
    }

    public final void setDoesPauseGame(boolean flag) {
        this.shouldPauseGame = flag;
    }

    @Override
    public final void initGui() {
        initializing = true;
        // TaleCraft.logger.info("Gui.init() -> " + this.getClass().getName());
        Keyboard.enableRepeatEvents(true);

        if (components == null) {
            components = Lists.newArrayList();

            try {
                buildGui();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        // XXX-WARNING: Untested. Might lead to crash. Leaving it in for now.
        if (this.behindScreen != null) {
            this.behindScreen.width = this.width;
            this.behindScreen.height = this.height;
            this.behindScreen.initGui();
        }
        onLayout();


        initializing = false;
    }

    /**
     * Called when the screen gets resized<br>Called after onUpdate
     */
    public void onScreenResized() {
    }

    private void onLayout() {
        if (components != null && !components.isEmpty() && lastUpdateComponents != null) {
            layoutGui();
            if (layout != null) {
                Vec2i newSize = new Vec2i();
                layout.layout(this, components, newSize);
            }
        } else {
            shouldRelayout = true;
        }
    }

    @Override
    public void onGuiClosed() {
        // AdventureCraft.logger.info("Gui.close() -> " + this.getClass().getName());
        Keyboard.enableRepeatEvents(false);

        if (behindScreen != null) {
            AdventureCraft.proxy.asClient().sheduleClientTickTask(() -> mc.displayGuiScreen(behindScreen));
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (QADComponent component : lastUpdateComponents) {

            if (component.focusInput()) {
                component.onMouseClicked(mouseX - component.getX(), mouseY - component.getY(), mouseButton);
                return;
            }
        }
        for (QADComponent component : lastUpdateComponents) {

            component.onMouseClicked(mouseX - component.getX(), mouseY - component.getY(), mouseButton);
        }
    }

    @Override
    protected final void mouseReleased(int mouseX, int mouseY, int state) {
        for (QADComponent component : lastUpdateComponents) {
            if (component.focusInput()) {
                component.onMouseReleased(mouseX - component.getX(), mouseY - component.getY(), state);
                return;
            }
        }
        for (QADComponent component : lastUpdateComponents) {

            if (component != null)
                component.onMouseReleased(mouseX - component.getX(), mouseY - component.getY(), state);
        }
    }

    @Override
    protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        for (QADComponent component : lastUpdateComponents) {

            if (component.focusInput()) {
                component.onMouseClickMove(mouseX - component.getX(), mouseY - component.getY(), clickedMouseButton, timeSinceLastClick);
                return;
            }
        }
        for (QADComponent component : lastUpdateComponents) {

            component.onMouseClickMove(mouseX - component.getX(), mouseY - component.getY(), clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    public final void keyTyped(char typedChar, int typedCode) {
        if (typedCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(returnScreen);
            mc.inGameHasFocus = returnScreen == null;
            return;
        }

        if (typedCode == Keyboard.KEY_F1) {
            this.shouldDebugRender ^= true;
            return;
        }

        if (typedCode == Keyboard.KEY_TAB) {
            // TODO: Continue working on the 'transfer focus' thing.
            // Commented out because it doesn't work.
            // transferFocus();
            return;
        }

        for (QADComponent component : lastUpdateComponents) {
            component.onKeyTyped(typedChar, typedCode);
        }
    }

    @Override
    public final void updateScreen() {
        if (initializing) return;


        if (lastUpdateComponents == null || components == null) {
            initGui();
            return;
        }

        if (shouldRelayout) {
            onLayout();
            shouldRelayout = false;
        }

        this.updateGui();

        for (QADComponent component : lastUpdateComponents) {
            if (component != null) component.onTickUpdate();
        }
        //Check for lastWidth and lastHeigth changes and run onScreenResized if required
        if (lastHeigth == 0 && lastWidth == 0) {
            lastHeigth = this.height;
            lastWidth = this.width;
        } else if (lastHeigth != this.height || lastWidth != this.width) {
            lastHeigth = this.height;
            lastWidth = this.width;
            onScreenResized();
        }

    }

    /**
     * Draws the screen and all the components in it.<br>
     * Only override to add drawHoveringText below the super.drawScreen call
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (initializing) return;

        // If there is a 'behind' screen, draw it first so it appears in the background.
        if (behindScreen != null) {
            behindScreen.drawScreen(-9999, -9999, partialTicks);
        }

        // ...
        if (this.fontRenderer != null)
            instance.setCurrentScreen(this, this.zLevel, this.fontRenderer, this.itemRender);

        instance.drawDefaultBackground();

        if (components == null)
            lastUpdateComponents = new ArrayList<>();
        else
            lastUpdateComponents = new ArrayList<>(components);

        // Draw all components
        //noinspection ConstantConditions
        if (lastUpdateComponents != null) {
            for (QADComponent component : lastUpdateComponents) {
                if (component != null)
                    component.draw(mouseX - component.getX(), mouseY - component.getY(), partialTicks, instance);
            }
            for (QADComponent component : lastUpdateComponents) {
                if (component != null)
                    component.postDraw(mouseX - component.getX(), mouseY - component.getY(), partialTicks, instance);
            }
        }

        if (shouldDebugRender) {
            QADGuiScreenDebugRenderer.debugRender(this, lastUpdateComponents, instance, mouseX, mouseY, partialTicks);
        }

        drawCustom(mouseX, mouseY, partialTicks, instance);

        // Check for tooltips, and draw them if necessary.
        if (lastUpdateComponents != null) {
            for (QADComponent component : lastUpdateComponents) {
                if (component == null) continue;
                if (component.isPointInside(mouseX, mouseY)) {
                    List<String> text = component.getTooltip(mouseX, mouseY);
                    if (text != null) {
                        int yPos = mouseY;
                        if (text.get(0).equalsIgnoreCase("ylock")) {
                            int add = component instanceof QADRectangularComponent ? ((QADRectangularComponent) component).getHeight() : 20;
                            yPos = component.getY() + add + fontRenderer.FONT_HEIGHT * 2;
                            text = text.subList(1, text.size() - 1);
                        }
                        this.drawHoveringText(text, mouseX, yPos);
                        break;
                    }
                }
            }
        }

        // Debug: Draw cursor position marker.
        if (this.drawCursorLines && Mouse.isInsideWindow()) {
            final int color = 0x1ACCEEFF;
            instance.drawHorizontalLine(0, width, mouseY, color);
            instance.drawVerticalLine(mouseX, -1, height, color);
        }

    }

    public void drawCustom(int mouseX, int mouseY, float partialTicks, VCUIRenderer instance2) {

    }

    public final GuiScreen getBehind() {
        return behindScreen;
    }

    public final void setBehind(GuiScreen behind) {
        behindScreen = behind;
    }

    public final void displayGuiScreen(GuiScreen guiScreen) {
        mc.displayGuiScreen(guiScreen);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCenterX() {
        return width / 2;
    }

    public int getCenterY() {
        return height / 2;
    }

    /*
        If this method is called, the QADGuiScreen
        will rebuild its layout in the next tick.
    */
    public final void relayout() {
        this.shouldRelayout = true;
    }

    @Override
    public <T extends QADComponent> T addComponent(T component) {
        if (components == null)
            components = Lists.newArrayList();
        this.components.add(component);
        return component;
    }

    @Override
    public Collection<QADComponent> getComponents() {
        return components;
    }

    @Override
    public void removeAllComponents() {
        components.clear();
    }

    public void removeComponent(int index) {
        components.remove(index);
    }

    public void removeComponent(QADComponent comp) {
        components.remove(comp);
    }

    public void removeAll(ArrayList<QADComponent> comps) {
        components.removeAll(comps);
    }

    public void resetGuiScreen() {
        removeAllComponents();
        buildGui();
    }

    @Override
    public QADComponent getComponentByName(String name) {
        for (QADComponent component : lastUpdateComponents) {
            if (name.equals(component.getName()))
                return component;
        }
        return null;
    }

    @Override
    public void forceRebuildLayout() {
        onLayout();
    }

    public void forceRebuildAll() {
        initializing = true;
        components = null;
        lastUpdateComponents = null;
        initGui();
    }

    public void setLayoutDirty() {
        shouldRelayout = true;
    }

    @Override
    public boolean isLayoutDirty() {
        // GuiScreen layout is never dirty.
        return false;
    }

    @Override
    public QADLayoutManager getLayout() {
        return layout;
    }

    @Override
    public void setLayout(QADLayoutManager newLayout) {
        layout = newLayout;
        onLayout();
    }

    // Note: This method currently doesn't work correctly.
    public void transferFocus() {
        Iterator<QADComponent> iterator = components.iterator();
        boolean unfocusRest = false;

        while (iterator.hasNext()) {
            QADComponent current = iterator.next();

            if (unfocusRest) {
                current.removeFocus();
                continue;
            }

            if (current.isFocused()) {
                if (current.transferFocus()) {
                    // stay
                } else {
                    // move to next
                    if (iterator.hasNext()) {
                        iterator.next().transferFocus();
                    } else {
                        components.get(0).transferFocus();
                        return;
                    }
                }
                unfocusRest = true;
            }
        }
    }

    @Override
    public int getContainerWidth() {
        return this.width;
    }

    @Override
    public int getContainerHeight() {
        return this.height;
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (this.mc == null) return;
        super.handleMouseInput();
        for (QADComponent comp : components) {
            comp.handleMouseInput();
        }
    }
}
