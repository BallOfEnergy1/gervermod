package com.gamma.gervermod.util;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import net.minecraft.world.World;

public class WorldPos extends BlockPos {

    private World world;

    public WorldPos() {}

    public void set(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World world() {
        return world;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && world == ((WorldPos) obj).world;
    }

    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + world.provider.dimensionId;
    }
}
