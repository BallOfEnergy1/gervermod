package com.gamma.gervermod.accessors;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.tileentity.TileEntity;

public interface ChunkAccessor {
    Short2ObjectMap<TileEntity> gervermod$getTileEntityMap();
}
