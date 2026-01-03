package com.gamma.gervermod.latemixin.nodespace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.gamma.gervermod.util.TileAccessCache;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.uninos.GenNode;
import com.hbm.uninos.UniNodespace;

import api.hbm.fluidmk2.IFluidConnectorMK2;
import api.hbm.fluidmk2.IFluidReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardSenderMK2;

@Mixin(IFluidStandardSenderMK2.class)
public interface IFluidStandardSenderMK2Mixin {

    /**
     * @author BallOfEnergy01
     * @reason Implement tweaks from main.
     */
    @SuppressWarnings("unchecked")
    @Overwrite(remap = false)
    default void tryProvide(FluidType type, int pressure, World world, int x, int y, int z, ForgeDirection dir) {
        TileEntity te = TileAccessCache.getTileOrCache(world, x, y, z);
        if (te instanceof IFluidConnectorMK2 con) {
            if (con.canConnect(type, dir.getOpposite())) {
                GenNode<?> node = UniNodespace.getNode(world, x, y, z, type.getNetworkProvider());
                if (node != null && node.net != null) {
                    node.net.addProvider(this);
                }
            }
        }

        if (te instanceof IFluidReceiverMK2 rec && te != ((IFluidStandardSenderMK2) this)) {
            if (rec.canConnect(type, dir.getOpposite())) {
                long provides = Math.min(
                    ((IFluidStandardSenderMK2) this).getFluidAvailable(type, pressure),
                    ((IFluidStandardSenderMK2) this).getProviderSpeed(type, pressure));
                long receives = Math.min(rec.getDemand(type, pressure), rec.getReceiverSpeed(type, pressure));
                long toTransfer = Math.min(provides, receives);
                toTransfer -= rec.transferFluid(type, pressure, toTransfer);
                ((IFluidStandardSenderMK2) this).useUpFluid(type, pressure, toTransfer);
            }
        }
    }
}
