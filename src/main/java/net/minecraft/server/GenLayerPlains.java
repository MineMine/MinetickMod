package net.minecraft.server;

public class GenLayerPlains extends GenLayer {

    public GenLayerPlains(long i, GenLayer genlayer) {
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

                if (this.a(57) == 0) {
                    //if (k1 == BiomeBase.PLAINS.id) {
                    if (k1 == BiomeIDEnum.PLAINS.id) { // Poweruser
                        //aint1[j1 + i1 * k] = BiomeBase.PLAINS.id + 128;
                        aint1[j1 + i1 * k] = BiomeIDEnum.PLAINS.id + 128; // Poweruser
                    } else {
                        aint1[j1 + i1 * k] = k1;
                    }
                } else {
                    aint1[j1 + i1 * k] = k1;
                }
            }
        }

        return aint1;
    }
}
