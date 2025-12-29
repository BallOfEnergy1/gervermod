package com.gamma.gervermod.latemixin.structworld;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gamma.gervermod.dim.struct.StructDimHandler;
import com.hbm.config.GeneralConfig;
import com.hbm.lib.HbmWorldGen;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(HbmWorldGen.class)
public abstract class HbmWorldGenMixin {

    @Redirect(
        method = "generate",
        at = @At(
            value = "FIELD",
            target = "Lcom/hbm/config/GeneralConfig;enableMDOres:Z",
            opcode = Opcodes.GETSTATIC,
            remap = false),
        remap = false)
    private boolean redirectedFieldAccess(@Local(name = "world") World world) {
        if (world.provider.dimensionId == StructDimHandler.structDim) return true;
        else return GeneralConfig.enableMDOres;
    }

    @Redirect(
        method = "generateSurface",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/WorldProvider;dimensionId:I",
            opcode = Opcodes.GETFIELD,
            remap = false),
        remap = false)
    private int redirectedFieldAccess(WorldProvider instance) {
        if (instance.dimensionId == StructDimHandler.structDim) return 0;
        else return instance.dimensionId;
    }

    @ModifyConstant(method = "generateSurface", constant = @Constant(intValue = 2000), remap = false)
    private int changeLampConstant(int constant) {
        return constant * 10; // 10x rarer.
    }
}
