package de.minetick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;

import de.minetick.antixray.AntiXRay;
import de.minetick.modcommands.AntiXRayCommand;
import de.minetick.modcommands.PacketCompressionCommand;
import de.minetick.modcommands.PacketsPerTickCommand;
import de.minetick.modcommands.TPSCommand;
import de.minetick.modcommands.ThreadListCommand;
import de.minetick.modcommands.ThreadPoolsCommand;
import de.minetick.modcommands.WorldStatsCommand;
import de.minetick.packetbuilder.PacketBuilderThreadPool;
import de.minetick.profiler.Profiler;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EntityWither;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PacketPlayOutMapChunkBulk;
import net.minecraft.server.WorldServer;

public class MinetickMod {

    private TickTimer tickTimerObject;
    private int timerDelay = 45;
    private ScheduledExecutorService timerService = Executors.newScheduledThreadPool(2, new MinetickThreadFactory("MinetickMod_TimerService"));
    private ExecutorService nbtFileService = Executors.newCachedThreadPool(new MinetickThreadFactory(Thread.NORM_PRIORITY - 2, "MinetickMod_NBTFileSaver"));
    private ScheduledFuture<Object> tickTimerTask;
    private static MinetickMod instance;
    private Profiler profiler;
    private ThreadPool threadPool;
    private boolean initDone = false;
    private TickCounter tickCounterObject;
    private List<Integer> ticksPerSecond;
    private int ticksCounter = 0;
    public static final int defaultPacketCompression = 7;
    private PacketBuilderThreadPool packetBuilderPool;
    private int availableProcessors;
    private int packetbuilderPoolSize;
    private int antixrayPoolSize;
    private HashSet<String> notGeneratingWorlds;
    private int maxEntityLifeTime = 10;
    private HashSet<EntityType> entitiesToDelete;
    private final Logger log = LogManager.getLogger();

    public MinetickMod() {
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
        this.tickTimerObject = new TickTimer();
        this.tickCounterObject = new TickCounter();
        this.ticksPerSecond = Collections.synchronizedList(new LinkedList<Integer>());
        this.timerService.scheduleAtFixedRate(this.tickCounterObject, 1, 1, TimeUnit.SECONDS);
        this.notGeneratingWorlds = new HashSet<String>();
        this.entitiesToDelete = new HashSet<EntityType>();
        instance = this;
    }
    
    public void init() {
        if(!this.initDone) {
            this.initDone = true;
            CraftServer craftserver = MinecraftServer.getServer().server;
            craftserver.getCommandMap().register("tps", "MinetickMod", new TPSCommand("tps"));
            craftserver.getCommandMap().register("packetspertick", "MinetickMod", new PacketsPerTickCommand("packetspertick"));
            craftserver.getCommandMap().register("packetcompression", "MinetickMod", new PacketCompressionCommand("packetcompression"));
            craftserver.getCommandMap().register("antixray", "MinetickMod", new AntiXRayCommand("antixray"));
            craftserver.getCommandMap().register("threadpools", "MinetickMod", new ThreadPoolsCommand("threadpools"));
            craftserver.getCommandMap().register("worldstats", "MinetickMod", new WorldStatsCommand("worldstats"));
            craftserver.getCommandMap().register("threadlist", "MinetickMod", new ThreadListCommand("threadlist"));
            this.profiler = new Profiler(craftserver.getMinetickModProfilerLogInterval(),
                    craftserver.getMinetickModProfilerWriteEnabled(),
                    craftserver.getMinetickModProfilerWriteInterval());
            this.threadPool = new ThreadPool(this.profiler);
            AntiXRay.setWorldsFromConfig(craftserver.getMinetickModOrebfuscatedWorlds());
            int axrps = craftserver.getMinetickModAntiXRayPoolSize();
            if(axrps <= 0 || axrps > 64) {
                axrps = this.availableProcessors;
            }
            this.antixrayPoolSize = axrps;
            AntiXRay.adjustThreadPoolSize(this.antixrayPoolSize);
            int pbps = craftserver.getMinetickModPacketBuilderPoolSize();
            if(pbps <= 0 || pbps > 64) {
                pbps = this.availableProcessors;
            }
            this.packetbuilderPoolSize = pbps;
            this.packetBuilderPool = new PacketBuilderThreadPool(this.packetbuilderPoolSize);
            int level = craftserver.getMinetickModCompressionLevel();
            if(level < 1 || level > 9) {
                level = defaultPacketCompression;
            }
            PacketPlayOutMapChunk.changeCompressionLevel(level);
            PacketPlayOutMapChunkBulk.changeCompressionLevel(level);
            int packets = craftserver.getMinetickModPacketsPerTick();
            if(packets < 1 || packets > 20) {
                packets = 1;
            }
            PlayerChunkManager.packetsPerTick = packets;
            List<String> worlds = craftserver.getMinetickModNotGeneratingWorlds();
            for(String w: worlds) {
                this.notGeneratingWorlds.add(w.toLowerCase());
            }
            this.maxEntityLifeTime = craftserver.getMinetickModMaxEntityLifeTime();
            List<String> entitiesToDelete = craftserver.getMinetickModEntitiesWithLimitedLifeTime();
            for(String name: entitiesToDelete) {
                try {
                    EntityType type = EntityType.valueOf(name.toUpperCase());
                    this.entitiesToDelete.add(type);
                } catch (IllegalArgumentException e) {
                    log.warn("[MinetickMod] Settings: Skipping \"" + name + "\", as it is not a constant in org.bukkit.entity.EntityType!");
                }
            }
        }
    }

