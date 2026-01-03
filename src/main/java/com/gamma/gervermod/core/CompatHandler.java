package com.gamma.gervermod.core;

import com.gamma.gervermod.dim.struct.StructDimHandler;
import com.hbm.world.gen.nbt.NBTStructure;
import com.hbm.world.gen.nbt.SpawnCondition;

public class CompatHandler {

    public static void onPreInit() {
        for (String s : NBTStructure.listStructures()) {
            SpawnCondition condition = NBTStructure.getStructure(s);
            for (int dimID : StructDimHandler.allDims.keySet()) {
                SpawnCondition newCondition = new SpawnCondition(s + dimID) {

                    {
                        this.maxHeight = condition.maxHeight;
                        this.minHeight = condition.minHeight;
                        this.structure = condition.structure;
                        this.pools = condition.pools;
                        this.startPool = condition.startPool;
                        this.spawnWeight = condition.spawnWeight;
                        this.checkCoordinates = (WorldCoordinate worldCoordinate) -> {
                            int x = worldCoordinate.coords.chunkXPos * 16;
                            int z = worldCoordinate.coords.chunkZPos * 16;
                            if (!condition.canSpawn.test(worldCoordinate.world.getBiomeGenForCoords(x, z)))
                                return false;

                            int frequency = Integer.valueOf(
                                worldCoordinate.world.getGameRules()
                                    .getGameRuleStringValue("structureFrequency" + dimID))
                                .intValue();

                            return worldCoordinate.rand.nextInt(frequency) == 0;
                        };
                    }
                };
                NBTStructure.registerStructure(dimID, newCondition);
            }
        }
    }
}
