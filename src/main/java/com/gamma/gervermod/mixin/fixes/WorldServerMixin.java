package com.gamma.gervermod.mixin.fixes;

import com.gamma.gervermod.accessors.ChunkAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Map;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin {

    @Redirect(
        method = "func_147486_a",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;values()Ljava/util/Collection;"
        )
    )
    private Collection<TileEntity> redirect(Map instance, @Local Chunk chunk) {
        return ((ChunkAccessor) chunk).gervermod$getTileEntityMap().values();
    }

}
