package com.gamma.gervermod.mixin.fixes;

import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@Mixin(Chunk.class)
public abstract class ChunkSpeedupMixin {

    @Shadow
    public Map<ChunkPosition, TileEntity> chunkTileEntityMap;

    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At("TAIL"), remap = false)
    private void injected(CallbackInfo ci) {
        chunkTileEntityMap = new Object2ObjectOpenHashMap<>();
    }
}
