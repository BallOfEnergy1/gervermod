package com.gamma.gervermod.dim.struct.providers.hbm;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class StructWorldPlainsProviderHBM extends AbstractStructWorldProviderHBM {

    @Override
    public BiomeGenBase getBiome() {
        return BiomeGenBase.plains;
    }

    @Override
    public Block[] getBlocks() {
        return new Block[] { Blocks.grass, Blocks.dirt, Blocks.stone };
    }

    @Override
    public String getWorldType() {
        return "Plains";
    }
}
