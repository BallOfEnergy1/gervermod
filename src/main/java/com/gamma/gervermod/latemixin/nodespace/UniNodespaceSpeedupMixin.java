package com.gamma.gervermod.latemixin.nodespace;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.hbm.uninos.NodeNet;
import com.hbm.uninos.UniNodespace;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

@Mixin(UniNodespace.class)
public abstract class UniNodespaceSpeedupMixin {

    @Shadow(remap = false)
    public static Set<NodeNet<?, ?, ?>> activeNodeNets;

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void injected(CallbackInfo ci) {
        activeNodeNets = new ObjectOpenHashSet<>();
    }

    /**
     * @author BallOfEnergy01
     * @reason Performance fixes.
     */
    @Overwrite(remap = false)
    private static void updateNetworks() {
        Collection<NodeNet<?, ?, ?>> toRemove = new ObjectArrayList<>();

        // more testing needed for parallelization.
        Stream<NodeNet<?, ?, ?>> stream = activeNodeNets.stream()/* .parallel() */;

        stream.forEach((net) -> {
            if (net.links.isEmpty()) {
                toRemove.add(net);
                return;
            }
            net.resetTrackers();
            net.update();
        });

        // the `toRemove` is very likely to be smaller than the `activeNodeNets`.
        for (NodeNet<?, ?, ?> nodeNet : toRemove) {
            activeNodeNets.remove(nodeNet);
        }
    }
}
