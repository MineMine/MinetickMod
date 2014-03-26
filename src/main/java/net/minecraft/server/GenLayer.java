package net.minecraft.server;

import java.util.concurrent.Callable;

public abstract class GenLayer {

    private long c;
    protected GenLayer a;
    private long d;
    protected long b;

    // Poweruser start
    protected IntCache intCache;
    public void setIntCache(IntCache intCache) {
        this.intCache = intCache;
    }
    // Poweruser end

    //public static GenLayer[] a(long i, WorldType worldtype) {
    public static GenLayer[] a(long i, WorldType worldtype, IntCache intCache) { // Poweruser
        boolean flag = false;
        LayerIsland layerisland = new LayerIsland(1L);
        layerisland.setIntCache(intCache); // Poweruser
        GenLayerZoomFuzzy genlayerzoomfuzzy = new GenLayerZoomFuzzy(2000L, layerisland);
        genlayerzoomfuzzy.setIntCache(intCache); // Poweruser
        GenLayerIsland genlayerisland = new GenLayerIsland(1L, genlayerzoomfuzzy);
        genlayerisland.setIntCache(intCache); // Poweruser
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayerisland);
        genlayerzoom.setIntCache(intCache); // Poweruser

        genlayerisland = new GenLayerIsland(2L, genlayerzoom);
        genlayerisland.setIntCache(intCache); // Poweruser
        genlayerisland = new GenLayerIsland(50L, genlayerisland);
        genlayerisland.setIntCache(intCache); // Poweruser
        genlayerisland = new GenLayerIsland(70L, genlayerisland);
        genlayerisland.setIntCache(intCache); // Poweruser
        GenLayerIcePlains genlayericeplains = new GenLayerIcePlains(2L, genlayerisland);
        genlayericeplains.setIntCache(intCache); // Poweruser
        GenLayerTopSoil genlayertopsoil = new GenLayerTopSoil(2L, genlayericeplains);
        genlayertopsoil.setIntCache(intCache); // PoweruserS

        genlayerisland = new GenLayerIsland(3L, genlayertopsoil);
        genlayerisland.setIntCache(intCache); // Poweruser
        GenLayerSpecial genlayerspecial = new GenLayerSpecial(2L, genlayerisland, EnumGenLayerSpecial.COOL_WARM);
        genlayerspecial.setIntCache(intCache); // Poweruser

