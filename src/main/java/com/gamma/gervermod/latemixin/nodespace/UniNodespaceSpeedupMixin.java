package com.gamma.gervermod.latemixin.nodespace;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private static int gervermod$reapTimer = 0;

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void injected(CallbackInfo ci) {
        activeNodeNets = new ObjectOpenHashSet<>();
        // Fixed in main, waiting for release.
        // worlds = new Object2ObjectOpenHashMap<>();
    }

    /**
     * @author BallOfEnergy01
     * @reason Performance fixes.
     */
    @Overwrite(remap = false)
    private static void updateNetworks() {
        // Must initialize due to stream lambda.
        Collection<NodeNet<?, ?, ?>> toRemove = new ObjectArrayList<>();

        // more testing needed for parallelization.
        Stream<NodeNet<?, ?, ?>> stream = activeNodeNets.stream()/* .parallel() */;
        stream.forEach((net) -> {
            // Net reaping fixes from main
            // BEGIN
            if (gervermod$reapTimer <= 0) {
                net.links.removeIf((link) -> link.expired);
                if (net.links.isEmpty()) {
                    toRemove.add(net);
                    return;
                }
            }
            // END
            net.resetTrackers();
            net.update();
        });

        if (gervermod$reapTimer <= 0) {
            // The `toRemove` is very likely to be smaller than the `activeNodeNets`.
            // Also, it's faster to iterate over (it's a list).
            for (NodeNet<?, ?, ?> nodeNet : toRemove) {
                activeNodeNets.remove(nodeNet);
            }
        }

        // This is technically at the end of the `updateNodespace()` function in main,
        // but it's more performant here than injecting. It also does the same thing.
        gervermod$updateReapTimer();
    }

    @Unique
    private static void gervermod$updateReapTimer() {
        if (gervermod$reapTimer <= 0) gervermod$reapTimer = 5 * 60 * 20; // 5 minutes is more than plenty
        else gervermod$reapTimer--;
    }
}
