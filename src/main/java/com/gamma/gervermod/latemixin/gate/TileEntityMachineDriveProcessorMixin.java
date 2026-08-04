package com.gamma.gervermod.latemixin.gate;

import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.gamma.gervermod.gate.GateManager;
import com.hbm.dim.CelestialBody;
import com.hbm.items.ItemVOTVdrive;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.tileentity.machine.TileEntityMachineDriveProcessor;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

@Mixin(value = TileEntityMachineDriveProcessor.class, remap = false)
public abstract class TileEntityMachineDriveProcessorMixin extends TileEntityMachineBase {

    @Shadow
    public String status;

    @Shadow
    public boolean isProcessing;

    public TileEntityMachineDriveProcessorMixin(int slotCount) {
        super(slotCount);
    }

    @Definition(
        id = "isProcessing",
        field = "Lcom/hbm/tileentity/machine/TileEntityMachineDriveProcessor;isProcessing:Z")
    @Expression("this.isProcessing")
    @ModifyExpressionValue(method = "updateEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean expression(boolean original) {
        if (!original) return false;
        int tier = ItemVOTVdrive.getProcessingTier(this.slots[0], CelestialBody.getBody(this.worldObj));
        boolean allowed = switch (tier) {
            case 0 -> true;
            case 1 -> GateManager.TierGates.PLANET1.satisfiedHolder.get();
            case 2 -> GateManager.TierGates.PLANET2.satisfiedHolder.get();
            case 3 -> GateManager.TierGates.PLANET3.satisfiedHolder.get();
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
        if (!allowed) {
            this.status = EnumChatFormatting.RED + "Gated! ";
            this.isProcessing = false;
        }
        return allowed;
    }
}
