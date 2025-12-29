package com.gamma.gervermod.dim.struct;

import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class StructDimTeleporter extends Teleporter {

    private final WorldServer world;

    public StructDimTeleporter(final WorldServer world) {
        super(world);
        this.world = world;
    }

    @Override
    public void placeInPortal(final Entity par1Entity, double par2, double par4, double par6, float par8) {
        WorldServer world = this.world;
        int[] spawn = getSpawn(world);
        par1Entity.setLocationAndAngles(spawn[0], spawn[1], spawn[2], par1Entity.rotationYaw, 0.0F);
        par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
    }

    private int[] getSpawn(WorldServer world) {
        ChunkCoordinates spawnpoint = world.getSpawnPoint();

        while (world.getBlock(spawnpoint.posX, spawnpoint.posY, spawnpoint.posZ)
            .isNormalCube()) {
            spawnpoint.posY += 2;
        }

        return new int[] { spawnpoint.posX, spawnpoint.posY, spawnpoint.posZ };
    }

    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
        return false;
    }

    @Override
    public boolean makePortal(Entity par1Entity) {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long par1) {

    }
}
