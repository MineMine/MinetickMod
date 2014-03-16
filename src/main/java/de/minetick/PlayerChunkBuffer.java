package de.minetick;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;

import de.minetick.PlayerChunkManager.ChunkPosEnum;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PlayerChunk;
import net.minecraft.server.PlayerChunkMap;

public class PlayerChunkBuffer {
    private LinkedHashSet<ChunkCoordIntPair> lowPriorityBuffer;
    private LinkedHashSet<ChunkCoordIntPair> highPriorityBuffer;
    public PriorityQueue<ChunkCoordIntPair> pq;
    public ChunkCoordComparator comp;
    public int generatedChunks = 0;
    public int loadedChunks = 0;
    public int skippedChunks = 0;
    public int enlistedChunks = 0;
    private PlayerChunkManager playerChunkManager;
    private PlayerChunkSendQueue sendQueue;
    private boolean playerHasMoved = false;
    private int[] playerRegionCenter;
    private int[] lastMovement;

    public PlayerChunkBuffer(PlayerChunkManager playerChunkManager, EntityPlayer ent) {
        this.playerChunkManager = playerChunkManager;
        this.lowPriorityBuffer = new LinkedHashSet<ChunkCoordIntPair>();
        this.highPriorityBuffer = new LinkedHashSet<ChunkCoordIntPair>();
        this.sendQueue = new PlayerChunkSendQueue(this.playerChunkManager, ent);
        this.comp = new ChunkCoordComparator(ent);
        this.pq = new PriorityQueue<ChunkCoordIntPair>(750, this.comp);
        this.playerRegionCenter = new int[] { MathHelper.floor(ent.locX) >> 4, MathHelper.floor(ent.locZ) >> 4 };
        this.lastMovement = new int[] { 0, 0 };
    }

    public PlayerChunkSendQueue getPlayerChunkSendQueue() {
        return this.sendQueue;
    }

    public Comparator<ChunkCoordIntPair> updatePos(EntityPlayer entityplayer) {
        this.comp.setPos(entityplayer);
        if(this.playerHasMoved) {
            PlayerChunkMap pcm = this.playerChunkManager.getPlayerChunkMap();
            int newCenterX = this.playerRegionCenter[0];
            int newCenterZ = this.playerRegionCenter[1];
            int oldCenterX = newCenterX - this.lastMovement[0];
            int oldCenterZ = newCenterZ - this.lastMovement[1];
            int diffX = this.lastMovement[0];
            int diffZ = this.lastMovement[1];
            if(diffX == 0 && diffZ == 0) {
                //return this.comp;
            }
            int radius = pcm.getViewDistance();
            int added = 0, removed = 0;
            for (int pointerX = newCenterX - radius; pointerX <= newCenterX + radius; pointerX++) {
                for (int pointerZ = newCenterZ - radius; pointerZ <= newCenterZ + radius; pointerZ++) {
                    ChunkCoordIntPair ccip;
                    ChunkPosEnum pos = PlayerChunkManager.isWithinRadius(pointerX, pointerZ, oldCenterX, oldCenterZ, radius);
                    if(!pos.equals(ChunkPosEnum.INSIDE)) {
                        ccip = new ChunkCoordIntPair(pointerX, pointerZ);
                        if(!this.sendQueue.alreadyLoaded(ccip) && !this.sendQueue.isOnServer(ccip)) {
                            added++;
                            this.sendQueue.addToServer(pointerX, pointerZ);
                            if(this.playerChunkManager.doAllCornersOfPlayerAreaExist(newCenterX, newCenterZ, radius)) {
                                this.addHighPriorityChunk(ccip);
                            } else {
                                this.addLowPriorityChunk(ccip);
                            }
                        }
                        //continue;
                    }

                    pos = PlayerChunkManager.isWithinRadius(pointerX - diffX, pointerZ - diffZ, newCenterX, newCenterZ, radius);
                    if(!pos.equals(ChunkPosEnum.INSIDE)) {
                        removed++;
                        ccip = new ChunkCoordIntPair(pointerX - diffX, pointerZ - diffZ);
                        this.sendQueue.removeFromServer(ccip.x, ccip.z);
                        this.sendQueue.removeFromClient(ccip);
                        this.remove(ccip);
                        PlayerChunk playerchunk = pcm.a(ccip.x, ccip.z, false);
                        if (playerchunk != null) {
                            playerchunk.b(entityplayer);
                        }
                    }
                }
            }
        }
        this.playerHasMoved = false;
        return this.comp;
    }

    public void clear() {
        this.lowPriorityBuffer.clear();
        this.highPriorityBuffer.clear();
        this.pq.clear();
        this.sendQueue.clear();
        this.playerHasMoved = false;
    }

    public boolean isEmpty() {
        return this.lowPriorityBuffer.isEmpty() && this.highPriorityBuffer.isEmpty();
    }

    public LinkedHashSet<ChunkCoordIntPair> getLowPriorityBuffer() {
        return this.lowPriorityBuffer;
    }

    public LinkedHashSet<ChunkCoordIntPair> getHighPriorityBuffer() {
        return this.highPriorityBuffer;
    }

    public void addLowPriorityChunk(ChunkCoordIntPair ccip) {
        this.lowPriorityBuffer.add(ccip);
    }

    public void addHighPriorityChunk(ChunkCoordIntPair ccip) {
        this.highPriorityBuffer.add(ccip);
    }

    public void remove(ChunkCoordIntPair ccip) {
        this.lowPriorityBuffer.remove(ccip);
        this.highPriorityBuffer.remove(ccip);
    }

    public boolean contains(ChunkCoordIntPair ccip) {
        return this.lowPriorityBuffer.contains(ccip) || this.highPriorityBuffer.contains(ccip);
    }

    public PriorityQueue<ChunkCoordIntPair> getLowPriorityQueue() {
        this.pq.clear();
        this.pq.addAll(this.lowPriorityBuffer);
        return this.pq;
    }

    public PriorityQueue<ChunkCoordIntPair> getHighPriorityQueue() {
        this.pq.clear();
        this.pq.addAll(this.highPriorityBuffer);
        return this.pq;
    }

    public void resetCounters() {
        this.generatedChunks = 0;
        this.enlistedChunks = 0;
        this.skippedChunks = 0;
        this.loadedChunks = 0;
    }

    public void playerMoved(int newCenterX, int newCenterZ) {
        this.lastMovement[0] = newCenterX - this.playerRegionCenter[0];
        this.lastMovement[1] = newCenterZ - this.playerRegionCenter[1];
        this.playerRegionCenter[0] = newCenterX;
        this.playerRegionCenter[1] = newCenterZ;
        this.playerHasMoved = true;
    }

    public int[] getPlayerRegionCenter() {
        return this.playerRegionCenter;
    }

    public int[] getLastMovement() {
        return this.lastMovement;
    }
}
