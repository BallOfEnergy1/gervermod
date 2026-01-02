package com.gamma.gervermod.mixin.fixes;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gamma.gervermod.util.NodespaceSpeedup;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin {

    @Unique
    private final TileEntity gervermod$this = (TileEntity) (Object) this;

    @Inject(method = "invalidate", at = @At("HEAD"))
    private void injected(CallbackInfo ci) {
        NodespaceSpeedup.invalidate(gervermod$this);
    }
}
