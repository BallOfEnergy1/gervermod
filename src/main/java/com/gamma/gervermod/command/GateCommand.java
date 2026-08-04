package com.gamma.gervermod.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

import com.gamma.gervermod.gate.GateManager;

public class GateCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "gate";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gate <show|open|close>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) throw new WrongUsageException("/gate <show|open|close>");
        if (args[0].equalsIgnoreCase("show")) {
            for (String string : GateManager.getInformation()) {
                sender.addChatMessage(new ChatComponentText(string));
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            if (!sender.canCommandSenderUseCommand(4, "gate"))
                throw new CommandException("You do not have permission to use this command.");
            if (args.length != 2) throw new WrongUsageException("/gate open <gate-name>");
            GateManager.TierGates gate;
            try {
                gate = GateManager.TierGates.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid gate name.");
            }
            GateManager.setGate(gate, true);
            sender.addChatMessage(new ChatComponentText("Gate " + gate.name + " opened."));
        } else if (args[0].equalsIgnoreCase("close")) {
            if (!sender.canCommandSenderUseCommand(4, "gate"))
                throw new CommandException("You do not have permission to use this command.");
            if (args.length != 2) throw new WrongUsageException("/gate close <gate-name>");
            GateManager.TierGates gate;
            try {
                gate = GateManager.TierGates.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid gate name.");
            }
            GateManager.setGate(gate, false);
            sender.addChatMessage(new ChatComponentText("Gate " + gate.name + " closed."));

        } else throw new WrongUsageException("/gate <show|open|close>");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "show", "open", "close") : null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
