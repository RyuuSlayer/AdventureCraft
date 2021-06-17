package de.ryuu.adventurecraft.client.gui.misc;

import com.google.common.collect.Lists;
import de.ryuu.adventurecraft.client.gui.qad.QADButton;
import de.ryuu.adventurecraft.client.gui.qad.QADFACTORY;
import de.ryuu.adventurecraft.client.gui.qad.QADGuiScreen;
import de.ryuu.adventurecraft.client.gui.qad.QADScrollPanel;
import de.ryuu.adventurecraft.util.GObjectTypeHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class GuiEntityTypeSelection extends QADGuiScreen {
    private EntityTypeDataLink dataLink;
    private QADScrollPanel panel;
    public GuiEntityTypeSelection(GuiScreen gui, EntityTypeDataLink dataLink) {
        this.setBehind(gui);
        this.returnScreen = gui;
        this.dataLink = dataLink;
    }

    @Override
    public void buildGui() {
        panel = new QADScrollPanel();
        panel.setPosition(0, 0);
        panel.setSize(200, 200);
        this.addComponent(panel);

        final int rowHeight = 12;

        Collection<ResourceLocation> names = GObjectTypeHelper.getEntityNameList();

        // Sort dat list
        {
            List<ResourceLocation> names2 = Lists.newArrayList(names);
            names2.sort(new Comparator<ResourceLocation>() {
                @Override
                public int compare(ResourceLocation o1, ResourceLocation o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            names = names2;
        }

        panel.setViewportHeight(names.size() * rowHeight + 2);
        panel.allowLeftMouseButtonScrolling = true;

        int yOff = 1;
        for (final ResourceLocation typeName : names) {
            QADButton component = QADFACTORY.createButton(typeName.toString(), 2, yOff, width);
            component.simplified = true;
            component.textAlignment = 0;
            component.setHeight(12);
            component.setAction(new Runnable() {
                final String pt = typeName.toString();

                @Override
                public void run() {
                    dataLink.setType(pt);
                    displayGuiScreen(getBehind());
                }
            });

            panel.addComponent(component);
            yOff += rowHeight;
        }
    }

    @Override
    public void layoutGui() {
        panel.setSize(this.width, this.height);
    }

    public static interface EntityTypeDataLink {
        public void setType(String identifier);
    }

}