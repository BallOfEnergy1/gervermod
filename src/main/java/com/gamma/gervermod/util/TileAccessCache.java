package com.gamma.gervermod.util;

import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.hbm.util.Compat;

import api.hbm.tile.ILoadedTile;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class TileAccessCache {

    public static Map<IntQuartet, TileAccessCache> cache = new Object2ObjectOpenHashMap<>();
    public static int NULL_CACHE = 20;
    public static int NONNULL_CACHE = 60;
    public TileEntity tile;
    public long expiresOn;

    public TileAccessCache(TileEntity tile, long expiresOn) {
        this.tile = tile;
        this.expiresOn = expiresOn;
    }

    public boolean hasExpired(long worldTime) {
        if (tile != null && tile.isInvalid()) return true;
        if (worldTime >= expiresOn) return true;
        return tile instanceof ILoadedTile && !((ILoadedTile) tile).isLoaded();
    }

    // `publicCumRag` in main
    public static IntQuartet mutableState = new IntQuartet();

    public static TileEntity getTileOrCache(World world, int x, int y, int z) {
        mutableState.mangle(x, y, z, world.provider.dimensionId);
        TileAccessCache cache = TileAccessCache.cache.get(mutableState);
        long worldTime = world.getTotalWorldTime();
        if (cache == null || cache.hasExpired(worldTime)) {
            TileEntity tile = Compat.getTileStandard(world, x, y, z);
            cache = new TileAccessCache(tile, worldTime + (tile == null ? NULL_CACHE : NONNULL_CACHE));
            TileAccessCache.cache.put(mutableState.clone(), cache);
            return tile;
        } else {
            return cache.tile;
        }
    }

    /// flavor town ///
    public static class IntQuartet implements Cloneable {

        public int x, y, z, w;

        public void mangle(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            result = prime * result + w;
            return result;
        }

        @Override
        public IntQuartet clone() {
            try {
                IntQuartet clone = (IntQuartet) super.clone();
                clone.mangle(x, y, z, w);
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
