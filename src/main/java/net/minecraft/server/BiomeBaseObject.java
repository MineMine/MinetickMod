/*
 * This class was created and added to the package net.minecraft.server by Poweruser
 * Most of its code was written by Mojang though and moved here from their class
 * net.minecraft.server.BiomeBase in the attempt to give each world their own
 * biome objects, instead of just having one static collection of biomes that all
 * worlds access and use. That change is necessary to make concurrent chunk generation
 * in different worlds possible. 
 */

package net.minecraft.server;

import java.util.Set;

import net.minecraft.util.com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeBaseObject {
    private static final Logger aC = LogManager.getLogger();
    //protected static final BiomeTemperature a = new BiomeTemperature(0.1F, 0.2F);
    protected static final BiomeTemperature b = new BiomeTemperature(-0.5F, 0.0F);
    protected static final BiomeTemperature c = new BiomeTemperature(-1.0F, 0.1F);
    protected static final BiomeTemperature d = new BiomeTemperature(-1.8F, 0.1F);
    protected static final BiomeTemperature e = new BiomeTemperature(0.125F, 0.05F);
    protected static final BiomeTemperature f = new BiomeTemperature(0.2F, 0.2F);
    protected static final BiomeTemperature g = new BiomeTemperature(0.45F, 0.3F);
    protected static final BiomeTemperature h = new BiomeTemperature(1.5F, 0.025F);
    protected static final BiomeTemperature i = new BiomeTemperature(1.0F, 0.5F);
    protected static final BiomeTemperature j = new BiomeTemperature(0.0F, 0.025F);
    protected static final BiomeTemperature k = new BiomeTemperature(0.1F, 0.8F);
    protected static final BiomeTemperature l = new BiomeTemperature(0.2F, 0.3F);
    protected static final BiomeTemperature m = new BiomeTemperature(-0.2F, 0.1F);
    protected final BiomeBase[] biomes = new BiomeBase[256];
    public final BiomeBase OCEAN = (new BiomeOcean(0)).b(112).a("Ocean").a(c).initBiomeBaseObject(this, false);
    public final BiomeBase PLAINS = (new BiomePlains(1)).b(9286496).a("Plains").initBiomeBaseObject(this, true);
    public final BiomeBase DESERT = (new BiomeDesert(2)).b(16421912).a("Desert").b().a(2.0F, 0.0F).a(e).initBiomeBaseObject(this, true);
    public final BiomeBase EXTREME_HILLS = (new BiomeBigHills(3, false)).b(6316128).a("Extreme Hills").a(i).a(0.2F, 0.3F).initBiomeBaseObject(this, true);
    public final BiomeBase FOREST = (new BiomeForest(4, 0)).b(353825).a("Forest").initBiomeBaseObject(this, true);
    public final BiomeBase TAIGA = (new BiomeTaiga(5, 0)).b(747097).a("Taiga").a(5159473).a(0.25F, 0.8F).a(f).initBiomeBaseObject(this, true);
    public final BiomeBase SWAMPLAND = (new BiomeSwamp(6)).b(522674).a("Swampland").a(9154376).a(m).a(0.8F, 0.9F).initBiomeBaseObject(this, true);
    public final BiomeBase RIVER = (new BiomeRiver(7)).b(255).a("River").a(b).initBiomeBaseObject(this, false);
    public final BiomeBase HELL = (new BiomeHell(8)).b(16711680).a("Hell").b().a(2.0F, 0.0F).initBiomeBaseObject(this, false);
    public final BiomeBase SKY = (new BiomeTheEnd(9)).b(8421631).a("Sky").b().initBiomeBaseObject(this, false);
    public final BiomeBase FROZEN_OCEAN = (new BiomeOcean(10)).b(9474208).a("FrozenOcean").c().a(c).a(0.0F, 0.5F).initBiomeBaseObject(this, false);
    public final BiomeBase FROZEN_RIVER = (new BiomeRiver(11)).b(10526975).a("FrozenRiver").c().a(b).a(0.0F, 0.5F).initBiomeBaseObject(this, false);
    public final BiomeBase ICE_PLAINS = (new BiomeIcePlains(12, false)).b(16777215).a("Ice Plains").c().a(0.0F, 0.5F).a(e).initBiomeBaseObject(this, true);
    public final BiomeBase ICE_MOUNTAINS = (new BiomeIcePlains(13, false)).b(10526880).a("Ice Mountains").c().a(g).a(0.0F, 0.5F).initBiomeBaseObject(this, false);
    public final BiomeBase MUSHROOM_ISLAND = (new BiomeMushrooms(14)).b(16711935).a("MushroomIsland").a(0.9F, 1.0F).a(l).initBiomeBaseObject(this, false);
    public final BiomeBase MUSHROOM_SHORE = (new BiomeMushrooms(15)).b(10486015).a("MushroomIslandShore").a(0.9F, 1.0F).a(j).initBiomeBaseObject(this, false);
    public final BiomeBase BEACH = (new BiomeBeach(16)).b(16440917).a("Beach").a(0.8F, 0.4F).a(j).initBiomeBaseObject(this, false);
    public final BiomeBase DESERT_HILLS = (new BiomeDesert(17)).b(13786898).a("DesertHills").b().a(2.0F, 0.0F).a(g).initBiomeBaseObject(this, false);
    public final BiomeBase FOREST_HILLS = (new BiomeForest(18, 0)).b(2250012).a("ForestHills").a(g).initBiomeBaseObject(this, false);
    public final BiomeBase TAIGA_HILLS = (new BiomeTaiga(19, 0)).b(1456435).a("TaigaHills").a(5159473).a(0.25F, 0.8F).a(g).initBiomeBaseObject(this, false);
    public final BiomeBase SMALL_MOUNTAINS = (new BiomeBigHills(20, true)).b(7501978).a("Extreme Hills Edge").a(i.a()).a(0.2F, 0.3F).initBiomeBaseObject(this, false);
    public final BiomeBase JUNGLE = (new BiomeJungle(21, false)).b(5470985).a("Jungle").a(5470985).a(0.95F, 0.9F).initBiomeBaseObject(this, true);
    public final BiomeBase JUNGLE_HILLS = (new BiomeJungle(22, false)).b(2900485).a("JungleHills").a(5470985).a(0.95F, 0.9F).a(g).initBiomeBaseObject(this, false);
    public final BiomeBase JUNGLE_EDGE = (new BiomeJungle(23, true)).b(6458135).a("JungleEdge").a(5470985).a(0.95F, 0.8F).initBiomeBaseObject(this, true);
    public final BiomeBase DEEP_OCEAN = (new BiomeOcean(24)).b(48).a("Deep Ocean").a(d).initBiomeBaseObject(this, false);
    public final BiomeBase STONE_BEACH = (new BiomeStoneBeach(25)).b(10658436).a("Stone Beach").a(0.2F, 0.3F).a(k).initBiomeBaseObject(this, false);
    public final BiomeBase COLD_BEACH = (new BiomeBeach(26)).b(16445632).a("Cold Beach").a(0.05F, 0.3F).a(j).c().initBiomeBaseObject(this, false);
    public final BiomeBase BIRCH_FOREST = (new BiomeForest(27, 2)).a("Birch Forest").b(3175492).initBiomeBaseObject(this, true);
    public final BiomeBase BIRCH_FOREST_HILLS = (new BiomeForest(28, 2)).a("Birch Forest Hills").b(2055986).a(g).initBiomeBaseObject(this, true);
    public final BiomeBase ROOFED_FOREST = (new BiomeForest(29, 3)).b(4215066).a("Roofed Forest").initBiomeBaseObject(this, true);
    public final BiomeBase COLD_TAIGA = (new BiomeTaiga(30, 0)).b(3233098).a("Cold Taiga").a(5159473).c().a(-0.5F, 0.4F).a(f).c(16777215).initBiomeBaseObject(this, true);
    public final BiomeBase COLD_TAIGA_HILLS = (new BiomeTaiga(31, 0)).b(2375478).a("Cold Taiga Hills").a(5159473).c().a(-0.5F, 0.4F).a(g).c(16777215).initBiomeBaseObject(this, false);
    public final BiomeBase MEGA_TAIGA = (new BiomeTaiga(32, 1)).b(5858897).a("Mega Taiga").a(5159473).a(0.3F, 0.8F).a(f).initBiomeBaseObject(this, true);
    public final BiomeBase MEGA_TAIGA_HILLS = (new BiomeTaiga(33, 1)).b(4542270).a("Mega Taiga Hills").a(5159473).a(0.3F, 0.8F).a(g).initBiomeBaseObject(this, false);
    public final BiomeBase EXTREME_HILLS_PLUS = (new BiomeBigHills(34, true)).b(5271632).a("Extreme Hills+").a(i).a(0.2F, 0.3F).initBiomeBaseObject(this, true);
    public final BiomeBase SAVANNA = (new BiomeSavanna(35)).b(12431967).a("Savanna").a(1.2F, 0.0F).b().a(e).initBiomeBaseObject(this, true);
    public final BiomeBase SAVANNA_PLATEAU = (new BiomeSavanna(36)).b(10984804).a("Savanna Plateau").a(1.0F, 0.0F).b().a(h).initBiomeBaseObject(this, true);
    public final BiomeBase MESA = (new BiomeMesa(37, false, false)).b(14238997).a("Mesa").initBiomeBaseObject(this, true);
    public final BiomeBase MESA_PLATEAU_F = (new BiomeMesa(38, false, true)).b(11573093).a("Mesa Plateau F").a(h).initBiomeBaseObject(this, true);
    public final BiomeBase MESA_PLATEAU = (new BiomeMesa(39, false, false)).b(13274213).a("Mesa Plateau").a(h).initBiomeBaseObject(this, true);
    public final Set n = Sets.newHashSet();
    private static boolean initDone = false;
    
    public BiomeBaseObject() {
        this.biomes[this.MEGA_TAIGA_HILLS.id + 128] = this.biomes[this.MEGA_TAIGA.id + 128];
        BiomeBase[] abiomebase = this.biomes;
        int i = abiomebase.length;

        for (int j = 0; j < i; ++j) {
            BiomeBase biomebase = abiomebase[j];

            if (biomebase != null && biomebase.id < 128) {
                this.n.add(biomebase);
            }
        }

        this.n.remove(this.HELL);
        this.n.remove(this.SKY);
    }

    public BiomeBase getBiome(int i) {
        if (i >= 0 && i <= this.biomes.length) {
            return this.biomes[i];
        } else {
            aC.warn("Biome ID is out of bounds: " + i + ", defaulting to 0 (Ocean)");
            return this.OCEAN;
        }
    }
    
    public void initStaticFields() {
        if(!initDone) {
            initDone = true;
            BiomeBase.initStaticFields(this.biomes, this.n);
        }
    }
}
