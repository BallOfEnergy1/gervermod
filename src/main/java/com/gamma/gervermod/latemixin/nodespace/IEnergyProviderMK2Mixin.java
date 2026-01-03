package com.gamma.gervermod.latemixin.nodespace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.gamma.gervermod.util.TileAccessCache;

import api.hbm.energymk2.IEnergyConductorMK2;
import api.hbm.energymk2.IEnergyProviderMK2;
import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.energymk2.Nodespace;

@Mixin(IEnergyProviderMK2.class)
public interface IEnergyProviderMK2Mixin {

    /**
     * @author BallOfEnergy01
     * @reason Implement tweaks from main.
     */
    @Overwrite(remap = false)
    default void tryProvide(World world, int x, int y, int z, ForgeDirection dir) {
        TileEntity te = TileAccessCache.getTileOrCache(world, x, y, z);
        if (te instanceof IEnergyConductorMK2 con) {
            if (con.canConnect(dir.getOpposite())) {
                Nodespace.PowerNode node = Nodespace.getNode(world, x, y, z);
                if (node != null && node.net != null) {
                    node.net.addProvider((IEnergyProviderMK2) this);
                }
            }
        }

        if (te instanceof IEnergyReceiverMK2 rec && te != ((IEnergyProviderMK2) this)) {
            if (rec.canConnect(dir.getOpposite())) {
                long provides = Math
                    .min(((IEnergyProviderMK2) this).getPower(), ((IEnergyProviderMK2) this).getProviderSpeed());
                long receives = Math.min(rec.getMaxPower() - rec.getPower(), rec.getReceiverSpeed());
                long toTransfer = Math.min(provides, receives);
                toTransfer -= rec.transferPower(toTransfer);
                ((IEnergyProviderMK2) this).usePower(toTransfer);
            }
        }
    }
}
