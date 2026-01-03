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

@Mixin(IFluidReceiverMK2.class)
public interface IFluidReceiverMK2Mixin {

    /**
     * @author BallOfEnergy01
     * @reason Implement tweaks from main.
     */
    @SuppressWarnings("unchecked")
    @Overwrite(remap = false)
    default void trySubscribe(FluidType type, World world, int x, int y, int z, ForgeDirection dir) {
        TileEntity te = TileAccessCache.getTileOrCache(world, x, y, z);
        if (te instanceof IFluidConnectorMK2 con) {
            if (!con.canConnect(type, dir.getOpposite())) {
                return;
            }

            GenNode<?> node = UniNodespace.getNode(world, x, y, z, type.getNetworkProvider());
            if (node != null && node.net != null) {
                node.net.addReceiver(this);
            }
        }
    }
}
