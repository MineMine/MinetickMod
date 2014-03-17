package de.minetick.modcommands;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PlayerChunk;
import net.minecraft.server.WorldServer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minetick.PlayerChunkBuffer;
import de.minetick.PlayerChunkManager;
import de.minetick.PlayerChunkSendQueue;

public class ChunkTestCommand extends Command {

    public ChunkTestCommand(String name) {
        super(name);
        this.usageMessage = "/chunktest";
        this.description = "Prints debug info about the current chunk";
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            CraftPlayer craftplayer = (CraftPlayer) player;
            EntityPlayer entityplayer = craftplayer.getHandle();
            Location pos = player.getLocation();
            int chunkX = pos.getBlockX() >> 4;
            int chunkZ = pos.getBlockZ() >> 4;
            World world = player.getWorld();
            CraftWorld craftworld = (CraftWorld) world;
            WorldServer worldserver = craftworld.getHandle();
            PlayerChunkManager pcm = worldserver.getPlayerChunkMap().getPlayerChunkManager();
            PlayerChunkBuffer pcb = pcm.getChunkBuffer(entityplayer);
            PlayerChunkSendQueue pcsq = pcb.getPlayerChunkSendQueue();
            ChunkCoordIntPair ccip = new ChunkCoordIntPair(chunkX, chunkZ);
            boolean onServer = pcsq.isOnServer(chunkX, chunkZ);
            boolean aboutToSend = pcsq.isAboutToSend(ccip);
            boolean isSent = pcsq.isChunkSent(ccip);
            boolean alreadyLoaded = pcsq.alreadyLoaded(ccip);
            player.sendMessage("Checking chunk ( " + chunkX + " / " + chunkZ + " )");
            player.sendMessage("S: " + onServer + " ATS: " + aboutToSend + " sent: " + isSent);
            String msg = "AL: " + alreadyLoaded;
            PlayerChunk pc = pcm.getPlayerChunkMap().a(chunkX, chunkZ, false);
            if(pc != null) {
                boolean enlisted = PlayerChunk.b(pc).contains(entityplayer);
                msg += " enlisted: " + enlisted;
            } else {
                msg += " enlisted: PC null";
            }
            player.sendMessage(msg);
        } else {
            sender.sendMessage("Must be run from within the game");
        }
        return false;
    }

}
