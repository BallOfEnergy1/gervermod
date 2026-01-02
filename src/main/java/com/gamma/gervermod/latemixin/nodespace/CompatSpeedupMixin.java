package com.gamma.gervermod.latemixin.nodespace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.gamma.gervermod.util.NodespaceSpeedup;
import com.hbm.util.Compat;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

@Mixin(Compat.class)
public abstract class CompatSpeedupMixin {

    @WrapMethod(method = "getTileStandard", remap = false)
    private static TileEntity wrapped(World world, int x, int y, int z, Operation<TileEntity> original) {
        if (NodespaceSpeedup.isWrapped) return !world.getChunkProvider()
            .chunkExists(x >> 4, z >> 4) ? null : world.getTileEntity(x, y, z);

        return NodespaceSpeedup.getTE(x, y, z, world);
    }
}
