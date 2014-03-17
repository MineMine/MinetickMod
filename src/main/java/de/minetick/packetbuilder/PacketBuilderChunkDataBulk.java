package de.minetick.packetbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.minetick.PlayerChunkSendQueue;

import net.minecraft.server.Chunk;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet;
import net.minecraft.server.PlayerConnection;
import net.minecraft.server.TileEntity;

public class PacketBuilderChunkDataBulk {

    private Map<Chunk, List<TileEntity>> map;

    public PacketBuilderChunkDataBulk() {
        this.map = new HashMap<Chunk, List<TileEntity>>();
    }

    public void addChunk(Chunk chunk) {
        List<TileEntity> list = new ArrayList<TileEntity>();
        list.addAll(chunk.tileEntities.values());
        this.map.put(chunk, list);
    }

    public boolean verifyLoadedChunks(PlayerChunkSendQueue pcsq) {
        boolean allOk = !this.isEmpty();
        Iterator<Chunk> iterator = this.map.keySet().iterator();
        while(iterator.hasNext()) {
            Chunk chunk = iterator.next();
            if(!pcsq.isOnServer(chunk.locX, chunk.locZ)) {
                List<TileEntity> list = this.map.get(chunk);
                list.clear();
                iterator.remove();
                allOk = false;
            }
        }
        return allOk;
    }

    public void sendTileEntities(PlayerConnection connection) {
        for(List<TileEntity> list: this.map.values()) {
            for(TileEntity tileEntity: list) {
                if (tileEntity != null) {
                    Packet packet = tileEntity.getUpdatePacket();
                    if(packet != null) {
                        connection.sendPacket(packet);
                    }
                }
            }
        }
    }

    public void queueChunksForTracking(EntityPlayer entityplayer, PlayerChunkSendQueue sendQueue) {
        entityplayer.chunksForTracking.addAll(this.map.keySet());
    }

    public void clear() {
        Iterator<Entry<Chunk, List<TileEntity>>> iterator = this.map.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<Chunk, List<TileEntity>> entry = iterator.next();
            entry.getValue().clear();
            iterator.remove();
        }
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public List<Chunk> getChunks() {
        return new ArrayList<Chunk>(this.map.keySet());
    }

    public int size() {
        return this.map.size();
    }
}
