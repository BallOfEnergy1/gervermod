package com.gamma.gervermod.mixin.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gamma.gervermod.accessors.ChunkAccessor;
import com.llamalad7.mixinextras.sugar.Local;

import cpw.mods.fml.common.FMLLog;

@Mixin(AnvilChunkLoader.class)
public abstract class AnvilChunkLoaderMixin {

    @Inject(
        method = "saveChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/storage/AnvilChunkLoader;writeChunkToNBT(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V",
            shift = At.Shift.AFTER))
    private void inject(World p_75816_1_, Chunk chunk, CallbackInfo ci, @Local(ordinal = 1) NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("TileEntities", 0);
        for (TileEntity tileentity : ((ChunkAccessor) chunk).gervermod$getTileEntityMap()
            .values()) {
            NBTTagCompound tileEntityNBT = new NBTTagCompound();
            try {
                tileentity.writeToNBT(tileEntityNBT);
                list.appendTag(tileEntityNBT);
            } catch (Exception e) {
                FMLLog.log(
                    Level.ERROR,
                    e,
                    "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                    tileentity.getClass()
                        .getName());
            }
        }
    }
}
