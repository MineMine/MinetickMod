package de.minetick;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.bukkit.craftbukkit.util.LongHash;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PlayerChunk;

public class PlayerChunkSendQueue {

    private LinkedHashSet<Long> serverData; // what it should be
    private LinkedHashSet<Long> clientData; // sent Data
    private LinkedList<ChunkCoordIntPair> queue; // waiting to be sent
    private LinkedList<ChunkCoordIntPair> skippedChunks;
    private PlayerChunkManager pcm;
    private EntityPlayer player;

    public PlayerChunkSendQueue(PlayerChunkManager pcm, EntityPlayer entityplayer) {
        this.pcm = pcm;
        this.serverData = new LinkedHashSet<Long>();
        this.clientData = new LinkedHashSet<Long>();
        this.queue = new LinkedList<ChunkCoordIntPair>();
        this.skippedChunks = new LinkedList<ChunkCoordIntPair>();
        this.player = entityplayer;
    }
    
    public void sort(EntityPlayer entityplayer) {
        ChunkCoordComparator comp = new ChunkCoordComparator(entityplayer);
        Collections.sort(this.queue, comp);
        Collections.sort(this.player.chunkCoordIntPairQueue, comp);
    }
    
    public boolean hasChunksQueued() {
        return this.queue.size() > 0;
    }
    
    public boolean queueForSend(PlayerChunk playerchunk, EntityPlayer entityplayer) {
        boolean alreadySent = false, onServer = false, aboutToSend = false;
        ChunkCoordIntPair ccip = PlayerChunk.a(playerchunk);
        alreadySent = this.clientData.contains(LongHash.toLong(ccip.x, ccip.z));
        onServer = this.serverData.contains(LongHash.toLong(ccip.x, ccip.z));
        aboutToSend = this.isAboutToSend(ccip);
        if(onServer) {
            if(!aboutToSend && !alreadySent) {
                this.queue.add(ccip);
                this.player.chunkCoordIntPairQueue.add(ccip);
                return true;
            }
        } else {
            playerchunk.b(entityplayer);
            this.removeFromClient(ccip);
        }
        return false;
    }
    
    public void addToServer(int x, int z) {
        this.serverData.add(LongHash.toLong(x, z));
    }
    
    public void removeFromServer(int x, int z) {
        this.serverData.remove(LongHash.toLong(x, z));
    }

    public void removeFromClient(ChunkCoordIntPair ccip) {
        this.clientData.remove(LongHash.toLong(ccip.x, ccip.z));
        this.removeFromQueue(ccip);
    }

    public void removeFromQueue(ChunkCoordIntPair ccip) {
        this.queue.remove(ccip);
        this.skippedChunks.remove(ccip);
        this.player.chunkCoordIntPairQueue.remove(ccip);
    }

    public ChunkCoordIntPair peekFirst() {
        ChunkCoordIntPair ccip = null;
        boolean foundOne = false;
        while(!foundOne && !this.queue.isEmpty()) {
            ccip = this.queue.peekFirst();
            if(!this.isOnServer(ccip)) {
                this.removeFromQueue(ccip);
                ccip = null;
            } else {
                foundOne = true;
            }
        }
        return ccip;
    }
    
    public void removeFirst(boolean ok) {
        if(!this.queue.isEmpty()) {
            ChunkCoordIntPair ccip = this.queue.removeFirst();
            if(ok) {
                this.clientData.add(LongHash.toLong(ccip.x, ccip.z));
                this.player.chunkCoordIntPairQueue.remove(ccip);
            }
        }
    }
    
    public void skipFirst() {
        if(!this.queue.isEmpty()) {
            ChunkCoordIntPair ccip = this.queue.removeFirst();
            this.player.chunkCoordIntPairQueue.remove(ccip);
            if(this.isOnServer(ccip) && !this.isChunkSent(ccip)) {
                 this.skippedChunks.addLast(ccip);
            }
        }
    }

    public int requeuePreviouslySkipped() {
        int count = 0;
        while(this.skippedChunks.size() > 0) {
            ChunkCoordIntPair ccip = this.skippedChunks.removeFirst();
            if(this.isOnServer(ccip) && !this.alreadyLoaded(ccip)) {
                count++;
                this.queue.addFirst(ccip);
                this.player.chunkCoordIntPairQueue.add(ccip);
            }
        }
        return count;
    }
    
    public void clear() {
        this.serverData.clear();
        this.clientData.clear();
        this.queue.clear();
        this.skippedChunks.clear();
        this.player.chunkCoordIntPairQueue.clear();
    }

    public boolean isChunkSent(ChunkCoordIntPair ccip) {
        return this.clientData.contains(LongHash.toLong(ccip.x, ccip.z));
    }

    public boolean isAboutToSend(ChunkCoordIntPair location) {
        return this.skippedChunks.contains(location) || this.queue.contains(location);
    }

    public boolean alreadyLoaded(ChunkCoordIntPair ccip) {
        return this.isChunkSent(ccip) || this.isAboutToSend(ccip);
    }

    public boolean isOnServer(ChunkCoordIntPair ccip) {
        return this.isOnServer(ccip.x, ccip.z);
    }

    public boolean isOnServer(int x, int z) {
        return this.serverData.contains(LongHash.toLong(x, z));
    }
    
    public int size() {
        return this.queue.size();
    }
}
