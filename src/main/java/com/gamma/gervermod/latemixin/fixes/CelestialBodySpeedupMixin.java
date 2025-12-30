package com.gamma.gervermod.latemixin.fixes;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.dim.CelestialBody;
import com.hbm.dim.trait.CelestialBodyTrait;

@Mixin(CelestialBody.class)
public abstract class CelestialBodySpeedupMixin {

    @Shadow(remap = false)
    private HashMap<Class<? extends CelestialBodyTrait>, CelestialBodyTrait> traits;

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("TAIL"), remap = false)
    private void getWorlds(String name, CallbackInfo ci) {
        this.traits = new LinkedHashMap<>();
    }
}
