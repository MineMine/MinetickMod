package de.minetick.packetbuilder.jobs;

import java.util.ArrayList;

import net.minecraft.server.Chunk;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PlayerConnection;
import de.minetick.PlayerChunkSendQueue;
import de.minetick.packetbuilder.PacketBuilderBuffer;
import de.minetick.packetbuilder.PacketBuilderChunkData;
import de.minetick.packetbuilder.PacketBuilderJobInterface;

public class PBJobPlayOutMapChunk implements PacketBuilderJobInterface {

    private PlayerConnection connection;
    private PlayerChunkSendQueue chunkQueue;
    private PlayerConnection[] connections;
    private PlayerChunkSendQueue[] chunkQueues;
    private PacketBuilderChunkData chunkData;
    ArrayList<PlayerConnection> validOnes = new ArrayList<PlayerConnection>();
    private boolean multipleConnections;

    public PBJobPlayOutMapChunk(PlayerConnection connection, PlayerChunkSendQueue chunkQueue, PacketBuilderChunkData chunkData) {
        this.multipleConnections = false;
        this.connection = connection;
        this.chunkQueue = chunkQueue;
        this.chunkData = chunkData;
    }

    public PBJobPlayOutMapChunk(PlayerConnection[] connections, PlayerChunkSendQueue[] chunkQueues, PacketBuilderChunkData chunkData) {
        this.multipleConnections = true;
        this.connections = connections;
        this.chunkQueues = chunkQueues;
        this.chunkData = chunkData;
    }

    @Override
    public void buildAndSendPacket(PacketBuilderBuffer pbb) {
        boolean packetSent = false;
        Chunk chunk = this.chunkData.getChunk();
        PacketPlayOutMapChunk packet = new PacketPlayOutMapChunk(pbb, chunk, this.chunkData.getSendAllFlag(), this.chunkData.getSectionBitmask());
        if(this.multipleConnections) {
            for(int a = 0; a < this.connections.length; a++) {
                if(this.chunkQueues[a] != null && this.connections[a] != null) {
                    if(this.chunkQueues[a].isOnServer(chunk.locX, chunk.locZ)) {
                        this.validOnes.add(this.connections[a]);
                        this.connections[a] = null;
                        this.chunkQueues[a] = null;
                    }
                }
            }
            if(this.validOnes.size() > 0) {
                packet.setPendingUses(this.validOnes.size());
                packetSent = true;
                for(PlayerConnection n: this.validOnes) {
                    n.sendPacket(packet);
                    this.chunkData.sendTileEntities(n);
                }
            }
            this.connections = null;
            this.chunkQueues = null;
        } else {
            if(this.chunkQueue != null &&  this.connection != null) {
                if(!this.chunkQueue.isOnServer(chunk.locX, chunk.locZ)) {
                    packetSent = true;
                    packet.setPendingUses(1);
                    this.connection.sendPacket(packet);
                }
            }
            this.connection = null;
            this.chunkQueue = null;
        }
        if(!packetSent) {
            packet.discard();
        }
        this.clear();
    }

    public void clear() {
        this.validOnes.clear();
        this.validOnes = null;
        this.chunkData.clear();
        if(this.connections != null) {
            for(int i = 0; i < this.connections.length; i++) {
                this.connections[i] = null;
            }
            this.connections = null;
        }
        if(this.chunkQueues != null) {
            for(int i = 0; i < this.chunkQueues.length; i++) {
                this.chunkQueues[i] = null;
            }
            this.chunkQueues = null;
        }
        this.connection = null;
        this.chunkQueue = null;
    }
}
