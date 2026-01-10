package com.gamma.gervermod.latemixin.fixes;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.hbm.extprop.HbmLivingProps;
import com.hbm.handler.EntityEffectHandler;

@Mixin(EntityEffectHandler.class)
public abstract class EntityEffectHandlerMixin {

    /**
     * @author BallOfEnergy01
     * @reason Remove MKU effects entirely.
     */
    @Overwrite(remap = false)
    private static void handleContagion(EntityLivingBase entity) {
        if (HbmLivingProps.getContagion(entity) != 0) {
            HbmLivingProps.setContagion(entity, 0);
            if (entity instanceof EntityPlayer player) player.addChatComponentMessage(
                new ChatComponentText(
                    EnumChatFormatting.ITALIC + "MKU infection prevented!" + EnumChatFormatting.RESET));
        }
    }
}
