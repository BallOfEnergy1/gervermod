package com.gamma.gervermod.accessors;

import net.minecraft.tileentity.TileEntity;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

public interface ChunkAccessor {

    Short2ObjectMap<TileEntity> gervermod$getTileEntityMap();
}
