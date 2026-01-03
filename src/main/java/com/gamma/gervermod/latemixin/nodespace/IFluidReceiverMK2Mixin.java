package com.gamma.gervermod.latemixin.nodespace;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gamma.gervermod.util.TileAccessCache;

import api.hbm.fluidmk2.IFluidReceiverMK2;

@Mixin(IFluidReceiverMK2.class)
public abstract class IFluidReceiverMK2Mixin {

    @Redirect(
        method = "trySubscribe(Lcom/hbm/inventory/fluid/FluidType;Lnet/minecraft/world/World;IIILnet/minecraftforge/common/util/ForgeDirection;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/hbm/util/Compat;getTileStandard(Lnet/minecraft/world/World;III)Lnet/minecraft/tileentity/TileEntity;",
            remap = false),
        remap = false)
    private TileEntity redirected(World world, int x, int y, int z) {
        return TileAccessCache.getTileOrCache(world, x, y, z);
    }
}
