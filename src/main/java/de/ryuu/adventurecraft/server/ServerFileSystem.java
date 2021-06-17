package de.ryuu.adventurecraft.server;

import de.ryuu.adventurecraft.util.Either;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ServerFileSystem {
    private final File root;

    public ServerFileSystem() {
        root = FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
    }

    public Either<NBTTagList, String> getDirectoryListing(String directoryPath) {
        File directory = getDirectory(directoryPath);
        return getDirectoryListing(directory);
    }

    public Either<NBTTagList, String> getDirectoryListing(File directory) {
        if (directory == null) {
            directory = root;
        }

        if (!directory.isDirectory()) {
            return new Either<NBTTagList, String>(null, "Given node is not a directory.");
        }

        boolean showFiles = !directory.getName().equalsIgnoreCase(".");

        NBTTagList nodesList = new NBTTagList();
        int count = 0;

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            file = sanitize(file);

            if (file == null || (!showFiles && file.isFile())) {
                continue;
            }

            // TODO: Use getFileData() here!

            NBTTagCompound nodeData = new NBTTagCompound();
            nodesList.appendTag(nodeData);

            String name = file.getName();
            nodeData.setString("name", name);

            if (file.isFile()) {
                nodeData.setString("type", "file");
                nodeData.setLong("size", file.length());
            } else if (file.isDirectory()) {
                nodeData.setString("type", "dir");
                nodeData.setString("location", sanitizePath(file.getPath()));
                nodeData.setLong("entries", Objects.requireNonNull(file.list()).length);
            }

            nodeData.setBoolean("flag.e", file.canExecute());
            nodeData.setBoolean("flag.r", file.canRead());
            nodeData.setBoolean("flag.w", file.canWrite());
        }

        return new Either<NBTTagList, String>(nodesList, null);

    }

    /**
     * @return Either a valid java.io.File object or NULL.
     **/
    private File getFileOrDirectory(String path) {
        return sanitize(new File(root, path));
    }

    private File getDirectory(String directoryPath) {
        directoryPath = sanitizePath(directoryPath);

        if (directoryPath.indexOf('.') != -1) {
            return null;
        }

        if (directoryPath.equals("/")) {
            return root;
        }

        return sanitize(new File(root, directoryPath));
    }

    public Either<NBTTagCompound, String> getFileData(String path, boolean withContent) {
        path = sanitizePath(path);

        File file = getFileOrDirectory(path);

        if (file == null)
            return new Either<NBTTagCompound, String>(null, "The given path does not lead anywere.");

        return getFileData(path, file, withContent);
    }

    public Either<NBTTagCompound, String> getFileData(String parent, File file, boolean withContent) {
        file = sanitize(file);

        if (file == null)
            return new Either<NBTTagCompound, String>(null, "Error: No node given.");

        NBTTagCompound nodeData = new NBTTagCompound();

        String name = file.getName();
        nodeData.setString("name", name);

        if (file.isFile()) {
            nodeData.setString("type", "file");
            nodeData.setLong("size", file.length());

            if (withContent) {
                try {
                    String content = FileUtils.readFileToString(file);

                    if (content.length() > 32768) {
                        return new Either<NBTTagCompound, String>(null,
                                "Error: Content too long; " + content.length() + " > 32768");
                    } else {
                        nodeData.setString("content", content);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (file.isDirectory()) {
            nodeData.setString("type", "dir");
            nodeData.setLong("entries", Objects.requireNonNull(file.list()).length);

            if (parent != null) {
                nodeData.setString("location", sanitizePath(file.getPath()));
            }

            String nodeParent = getParent(parent);
            nodeData.setString("parent", nodeParent);

            if (withContent) {
                Either<NBTTagList, String> either = getDirectoryListing(parent);
                if (either.issetA()) {
                    nodeData.setTag("content", either.getA());
                } else {
                    nodeData.setString("error", either.getB());
                }
            }
        }

        nodeData.setBoolean("flag.e", file.canExecute());
        nodeData.setBoolean("flag.r", file.canRead());
        nodeData.setBoolean("flag.w", file.canWrite());

        return new Either<NBTTagCompound, String>(nodeData, null);
    }

    public String sanitizePath(String path) {
        // No path given, return root.
        if (path == null)
            return "/";

        // Trim all whitespace...
        path = path.trim();

        // make sure there are no '.' at the start of the path
        while (path.startsWith(".")) {
            path = path.substring(1);
        }

        // we just reduced the path, check if its empty
        if (path.isEmpty())
            return "/";

        path = path.replace(File.separatorChar, '/');

        // is there a slash at the start of the path?
        if (!path.startsWith("/")) {
            // No; Add a slash to the start of the path.
            path = "/".concat(path);
        }

        // Is the path equal to a slash?
        if (path.equals("/"))
            return "/";

        // Evil dot placement prevention.
        if (path.contains("/."))
            return "/";
        if (path.contains("./"))
            return "/";

        // All checks passed; return the path!
        return path;
    }

    public File sanitize(File file) {
        if (file == null) {
            // No file given.
            return null;
        }

        if (!file.exists()) {
            // File does not exist.
            return null;
        }

        if (file.isHidden()) {
            // Do not display hidden files.
            return null;
        }

        if (file.getName().startsWith(".")) {
            return null;
        }

        if (file.isFile()) {
            // Is FILE, return it.
            return file;
        }

        if (file.isDirectory()) {
            // Is DIRECTORY, return it.
            return file;
        }

        // Given path is neither a file nor a directory.
        // Return null!
        return null;
    }

    public String getParent(String path) {
        if (path == null)
            return "/";

        int cut = path.lastIndexOf('/');
        if (cut > 1) {
            path = path.substring(0, cut);
        } else {
            return "/";
        }

        return path;
    }

}