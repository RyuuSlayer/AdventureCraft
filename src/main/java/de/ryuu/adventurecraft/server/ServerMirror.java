package de.ryuu.adventurecraft.server;

import de.ryuu.adventurecraft.AdventureCraft;
import de.ryuu.adventurecraft.invoke.IInvoke;
import de.ryuu.adventurecraft.invoke.IInvokeSource;
import net.minecraft.server.MinecraftServer;

public class ServerMirror {
    private MinecraftServer server;
    private PlayerList players;
    private ServerClipboard clipboard;
    private ServerFileSystem fileSystem;
    private boolean trackInvokes;

    public static ServerMirror instance() {
        return ServerHandler.getServerMirror(null);
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void create(MinecraftServer server) {
        AdventureCraft.logger.info("Creating Server Mirror: " + server);

        this.server = server;
        this.players = new PlayerList();
        this.clipboard = new ServerClipboard();
        this.fileSystem = new ServerFileSystem();
        this.trackInvokes = true;
    }

    public void destroy() {
        AdventureCraft.logger.info("Destroying Server Mirror: " + server);
        this.players.destroy();
    }

    public PlayerList playerList() {
        return players;
    }

    public ServerClipboard getClipboard() {
        return clipboard;
    }

    public ServerFileSystem getFileSystem() {
        return fileSystem;
    }

    public void trackInvoke(IInvokeSource source, IInvoke invoke) {
        if (!trackInvokes)
            return;

        for (PlayerMirror playerMirror : players.getBackingList()) {
            playerMirror.trackInvoke(source, invoke);
        }
    }

}