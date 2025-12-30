package com.gamma.gervermod.mixin.fixes;

import java.util.Map;

import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@Mixin(MapStorage.class)
public abstract class MapStorageSpeedupMixin {

    @Shadow
    private Map loadedDataMap;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injected(ISaveHandler p_i2162_1_, CallbackInfo ci) {
        this.loadedDataMap = new Object2ObjectOpenHashMap<>();
    }
}
