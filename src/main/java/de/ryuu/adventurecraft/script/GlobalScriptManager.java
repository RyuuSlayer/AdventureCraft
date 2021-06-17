package de.ryuu.adventurecraft.script;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.entity.EntityMovingBlock;
import de.ryuu.adventurecraft.entity.NPC.EntityNPC;
import de.ryuu.adventurecraft.proxy.CommonProxy;
import de.ryuu.adventurecraft.script.wrappers.IObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.entity.EntityObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.entity.MovingBlockObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.entity.NPCObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.entity.PlayerObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.item.ItemStackObjectWrapper;
import de.ryuu.adventurecraft.script.wrappers.nbt.CompoundTagWrapper;
import de.ryuu.adventurecraft.script.wrappers.world.WorldObjectWrapper;
import de.ryuu.adventurecraft.util.MutableBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GlobalScriptManager {
    private NativeObject globalScope;
    private ClassShutter globalClassShutter;
    private ContextFactory globalContextFactory;
    private GlobalScriptObject globalScriptObject;
    private ConsoleOutput consoleOutput;
    private WrapperClassCache cache;

    public void init(AdventureCraft adventureCraft, CommonProxy proxy) {
        AdventureCraft.logger.info("Initializing Rhino Script Engine...");
        globalScope = new NativeObject();
        globalClassShutter = new GlobalClassShutter();
        cache = new WrapperClassCache();
        globalContextFactory = new GlobalContextFactory();
        ContextFactory.initGlobal(globalContextFactory);

        Context cx = Context.enter();
        try {
            ScriptRuntime.initStandardObjects(cx, globalScope, true);

            globalScriptObject = new GlobalScriptObject(this);
            ScriptableObject.putProperty(globalScope, "system", Context.javaToJS(globalScriptObject, globalScope));

            consoleOutput = new ConsoleOutput();
            ScriptableObject.putProperty(globalScope, "out", Context.javaToJS(consoleOutput, globalScope));

            // String loadMe = "RegExp; getClass; java; Packages; JavaAdapter;";
            // cx.evaluateString(globalScope , loadMe, "lazyLoad", 0, null);

            // Startup Script Test
            String startupTestScript = "msg = \"Rhino Time!\"; msg;";
            Object startupTestScriptResult = cx.evaluateString(globalScope, startupTestScript, "<cmd>", 0, null);
            AdventureCraft.logger.info("Startup Script Test: " + startupTestScriptResult);
        } finally {
            Context.exit();
        }

        AdventureCraft.logger.info("Script Engine initialized!");
    }

    public void contextCreation(Context cx) {
        cx.setClassShutter(globalClassShutter);
    }

    public Scriptable createNewScope() {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);
        Context.exit();
        return newScope;
    }

    public Scriptable createNewBlockScope(World world, BlockPos blockpos) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);

        ScriptableObject.putProperty(newScope, "position", Context.javaToJS(new MutableBlockPos(blockpos), newScope));
        ScriptableObject.putProperty(newScope, "world", Context.javaToJS(new WorldObjectWrapper(world), newScope));

        Context.exit();

        return newScope;
    }

    public Scriptable createNewMovingBlock(EntityMovingBlock moving) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);

        ScriptableObject.putProperty(newScope, "position",
                Context.javaToJS(new MutableBlockPos(moving.getPosition()), newScope));
        ScriptableObject.putProperty(newScope, "world",
                Context.javaToJS(new WorldObjectWrapper(moving.getEntityWorld()), newScope));
        ScriptableObject.putProperty(newScope, "entity",
                Context.javaToJS(new MovingBlockObjectWrapper(moving), newScope));

        Context.exit();

        return newScope;
    }

    public Scriptable createNewMovingBlock(EntityMovingBlock moving, Entity entity) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);

        ScriptableObject.putProperty(newScope, "position",
                Context.javaToJS(new MutableBlockPos(moving.getPosition()), newScope));
        ScriptableObject.putProperty(newScope, "world",
                Context.javaToJS(new WorldObjectWrapper(moving.getEntityWorld()), newScope));
        ScriptableObject.putProperty(newScope, "entity",
                Context.javaToJS(new MovingBlockObjectWrapper(moving), newScope));
        ScriptableObject.putProperty(newScope, "collide", Context.javaToJS(new EntityObjectWrapper(entity), newScope));

        Context.exit();

        return newScope;
    }

    public Scriptable createNewMovingBlock(EntityMovingBlock moving, EntityPlayer entity) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);

        ScriptableObject.putProperty(newScope, "position",
                Context.javaToJS(new MutableBlockPos(moving.getPosition()), newScope));
        ScriptableObject.putProperty(newScope, "world",
                Context.javaToJS(new WorldObjectWrapper(moving.getEntityWorld()), newScope));
        ScriptableObject.putProperty(newScope, "entity",
                Context.javaToJS(new MovingBlockObjectWrapper(moving), newScope));
        ScriptableObject.putProperty(newScope, "player", Context.javaToJS(new PlayerObjectWrapper(entity), newScope));

        Context.exit();

        return newScope;
    }

    public Scriptable createNewNPCScope(EntityNPC entity, ItemStack stack, EntityPlayer player) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);
        ScriptableObject.putProperty(newScope, "position",
                Context.javaToJS(new MutableBlockPos(entity.getPosition()), newScope));
        ScriptableObject.putProperty(newScope, "world",
                Context.javaToJS(new WorldObjectWrapper(entity.getEntityWorld()), newScope));
        ScriptableObject.putProperty(newScope, "npc", Context.javaToJS(new NPCObjectWrapper(entity), newScope));
        ScriptableObject.putProperty(newScope, "itemstack",
                Context.javaToJS(new ItemStackObjectWrapper(stack), newScope));
        ScriptableObject.putProperty(newScope, "player", Context.javaToJS(new PlayerObjectWrapper(player), newScope));
        Context.exit();

        return newScope;
    }

    public Scriptable createNewDecorationScope(World world, BlockPos[] pos, NBTTagCompound options) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);
        MutableBlockPos[] mpos = new MutableBlockPos[pos.length];
        for (int i = 0; i < pos.length; i++) {
            mpos[i] = new MutableBlockPos(pos[i]);
        }
        ScriptableObject.putProperty(newScope, "positions", Context.javaToJS(mpos, newScope));
        ScriptableObject.putProperty(newScope, "world", Context.javaToJS(new WorldObjectWrapper(world), newScope));
        ScriptableObject.putProperty(newScope, "options", Context.javaToJS(new CompoundTagWrapper(options), newScope));
        Context.exit();

        return newScope;
    }

    public Scriptable createNewNPCScope(EntityNPC entity) {
        Context cx = Context.enter();
        try {
            Scriptable newScope = cx.newObject(globalScope);
            newScope.setPrototype(globalScope);
            newScope.setParentScope(null);

            ScriptableObject.putProperty(newScope, "position",
                    Context.javaToJS(new MutableBlockPos(entity.getPosition()), newScope));
            ScriptableObject.putProperty(newScope, "world",
                    Context.javaToJS(new WorldObjectWrapper(entity.getEntityWorld()), newScope));
            ScriptableObject.putProperty(newScope, "npc", Context.javaToJS(new NPCObjectWrapper(entity), newScope));
            return newScope;
        } finally {
            Context.exit();
        }
    }

    public Scriptable createNewWorldScope(World world) {
        Context cx = Context.enter();
        Scriptable newScope = cx.newObject(globalScope);
        newScope.setPrototype(globalScope);
        newScope.setParentScope(null);

        ScriptableObject.putProperty(newScope, "world", Context.javaToJS(new WorldObjectWrapper(world), newScope));

        Context.exit();

        return newScope;
    }

    public Object interpret(String script, String fileName, Scriptable scope) {
        Object rvalue = null;
        Context cx = Context.enter();

        if (scope == null) {
            Scriptable newScope = cx.newObject(globalScope);
            newScope.setPrototype(globalScope);
            newScope.setParentScope(null);
            scope = newScope;
        }

        try {
            rvalue = cx.evaluateString(scope, script, fileName, 0, null);
        } catch (Throwable e) {
            e.printStackTrace();
            TextComponentString text = new TextComponentString("Script Error: " + e.getMessage());
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(text);
        } finally {
            Context.exit();
        }

        return rvalue;
    }

    /**
     * @return The script, or a short block-comment saying that an error ocurred.
     **/
    public String loadScript(World world, String fileName) {
        if (fileName.isEmpty()) {
            return "";
        }

        File worldDirectory = world.getSaveHandler().getWorldDirectory();

        File dataDir = worldDirectory;
        File scriptDir = new File(dataDir, "scripts");

        if (!scriptDir.exists()) {
            scriptDir.mkdir();
        }

        File scriptFile = new File(scriptDir, fileName + ".js");

        if (!scriptFile.exists()) {
            String message = "Script does not exist: " + scriptFile;
            AdventureCraft.logger.error(message);
            return "/*Failed to load script: " + fileName + ". Reason: " + message + "*/";
        }

        try {
            String script = FileUtils.readFileToString(scriptFile);
            // AdventureCraft.logger.info("Script successfully loaded: " + scriptFile + "
            // (~"+script.length()+" chars)");
            return script;
        } catch (IOException e) {
            AdventureCraft.logger.error("Failed to load Script: " + scriptFile);
            e.printStackTrace();
            return "/*Failed to load script: " + fileName + ". Reason: " + e.getMessage() + "*/";
        }
    }

    public void saveScript(World world, String fileContent, String fileName) throws IOException {
        File worldDirectory = world.getSaveHandler().getWorldDirectory();

        File dataDir = worldDirectory;
        File scriptDir = new File(dataDir, "scripts");

        if (!scriptDir.exists()) {
            scriptDir.mkdir();
        }

        File scriptFile = new File(scriptDir, fileName + ".js");
        FileUtils.writeStringToFile(scriptFile, fileContent);
    }

    public List<String> getOwnPropertyNames(IObjectWrapper wrapper) {
        return cache.getCache(wrapper).getProps();
    }

    public NativeObject getGlobalScope() {
        return globalScope;
    }

}