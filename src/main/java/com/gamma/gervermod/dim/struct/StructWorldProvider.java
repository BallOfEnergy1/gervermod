package com.gamma.gervermod.dim.struct;

import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

import com.hbm.dim.WorldProviderCelestial;

public class StructWorldProvider extends WorldProviderCelestial {

    public StructWorldProvider() {

    }

    private static final Random rand = new Random();
    private long seed = rand.nextLong();

    void nextSeed() {
        seed = rand.nextLong();
    }

    @Override
    public void registerWorldChunkManager() {
        super.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.0F);
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new FastChunkProviderFlat(this.worldObj);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public String getDimensionName() {
        return "Structure World";
    }

    @Override
    public boolean canSnowAt(final int x, final int y, final int z, final boolean checkLight) {
        return false;
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(0, 71, 0);
    }

    @Override
    public boolean isBlockHighHumidity(final int x, final int y, final int z) {
        return false;
    }
}
