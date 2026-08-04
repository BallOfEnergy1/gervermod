package com.gamma.gervermod.latemixin.fixes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.hbm.handler.ArmorModHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@Mixin(ArmorModHandler.class)
public abstract class ArmorModHandlerSpeedupMixin {

    @Unique
    private static final Object2ObjectMap<NBTTagCompound, ItemStack[]> gervermod$cache = new Object2ObjectOpenHashMap<>();

    @Inject(method = "pryMods", at = @At("HEAD"), remap = false, cancellable = true)
    private static void injectedHead(ItemStack armor, CallbackInfoReturnable<ItemStack[]> cir) {
        if (armor == null) {
            gervermod$cache.clear();
            cir.setReturnValue(null);
        }
    }

    @Inject(
        method = "pryMods",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;",
            shift = At.Shift.BY,
            by = 2),
        cancellable = true)
    private static void injectedInside(ItemStack armor, CallbackInfoReturnable<ItemStack[]> cir,
        @Local(name = "nbt") NBTTagCompound nbt, @Share("stacks") LocalRef<ItemStack[]> stacks) {
        stacks.set(gervermod$cache.get(nbt));
        if (stacks.get() != null) {
            cir.setReturnValue(stacks.get());
        }
    }

    @Inject(method = "pryMods", at = @At(value = "RETURN", ordinal = 1, shift = At.Shift.BY, by = -6), remap = false)
    private static void injectedReturn(ItemStack armor, CallbackInfoReturnable<ItemStack[]> cir,
        @Local(name = "nbt") NBTTagCompound nbt, @Share("stacks") LocalRef<ItemStack[]> stacks) {
        gervermod$cache.put(nbt, stacks.get());
    }
}
