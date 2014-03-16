package de.minetick;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.bukkit.craftbukkit.util.LongHash;

import de.minetick.PlayerChunkManager.ChunkPosEnum;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PlayerChunk;

public class PlayerChunkSendQueue {

    private LinkedHashSet<Long> serverData; // what it should be
    private LinkedHashSet<Long> clientData; // sent Data
    private LinkedList<ChunkCoordIntPair> queue; // waiting to be sent
    private LinkedList<ChunkCoordIntPair> skippedChunks;
    private Object lock = new Object();
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
        synchronized(this.lock) {
            ChunkCoordComparator comp = new ChunkCoordComparator(entityplayer);
            Collections.sort(this.queue, comp);
            Collections.sort(this.player.chunkCoordIntPairQueue, comp);
        }
    }
    
    public boolean hasChunksQueued() {
        return this.queue.size() > 0;
    }
    
    public boolean queueForSend(PlayerChunk playerchunk, EntityPlayer entityplayer) {
        boolean alreadySent = false, onServer = false, inQueue = false;
        ChunkCoordIntPair ccip = PlayerChunk.a(playerchunk);
        synchronized(this.lock) {
            alreadySent = this.clientData.contains(LongHash.toLong(ccip.x, ccip.z));
            onServer = this.serverData.contains(LongHash.toLong(ccip.x, ccip.z));
            inQueue = this.queue.contains(ccip);
            if(onServer) {
                if(!inQueue && !alreadySent) {
                    this.queue.add(ccip);
                    this.player.chunkCoordIntPairQueue.add(ccip);
                    return true;
                }
            } else {
                playerchunk.b(entityplayer);
                this.removeFromClient(ccip);
            }
        }
        return false;
    }
    
    public void addToServer(int x, int z) {
        synchronized(this.lock) {
            this.serverData.add(LongHash.toLong(x, z));
        }
    }
    
    public void checkServerDataSize(int x, int z, int viewDistance, EntityPlayer entityplayer) {
        int a = viewDistance * 2 + 1;
        if(this.serverData.size() > a*a) {
            synchronized(this.lock) {
                Iterator<Long> iter = this.serverData.iterator();
                while(iter.hasNext()) {
                    Long l = iter.next();
                    int i = LongHash.lsw(l.longValue());
                    int j = LongHash.msw(l.longValue());
                    ChunkPosEnum pos = PlayerChunkManager.isWithinRadius(i, j, x, z, viewDistance);
                    if(pos.equals(ChunkPosEnum.OUTSIDE)) {
                        PlayerChunk c = this.pcm.getPlayerChunkMap().a(i, j, false);
                        if(c != null) {
                            c.b(entityplayer);
                            this.removeFromClient(PlayerChunk.a(c));
                        }
                        iter.remove(); // remove from server
                    }
                }
            }
        }
    }
    
    public void removeFromServer(int x, int z) {
        synchronized(this.lock) {
            this.serverData.remove(LongHash.toLong(x, z));
        }
    }
    
    public void removeFromClient(ChunkCoordIntPair ccip) {
        synchronized(this.lock) {
            this.clientData.remove(LongHash.toLong(ccip.x, ccip.z));
            this.queue.remove(ccip);
            this.player.chunkCoordIntPairQueue.remove(ccip);
        }
    }

    public void removeFromQueue(ChunkCoordIntPair ccip) {
        synchronized(this.lock) {
            this.queue.remove(ccip);
            this.player.chunkCoordIntPairQueue.remove(ccip);
        }
    }

    public ChunkCoordIntPair peekFirst() {
        ChunkCoordIntPair cc = null;
        synchronized(this.lock) {
            boolean foundOne = false;
            while(!foundOne && !this.queue.isEmpty()) {
                cc = this.queue.peekFirst();
                if(!this.serverData.contains(LongHash.toLong(cc.x, cc.z)) || this.clientData.contains(LongHash.toLong(cc.x, cc.z))) {
                    this.queue.removeFirst();
                    this.player.chunkCoordIntPairQueue.remove(cc);
                    cc = null;
                } else {
                    foundOne = true;
                }
            }
        }
        return cc;
    }
    
    public void removeFirst() {
        synchronized(this.lock) {
            if(!this.queue.isEmpty()) {
                ChunkCoordIntPair ccip = this.queue.removeFirst();
                this.clientData.add(LongHash.toLong(ccip.x, ccip.z));
                this.player.chunkCoordIntPairQueue.remove(ccip);
            }
        }
    }
    
    public void skipFirst() {
        synchronized(this.lock) {
            if(!this.queue.isEmpty()) {
                ChunkCoordIntPair ccip = this.queue.removeFirst();
                this.player.chunkCoordIntPairQueue.remove(ccip);
                if(this.isOnServer(ccip) && !this.isChunkSent(ccip)) {
                     this.skippedChunks.addLast(ccip);
                }
            }
        }
    }

    public int requeuePreviouslySkipped() {
        int count = 0;
        synchronized(this.lock) {
            while(this.skippedChunks.size() > 0) {
                ChunkCoordIntPair ccip = this.skippedChunks.removeLast();
                if(this.isOnServer(ccip) && !this.alreadyLoaded(ccip)) {
                    count++;
                    this.queue.addFirst(ccip);
                    this.player.chunkCoordIntPairQueue.add(ccip);
                }
            }
        }
        return count;
    }
    
    public void clear() {
        synchronized(this.lock) {
            this.serverData.clear();
            this.clientData.clear();
            this.queue.clear();
            this.skippedChunks.clear();
            this.player.chunkCoordIntPairQueue.clear();
        }
    }

    public boolean isChunkSent(ChunkCoordIntPair ccip) {
        return this.clientData.contains(LongHash.toLong(ccip.x, ccip.z));
    }

    public boolean isAboutToSend(ChunkCoordIntPair location) {
        synchronized(this.lock) {
            return this.skippedChunks.contains(location) || this.queue.contains(location);
        }
    }

    public boolean alreadyLoaded(ChunkCoordIntPair ccip) {
        synchronized(this.lock) {
            return this.isChunkSent(ccip) || this.isAboutToSend(ccip);
        }
    }

    public boolean isOnServer(ChunkCoordIntPair ccip) {
            return this.serverData.contains(LongHash.toLong(ccip.x, ccip.z));
    }

    public boolean isOnServer(int x, int z) {
        return this.serverData.contains(LongHash.toLong(x, z));
    }

    public int size() {
        return this.queue.size();
    }
}
