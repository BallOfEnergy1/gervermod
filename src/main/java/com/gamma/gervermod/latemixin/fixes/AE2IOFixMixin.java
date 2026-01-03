package com.gamma.gervermod.latemixin.fixes;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gamma.gervermod.accessors.AE2IOFixAccessor;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * This is so fucking stupid.
 * Opening a new file handle for every single chunk generated.
 * No wonder this mod has so many bugs. It fucking sucks.
 */
@Mixin(targets = "appeng.core.worlddata.SpawnData")
public abstract class AE2IOFixMixin implements AE2IOFixAccessor {

    @Unique
    private static final IntFunction<Long2ObjectMap<NBTTagCompound>> gervermod$newMapSupplier = (
        ignored) -> Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
    @Unique
    private static final Int2ObjectMap<Long2ObjectMap<NBTTagCompound>> gervermod$concurrentCache = Int2ObjectMaps
        .synchronize(new Int2ObjectOpenHashMap<>());
    // We don't need to keep the cache forever.
    // This clears the cache every so often, as chunks that have had their neighbors generated don't need to be in this
    // cache anymore.
    // This clears every 60 seconds.
    @Unique
    private static final AtomicInteger gervermod$clearCounter = new AtomicInteger();
    @Unique
    private static final int gervermod$CLEAR_INTERVAL = 60 * 20;

    @Inject(method = "writeSpawnData", at = @At("HEAD"), remap = false)
    private void injected(int dim, int chunkX, int chunkZ, NBTTagCompound data, CallbackInfo ci) {
        Long2ObjectMap<NBTTagCompound> dimMap = gervermod$concurrentCache
            .computeIfAbsent(dim, gervermod$newMapSupplier);
        dimMap.put(gervermod$packCoords(chunkX, chunkZ), data);
    }

    @WrapMethod(method = "loadSpawnData", remap = false)
    private NBTTagCompound wrapped(int dim, int chunkX, int chunkZ, Operation<NBTTagCompound> original) {
        Long2ObjectMap<NBTTagCompound> dimMap = gervermod$concurrentCache
            .computeIfAbsent(dim, gervermod$newMapSupplier);
        long packed = gervermod$packCoords(chunkX, chunkZ);
        NBTTagCompound data = dimMap.get(packed);
        if (data == null) {
            data = original.call(dim, chunkX, chunkZ);
            dimMap.put(packed, data);
        }

        return data;
    }

    @Unique
    private long gervermod$packCoords(int x, int z) {
        return (long) (x >> 4) | (long) (z >> 4) << 32;
    }

    @Override
    public void gervermod$tickCache() {
        if (gervermod$clearCounter.getAndIncrement() >= gervermod$CLEAR_INTERVAL) {
            gervermod$clearCounter.set(0);
            gervermod$concurrentCache.values()
                .forEach(Long2ObjectMap::clear);
        }
    }
}
