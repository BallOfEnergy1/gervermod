package com.gamma.gervermod.latemixin.nodespace;

import java.util.Map;
import java.util.Set;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.uninos.NodeNet;
import com.hbm.uninos.UniNodespace;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

@Mixin(UniNodespace.class)
public abstract class UniNodespaceSpeedupMixin {

    @Shadow(remap = false)
    public static Set<NodeNet<?, ?, ?>> activeNodeNets;
    @Shadow(remap = false)
    public static Map<World, UniNodespace.UniNodeWorld> worlds;

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void injected(CallbackInfo ci) {
        activeNodeNets = new ObjectOpenHashSet<>();
        worlds = new Object2ObjectOpenHashMap<>();
    }
}
