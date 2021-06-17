package de.ryuu.adventurecraft.invoke;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mozilla.javascript.Scriptable;

import java.util.List;

public interface IInvokeSource {

    public Scriptable getInvokeScriptScope();

    public ICommandSender getInvokeAsCommandSender();

    public BlockPos getInvokePosition();

    public World getInvokeWorld();

    public void getInvokes(List<IInvoke> invokes);

    public void getInvokeColor(float[] color);

}