package com.gamma.gervermod.util;

import com.hbm.util.Compat;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NodespaceSpeedup {

    public static boolean isWrapped = false;

    private static final WorldPos gervermod$mutableBlockPosInstance = new WorldPos();

    private static final Object2ObjectMap<WorldPos, TileEntity> gervermod$teCache = new Object2ObjectOpenHashMap<>();

    private static final Object2ObjectFunction<WorldPos, TileEntity> gervermod$MAPPER = (Object obj) -> {
        WorldPos pos = (WorldPos) obj;
        return Compat.getTileStandard(pos.world(), pos.x(), pos.y(), pos.z());
    };

    public static TileEntity getTE(int x, int y, int z, World world) {
        gervermod$mutableBlockPosInstance.set(x, y, z, world);
        isWrapped = true;
        try {
            return gervermod$teCache.computeIfAbsent(gervermod$mutableBlockPosInstance, gervermod$MAPPER);
        } finally {
            isWrapped = false;
        }
    }

    public static void invalidate(TileEntity te) {
        gervermod$mutableBlockPosInstance.set(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj());
        gervermod$teCache.remove(gervermod$mutableBlockPosInstance);
    }
}
