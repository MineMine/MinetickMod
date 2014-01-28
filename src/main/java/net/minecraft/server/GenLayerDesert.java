package net.minecraft.server;

public class GenLayerDesert extends GenLayer {

    public GenLayerDesert(long i, GenLayer genlayer) {
        super(i);
        this.a = genlayer;
    }

    public int[] a(int i, int j, int k, int l) {
        int[] aint = this.a.a(i - 1, j - 1, k + 2, l + 2);
        int[] aint1 = this.intCache.a(k * l); // Poweruser

        for (int i1 = 0; i1 < l; ++i1) {
            for (int j1 = 0; j1 < k; ++j1) {
                this.a((long) (j1 + i), (long) (i1 + j));
                int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];

                //if (!this.a(aint, aint1, j1, i1, k, k1, BiomeBase.EXTREME_HILLS.id, BiomeBase.SMALL_MOUNTAINS.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeBase.MESA_PLATEAU_F.id, BiomeBase.MESA.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeBase.MESA_PLATEAU.id, BiomeBase.MESA.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeBase.MEGA_TAIGA.id, BiomeBase.TAIGA.id)) {
                if (!this.a(aint, aint1, j1, i1, k, k1, BiomeIDEnum.EXTREME_HILLS.id, BiomeIDEnum.SMALL_MOUNTAINS.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeIDEnum.MESA_PLATEAU_F.id, BiomeIDEnum.MESA.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeIDEnum.MESA_PLATEAU.id, BiomeIDEnum.MESA.id) && !this.b(aint, aint1, j1, i1, k, k1, BiomeIDEnum.MEGA_TAIGA.id, BiomeIDEnum.TAIGA.id)) { // Poweruser
                    int l1;
                    int i2;
                    int j2;
                    int k2;

                    //if (k1 == BiomeBase.DESERT.id) {
                    if (k1 == BiomeIDEnum.DESERT.id) { // Poweruser
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        //if (l1 != BiomeBase.ICE_PLAINS.id && i2 != BiomeBase.ICE_PLAINS.id && j2 != BiomeBase.ICE_PLAINS.id && k2 != BiomeBase.ICE_PLAINS.id) {
                        if (l1 != BiomeIDEnum.ICE_PLAINS.id && i2 != BiomeIDEnum.ICE_PLAINS.id && j2 != BiomeIDEnum.ICE_PLAINS.id && k2 != BiomeIDEnum.ICE_PLAINS.id) { // Poweruser
                            aint1[j1 + i1 * k] = k1;
                        } else {
                            //aint1[j1 + i1 * k] = BiomeBase.EXTREME_HILLS_PLUS.id;
                            aint1[j1 + i1 * k] = BiomeIDEnum.EXTREME_HILLS_PLUS.id; // Poweruser
                        }
                    //} else if (k1 == BiomeBase.SWAMPLAND.id) {
                    } else if (k1 == BiomeIDEnum.SWAMPLAND.id) { // Poweruser
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                        //if (l1 != BiomeBase.DESERT.id && i2 != BiomeBase.DESERT.id && j2 != BiomeBase.DESERT.id && k2 != BiomeBase.DESERT.id && l1 != BiomeBase.COLD_TAIGA.id && i2 != BiomeBase.COLD_TAIGA.id && j2 != BiomeBase.COLD_TAIGA.id && k2 != BiomeBase.COLD_TAIGA.id && l1 != BiomeBase.ICE_PLAINS.id && i2 != BiomeBase.ICE_PLAINS.id && j2 != BiomeBase.ICE_PLAINS.id && k2 != BiomeBase.ICE_PLAINS.id) {
                            //if (l1 != BiomeBase.JUNGLE.id && k2 != BiomeBase.JUNGLE.id && i2 != BiomeBase.JUNGLE.id && j2 != BiomeBase.JUNGLE.id) {
                        if (l1 != BiomeIDEnum.DESERT.id && i2 != BiomeIDEnum.DESERT.id && j2 != BiomeIDEnum.DESERT.id && k2 != BiomeIDEnum.DESERT.id && l1 != BiomeIDEnum.COLD_TAIGA.id && i2 != BiomeIDEnum.COLD_TAIGA.id && j2 != BiomeIDEnum.COLD_TAIGA.id && k2 != BiomeIDEnum.COLD_TAIGA.id && l1 != BiomeIDEnum.ICE_PLAINS.id && i2 != BiomeIDEnum.ICE_PLAINS.id && j2 != BiomeIDEnum.ICE_PLAINS.id && k2 != BiomeIDEnum.ICE_PLAINS.id) { // Poweruser
                            if (l1 != BiomeIDEnum.JUNGLE.id && k2 != BiomeIDEnum.JUNGLE.id && i2 != BiomeIDEnum.JUNGLE.id && j2 != BiomeIDEnum.JUNGLE.id) { // Poweruser

                                aint1[j1 + i1 * k] = k1;
                            } else {
                                //aint1[j1 + i1 * k] = BiomeBase.JUNGLE_EDGE.id;
                                aint1[j1 + i1 * k] = BiomeIDEnum.JUNGLE_EDGE.id; // Poweruser
                            }
                        } else {
                            //aint1[j1 + i1 * k] = BiomeBase.PLAINS.id;
                            aint1[j1 + i1 * k] = BiomeIDEnum.PLAINS.id; // Poweruser
                        }
                    } else {
                        aint1[j1 + i1 * k] = k1;
                    }
                }
            }
        }

        return aint1;
    }

    private boolean a(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (!a(l, i1)) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (this.b(k1, i1) && this.b(l1, i1) && this.b(i2, i1) && this.b(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean b(int[] aint, int[] aint1, int i, int j, int k, int l, int i1, int j1) {
        if (l != i1) {
            return false;
        } else {
            int k1 = aint[i + 1 + (j + 1 - 1) * (k + 2)];
            int l1 = aint[i + 1 + 1 + (j + 1) * (k + 2)];
            int i2 = aint[i + 1 - 1 + (j + 1) * (k + 2)];
            int j2 = aint[i + 1 + (j + 1 + 1) * (k + 2)];

            if (a(k1, i1) && a(l1, i1) && a(i2, i1) && a(j2, i1)) {
                aint1[i + j * k] = l;
            } else {
                aint1[i + j * k] = j1;
            }

            return true;
        }
    }

    private boolean b(int i, int j) {
        if (a(i, j)) {
            return true;
        /*
        } else if (BiomeBase.getBiome(i) != null && BiomeBase.getBiome(j) != null) {
            EnumTemperature enumtemperature = BiomeBase.getBiome(i).m();
            EnumTemperature enumtemperature1 = BiomeBase.getBiome(j).m();
        */
        // Poweruser start
        } else if (this.biomeBaseObj.getBiome(i) != null && this.biomeBaseObj.getBiome(j) != null) {
            EnumTemperature enumtemperature = this.biomeBaseObj.getBiome(i).m();
            EnumTemperature enumtemperature1 = this.biomeBaseObj.getBiome(j).m();
        // Poweruser end

            return enumtemperature == enumtemperature1 || enumtemperature == EnumTemperature.MEDIUM || enumtemperature1 == EnumTemperature.MEDIUM;
        } else {
            return false;
        }
    }
}