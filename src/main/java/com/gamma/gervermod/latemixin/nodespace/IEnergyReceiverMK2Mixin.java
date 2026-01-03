package com.gamma.gervermod.latemixin.nodespace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.gamma.gervermod.util.TileAccessCache;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.energymk2.Nodespace;

@Mixin(IEnergyReceiverMK2.class)
public interface IEnergyReceiverMK2Mixin {

    /**
     * @author BallOfEnergy01
     * @reason Implement tweaks from main.
     */
    @Overwrite(remap = false)
    default void trySubscribe(World world, int x, int y, int z, ForgeDirection dir) {
        TileEntity te = TileAccessCache.getTileOrCache(world, x, y, z);
        if (te instanceof IEnergyConductorMK2 con) {
            if (!con.canConnect(dir.getOpposite())) {
                return;
            }

            Nodespace.PowerNode node = Nodespace.getNode(world, x, y, z);
            if (node != null && node.net != null) {
                node.net.addReceiver(((IEnergyReceiverMK2) this));
            }
        }
    }
}
