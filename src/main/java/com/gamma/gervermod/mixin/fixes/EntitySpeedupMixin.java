package com.gamma.gervermod.mixin.fixes;

import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.IExtendedEntityProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySpeedupMixin {

    @Shadow(remap = false)
    protected HashMap<String, IExtendedEntityProperties> extendedProperties;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void injected(CallbackInfo ci) {
        extendedProperties = new LinkedHashMap<>();
    }
}
