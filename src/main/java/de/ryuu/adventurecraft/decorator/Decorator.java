package de.ryuu.adventurecraft.decorator;

import de.ryuu.adventurecraft.decorator.SchematicDecoration.InvalidSchematicException;
import de.ryuu.adventurecraft.decorator.SchematicDecoration.Schematic;
import de.ryuu.adventurecraft.invoke.FileScriptInvoke;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Decorator {

    private static List<StaticDecoration> statics = new ArrayList<StaticDecoration>();
    private static List<Decoration> decorations;

    static {
        statics.add(new GrassDecoration());
        statics.add(new TreeDecoration());
        statics.add(new IceSpikeDecoration());
    }

    public static List<Decoration> getAllDecorations() {
        if (decorations != null)
            return decorations;
        decorations = new ArrayList<Decoration>(statics);
        File mc = FMLCommonHandler.instance().getSavesDirectory().getParentFile();
        File decorator = new File(mc, "decorator");
        if (!decorator.exists() || !decorator.isDirectory())
            return decorations;
        for (File file : decorator.listFiles()) {
            if (file.getName().endsWith(".schematic")) {// Loads a schematic
                try {
                    NBTTagCompound tag = CompressedStreamTools.readCompressed(new FileInputStream(file));
                    decorations
                            .add(new SchematicDecoration(new Schematic(tag), file.getName().replace(".schematic", "")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidSchematicException e) {
                    System.out.println("Found invalid schematic: " + file.getName());
                }
            } else if (file.getName().endsWith(".js")) {// Loads a script
                decorations
                        .add(new JavascriptDecoration(new FileScriptInvoke(file), file.getName().replace(".js", "")));
            }
        }
        return decorations;
    }

    public static Decoration getDecorationFromString(String string) {
        for (Decoration decor : getAllDecorations()) {
            if (decor.name().equals(string))
                return decor;
        }
        return null;
    }

}