        genlayerspecial = new GenLayerSpecial(2L, genlayerspecial, EnumGenLayerSpecial.HEAT_ICE);
        genlayerspecial.setIntCache(intCache); // Poweruser
        genlayerspecial = new GenLayerSpecial(3L, genlayerspecial, EnumGenLayerSpecial.PUFFERFISH);
        genlayerspecial.setIntCache(intCache); // Poweruser
        genlayerzoom = new GenLayerZoom(2002L, genlayerspecial);
        genlayerzoom.setIntCache(intCache); // Poweruser
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        genlayerzoom.setIntCache(intCache); // Poweruser
        genlayerisland = new GenLayerIsland(4L, genlayerzoom);
        genlayerisland.setIntCache(intCache); // Poweruser
        GenLayerMushroomIsland genlayermushroomisland = new GenLayerMushroomIsland(5L, genlayerisland);
        genlayermushroomisland.setIntCache(intCache); // Poweruser
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayermushroomisland);
        genlayerdeepocean.setIntCache(intCache); // Poweruser
        GenLayer genlayer = GenLayerZoom.b(1000L, genlayerdeepocean, 0);
        genlayer.setIntCache(intCache); // Poweruser
        byte b0 = 4;

        if (worldtype == WorldType.LARGE_BIOMES) {
            b0 = 6;
        }

        if (flag) {
            b0 = 4;
        }

        GenLayer genlayer1 = GenLayerZoom.b(1000L, genlayer, 0);
        genlayer1.setIntCache(intCache); // Poweruser
        GenLayerCleaner genlayercleaner = new GenLayerCleaner(100L, genlayer1);
        genlayercleaner.setIntCache(intCache); // Poweruser
        //Object object = new GenLayerBiome(200L, genlayer, worldtype);
        GenLayer object = new GenLayerBiome(200L, genlayer, worldtype); // Poweruser
        object.setIntCache(intCache); // Poweruser

        if (!flag) {
            GenLayer genlayer2 = GenLayerZoom.b(1000L, (GenLayer) object, 2);
            genlayer2.setIntCache(intCache); // Poweruser

            object = new GenLayerDesert(1000L, genlayer2);
            object.setIntCache(intCache); // Poweruser
        }

        GenLayer genlayer3 = GenLayerZoom.b(1000L, genlayercleaner, 2);
        genlayer3.setIntCache(intCache); // Poweruser
        GenLayerRegionHills genlayerregionhills = new GenLayerRegionHills(1000L, (GenLayer) object, genlayer3);
        genlayerregionhills.setIntCache(intCache); // Poweruser

        genlayer1 = GenLayerZoom.b(1000L, genlayercleaner, 2);
        genlayer1.setIntCache(intCache); // Poweruser
        genlayer1 = GenLayerZoom.b(1000L, genlayer1, b0);
        genlayer1.setIntCache(intCache); // Poweruser
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer1);
        genlayerriver.setIntCache(intCache); // Poweruser
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        genlayersmooth.setIntCache(intCache); // Poweruser

        object = new GenLayerPlains(1001L, genlayerregionhills);
        object.setIntCache(intCache); // Poweruser

        for (int j = 0; j < b0; ++j) {
            object = new GenLayerZoom((long) (1000 + j), (GenLayer) object);
            object.setIntCache(intCache); // Poweruser
            if (j == 0) {
                object = new GenLayerIsland(3L, (GenLayer) object);
                object.setIntCache(intCache); // Poweruser
            }

            if (j == 1) {
                object = new GenLayerMushroomShore(1000L, (GenLayer) object);
                object.setIntCache(intCache); // Poweruser
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer) object);
        genlayersmooth1.setIntCache(intCache); // Poweruser
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        genlayerrivermix.setIntCache(intCache); // Poweruser
        GenLayerZoomVoronoi genlayerzoomvoronoi = new GenLayerZoomVoronoi(10L, genlayerrivermix);
        genlayerzoomvoronoi.setIntCache(intCache); // Poweruser

        genlayerrivermix.a(i);
        genlayerzoomvoronoi.a(i);
        return new GenLayer[] { genlayerrivermix, genlayerzoomvoronoi, genlayerrivermix};
    }

    public GenLayer(long i) {
        this.b = i;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += i;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += i;
        this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
        this.b += i;
    }

    public void a(long i) {
        this.c = i;
        if (this.a != null) {
            this.a.a(i);
        }

        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
        this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
        this.c += this.b;
    }

    public void a(long i, long j) {
        this.d = this.c;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += i;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += j;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += i;
        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += j;
    }

    protected int a(int i) {
        int j = (int) ((this.d >> 24) % (long) i);

        if (j < 0) {
            j += i;
        }

        this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
        this.d += this.c;
        return j;
    }

    public abstract int[] a(int i, int j, int k, int l);

    protected static boolean a(int i, int j) {
        if (i == j) {
            return true;
        } else if (i != BiomeBase.MESA_PLATEAU_F.id && i != BiomeBase.MESA_PLATEAU.id) {
            try {
                return BiomeBase.getBiome(i) != null && BiomeBase.getBiome(j) != null ? BiomeBase.getBiome(i).a(BiomeBase.getBiome(j)) : false;
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Comparing biomes");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Biomes being compared");

                crashreportsystemdetails.a("Biome A ID", Integer.valueOf(i));
                crashreportsystemdetails.a("Biome B ID", Integer.valueOf(j));
                crashreportsystemdetails.a("Biome A", (Callable) (new CrashReportGenLayer1(i)));
                crashreportsystemdetails.a("Biome B", (Callable) (new CrashReportGenLayer2(j)));
                throw new ReportedException(crashreport);
            }
        } else {
            return j == BiomeBase.MESA_PLATEAU_F.id || j == BiomeBase.MESA_PLATEAU.id;
        }
    }

    protected static boolean b(int i) {
        return i == BiomeBase.OCEAN.id || i == BiomeBase.DEEP_OCEAN.id || i == BiomeBase.FROZEN_OCEAN.id;
    }

    protected int a(int... aint) {
        return aint[this.a(aint.length)];
    }

    protected int b(int i, int j, int k, int l) {
        return j == k && k == l ? j : (i == j && i == k ? i : (i == j && i == l ? i : (i == k && i == l ? i : (i == j && k != l ? i : (i == k && j != l ? i : (i == l && j != k ? i : (j == k && i != l ? j : (j == l && i != k ? j : (k == l && i != j ? k : this.a(new int[] { i, j, k, l}))))))))));
    }
}