    public Profiler getProfiler() {
        return this.profiler;
    }

    public static Profiler getProfilerStatic() {
        return instance.getProfiler();
    }

    public ThreadPool getThreadPool() {
        return this.threadPool;
    }

    public static boolean isImportantEntity(Entity entity) {
        return (entity instanceof EntityArrow || entity instanceof EntityPlayer || 
                entity instanceof EntityProjectile || entity instanceof EntityFireball ||
                entity instanceof EntityWither || entity instanceof EntityEnderCrystal ||
                entity instanceof EntityEnderDragon);
    }
    
    public void shutdown() {
        this.timerService.shutdown();
        this.threadPool.shutdown();
        PacketBuilderThreadPool.shutdownStatic();
        AntiXRay.shutdown();
        this.nbtFileService.shutdown();
        while(!this.nbtFileService.isTerminated()) {
            try {
                if(!this.nbtFileService.awaitTermination(3, TimeUnit.MINUTES)) {
                    log.warn("MinetickMod is still waiting for NBT Files to be saved.");
                }
            } catch(InterruptedException e) {}
        }
    }

    public void checkTickTime(long tickTime) {         
        if(tickTime > 45000000L) {
            if(this.timerDelay > 40) {
                this.timerDelay--;
            }
        } else if(this.timerDelay < 45) {
            this.timerDelay++;
        }
    }

    public void startTickTimerTask() {
        this.tickTimerTask = instance.timerService.schedule(this.tickTimerObject, this.timerDelay, TimeUnit.MILLISECONDS);
    }

    public void cancelTimerTask(boolean flag) {
        this.tickTimerTask.cancel(false);
    }

    private class TickTimer implements Callable<Object> {
        public Object call() {
            MinecraftServer.getServer().cancelHeavyCalculationsForAllWorlds(true);
            return null;
        }
    }

    private class TickCounter implements Runnable {
        @Override
        public void run() {
            ticksPerSecond.add(ticksCounter);
            ticksCounter = 0;
            if(ticksPerSecond.size() > 30) {
                ticksPerSecond.remove(0);
            }
        }
    }

    public void increaseTickCounter() {
        this.ticksCounter++;
    }

    public static Integer[] getTicksPerSecond() {
        return instance.ticksPerSecond.toArray(new Integer[0]);
    }

    public static boolean doesWorldNotGenerateChunks(String worldName) {
        return instance.notGeneratingWorlds.contains(worldName.toLowerCase());
    }

    public static void saveNBTFileStatic(NBTTagCompound compound, File file) {
        instance.saveNBTFile(compound, file);
    }

    public void saveNBTFile(NBTTagCompound compound, File file) {
        this.nbtFileService.submit(new NBTFileSaver(compound, file));
    }

    private class NBTFileSaver implements Callable<Object> {

        private NBTTagCompound compound;
        private File file;

        public NBTFileSaver(NBTTagCompound compound, File file) {
            this.compound = compound;
            this.file = file;
        }

        public Object call() {
            try {
                long start = System.currentTimeMillis();
                FileOutputStream fileoutputstream = new FileOutputStream(this.file);
                NBTCompressedStreamTools.a(this.compound, (OutputStream) fileoutputstream);
                fileoutputstream.close();
                long duration = System.currentTimeMillis() - start;
                if(duration > 1000L) {
                    log.info("Saving the file \"" + this.file.getAbsolutePath() + "\" took " + ((float)(duration/100L) / 10.0F) + " seconds.");
                }
            } catch (Exception e) {
                log.error("Error \""+ e.getMessage() +"\" while saving file: " + this.file.getAbsolutePath());
                e.printStackTrace();
            }
            this.compound = null;
            this.file = null;
            return null;
        }
    }

    public static int getMaxEntityLifeTime() {
        return instance.maxEntityLifeTime;
    }

    public static boolean isEntityAllowedToBeDeleted(EntityLiving entity) {
        return !isImportantEntity(entity) && instance.entitiesToDelete.contains(entity.getBukkitEntity().getType());
    }
}
