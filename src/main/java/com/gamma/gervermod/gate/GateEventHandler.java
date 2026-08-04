package com.gamma.gervermod.gate;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.BlockEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.hbm.blocks.ModBlocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class GateEventHandler {

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.PlaceEvent event) {
        event.setCanceled(onBlockPlaced(event.placedBlock, event.player));
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.MultiPlaceEvent event) {
        event.setCanceled(onBlockPlaced(event.placedBlock, event.player));
    }

    private static boolean onBlockPlaced(Block placedBlock, EntityPlayer player) {
        if (!player.worldObj.isRemote && (placedBlock == ModBlocks.rbmk_rod || placedBlock == ModBlocks.rbmk_rod_mod
            || placedBlock == ModBlocks.rbmk_rod_reasim
            || placedBlock == ModBlocks.rbmk_rod_reasim_mod)) {

            if (!GateManager.TierGates.RBMK.satisfiedHolder.get()) {
                player.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.RED + "The RBMK gate hasn't been opened yet!" + EnumChatFormatting.RESET));
                return true;
            }
        }
        return false;
    }
}
