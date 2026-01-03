package com.gamma.gervermod.dim.struct.providers;

import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public interface IStructWorldProvider {

    void nextSeed();

    BiomeGenBase getBiome();

    Block[] getBlocks();

    String getWorldType();
}
