package com.gamma.gervermod.latemixin.fixes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.BufPacket;
import com.hbm.tileentity.IBufPacketReceiver;
import com.hbm.tileentity.TileEntityLoadedBase;

import api.hbm.tile.ILoadedTile;
import io.netty.buffer.ByteBuf;

@Mixin(TileEntityLoadedBase.class)
public abstract class HBMNetworkSpeedupMixin extends TileEntity implements ILoadedTile, IBufPacketReceiver {

    @Unique
    private int gervermod$lastHashcode = -1;

    /**
     * @author BallOfEnergy01
     * @reason Performance fixes.
     */
    @Overwrite(remap = false)
    public void networkPackNT(int range) {
        if (!this.worldObj.isRemote) {

            Set<EntityPlayerMP> toSendTo = new HashSet<>();

            for (Object obj : worldObj.playerEntities) {
                EntityPlayerMP player = (EntityPlayerMP) obj;
                if (player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= range * range)
                    toSendTo.add(player);
            }

            if (toSendTo.isEmpty()) return;

            BufPacket packet = new BufPacket(this.xCoord, this.yCoord, this.zCoord, this);
            ByteBuf preBuf = packet.getCompiledBuffer();
            int bufferHash = preBuf.hashCode();

            if (gervermod$lastHashcode != -1 && bufferHash == gervermod$lastHashcode
                && worldObj.getTotalWorldTime() % 20 != 0) return;

            this.gervermod$lastHashcode = bufferHash;
            for (EntityPlayerMP player : toSendTo) {
                PacketDispatcher.wrapper.sendTo(packet, player);
                // MainRegistry.logger.info("Sent TE data in chunk ({}, {}) to {}", this.xCoord >> 4, this.zCoord >> 4,
                // player.getCommandSenderName());
            }
        }
    }
}
