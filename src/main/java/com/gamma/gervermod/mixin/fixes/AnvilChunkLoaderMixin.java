package com.gamma.gervermod.mixin.fixes;

import java.util.Collection;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gamma.gervermod.accessors.ChunkAccessor;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin {

    @Redirect(
        method = "writeChunkToNBT",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"))
    private Collection<TileEntity> redirect(Map instance, @Local(argsOnly = true) Chunk chunk) {
        return ((ChunkAccessor) chunk).gervermod$getTileEntityMap()
            .values();
    }

}
