package com.gamma.gervermod.latemixin.gate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.hbm.entity.EntityMappings;

@Mixin(value = EntityMappings.class, remap = false)
public abstract class MixinEntityMappings {

    @Redirect(
        method = "writeMappings",
        at = @At(
            value = "INVOKE",
            target = "Lcom/hbm/entity/EntityMappings;addSpawn(Ljava/lang/Class;IIILnet/minecraft/entity/EnumCreatureType;[Lnet/minecraft/world/biome/BiomeGenBase;)V",
            ordinal = 3))
    private static void noop(Class<? extends EntityLiving> entityClass, int weightedProb, int min, int max,
        EnumCreatureType typeOfCreature, BiomeGenBase[] biomes) {
        // NOOP
    }
}
