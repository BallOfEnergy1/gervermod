package com.gamma.gervermod.mixin.fixes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gamma.gervermod.accessors.ChunkAccessor;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {

    @Inject(
        method = "handleMapChunkBulk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/Chunk;fillChunk([BIIZ)V",
            shift = At.Shift.AFTER))
    private void injected(S26PacketMapChunkBulk packetIn, CallbackInfo ci, @Local Chunk chunk) {
        gervermod$doInject(chunk);
    }

    @Inject(
        method = "handleChunkData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/Chunk;fillChunk([BIIZ)V",
            shift = At.Shift.AFTER))
    private void injected(S21PacketChunkData packetIn, CallbackInfo ci, @Local Chunk chunk) {
        gervermod$doInject(chunk);
    }

    @Unique
    private void gervermod$doInject(@Local Chunk chunk) {
        List<TileEntity> invalidList = new ArrayList<>();
        for (TileEntity tileentity : ((ChunkAccessor) chunk).gervermod$getTileEntityMap()
            .values()) {
            int x = tileentity.xCoord & 15;
            int y = tileentity.yCoord;
            int z = tileentity.zCoord & 15;
            Block block = tileentity.getBlockType();
            if ((block != chunk.getBlock(x, y, z) || tileentity.blockMetadata != chunk.getBlockMetadata(x, y, z))
                && tileentity.shouldRefresh(
                    block,
                    chunk.getBlock(x, y, z),
                    tileentity.blockMetadata,
                    chunk.getBlockMetadata(x, y, z),
                    chunk.worldObj,
                    x,
                    y,
                    z)) {
                invalidList.add(tileentity);
            }
            tileentity.updateContainingBlockInfo();
        }

        for (TileEntity te : invalidList) {
            te.invalidate();
        }
    }
}
