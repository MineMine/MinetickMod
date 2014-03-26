package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class IntCache {

    // Poweruser start
    private static final ThreadLocal<IntCache> caches = new ThreadLocal<IntCache>() {
        @Override
        protected IntCache initialValue() {
            return new IntCache();
        }
    };

    // removed static
    private int a = 256;
    private List b = new ArrayList();
    private List c = new ArrayList();
    private List d = new ArrayList();
    private List e = new ArrayList();

    public static int[] a(int i) { // removed synchronized
        return caches.get().aOriginal(i);
    }

    public int[] aOriginal(int i) {
    // Poweruser end
        int[] aint;

        if (i <= 256) {
            if (b.isEmpty()) {
                aint = new int[256];
                c.add(aint);
                return aint;
            } else {
                aint = (int[]) b.remove(b.size() - 1);
                c.add(aint);
                return aint;
            }
        } else if (i > a) {
            a = i;
            d.clear();
            e.clear();
            aint = new int[a];
            e.add(aint);
            return aint;
        } else if (d.isEmpty()) {
            aint = new int[a];
            e.add(aint);
            return aint;
        } else {
            aint = (int[]) d.remove(d.size() - 1);
            e.add(aint);
            return aint;
        }
    }

    // Poweruser start
    public static void a() { // removed synchronized
        caches.get().aOriginal();
    }

    public void aOriginal() {
    // Poweruser end
        if (!d.isEmpty()) {
            d.remove(d.size() - 1);
        }

        if (!b.isEmpty()) {
            b.remove(b.size() - 1);
        }

        d.addAll(e);
        b.addAll(c);
        e.clear();
        c.clear();
    }

    public static synchronized String b() {
        //return "cache: " + d.size() + ", tcache: " + b.size() + ", allocated: " + e.size() + ", tallocated: " + c.size();
        return "Debug output currently not supported"; // Poweruser
    }
}
