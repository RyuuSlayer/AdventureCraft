package de.ryuu.adventurecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {

    private static HashMap<GuiListWorldSelectionEntry, File> worldfolder = new HashMap<GuiListWorldSelectionEntry, File>();
    private static Map<World, List<IEventListener>> eventlisteners = new HashMap<World, List<IEventListener>>();
    private static Map<GuiListWorldSelectionEntry, WorldSummary> worldSummaries = new HashMap<GuiListWorldSelectionEntry, WorldSummary>();

    public static File getWorldFolderFromSelection(GuiListWorldSelectionEntry entry) {
        if (worldfolder.containsKey(entry)) {
            return worldfolder.get(entry);
        }
        try {
            File image = null;
            for (Field field : GuiListWorldSelectionEntry.class.getDeclaredFields()) {
                if (field.getType() == File.class) {
                    field.setAccessible(true);
                    image = (File) field.get(entry);
                    break;
                }
            }
            if (image == null)
                return null;
            File folder = image.getParentFile();
            worldfolder.put(entry, folder);
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<IEventListener> getEventListeners(World world) {
        if (eventlisteners.containsKey(world))
            return eventlisteners.get(world);
        try {
            List<IEventListener> list = null;
            for (Field field : World.class.getDeclaredFields()) {
                if (field.getType() == World.class) {
                    field.setAccessible(true);
                    list = (List<IEventListener>) field.get(world);
                    break;
                }
            }

            eventlisteners.put(world, list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WorldSummary getWorldSummary(GuiListWorldSelectionEntry world) {
        if (worldSummaries.containsKey(world))
            return worldSummaries.get(world);
        try {
            WorldSummary summary = null;
            for (Field field : GuiListWorldSelectionEntry.class.getDeclaredFields()) {
                if (field.getType() == WorldSummary.class) {
                    field.setAccessible(true);
                    summary = (WorldSummary) field.get(world);
                    break;
                }
            }
            worldSummaries.put(world, summary);
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void replaceMusicTicker() {
        Minecraft mc = Minecraft.getMinecraft();
        try {
            Field[] fields = Minecraft.class.getDeclaredFields();
            Field musicTickerF = null;
            for (Field f : fields) {
                if (f.getType() == MusicTicker.class) {
                    musicTickerF = f;
                    break;
                }
            }
            musicTickerF.setAccessible(true);
            musicTickerF.set(mc, new ACMusicTicker(mc));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}