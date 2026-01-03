package com.gamma.gervermod.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import com.hbm.uninos.NodeNet;
import com.hbm.uninos.UniNodespace;
import com.hbm.util.ChatBuilder;

public class CommandReapNetworks extends CommandBase {

    @Override
    public String getCommandName() {
        return "ntmreapnetworks";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ntmreapnetworks";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            for (NodeNet<?, ?, ?> net : UniNodespace.activeNodeNets) {
                net.links.forEach((link) -> link.expired = true);
                net.links.clear();
                net.providerEntries.clear();
                net.receiverEntries.clear();
            }
            UniNodespace.activeNodeNets.clear();
            UniNodespace.worlds.clear();
        } catch (Exception ex) {
            sender.addChatMessage(
                ChatBuilder.start("----------------------------------")
                    .color(EnumChatFormatting.GRAY)
                    .flush());
            sender.addChatMessage(
                ChatBuilder.start("An error has occoured during network reap, consult the log for details.")
                    .color(EnumChatFormatting.RED)
                    .flush());
            sender.addChatMessage(
                ChatBuilder.start(ex.getLocalizedMessage())
                    .color(EnumChatFormatting.RED)
                    .flush());
            sender.addChatMessage(
                ChatBuilder.start(ex.getStackTrace()[0].toString())
                    .color(EnumChatFormatting.RED)
                    .flush());
            sender.addChatMessage(
                ChatBuilder.start("----------------------------------")
                    .color(EnumChatFormatting.GRAY)
                    .flush());
            throw ex;
        }
    }
}
