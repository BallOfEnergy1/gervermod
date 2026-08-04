package com.gamma.gervermod.command;

import java.util.BitSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import com.gamma.gervermod.core.GerverMod;
import com.mojang.authlib.GameProfile;

public class ClearItemsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "cleanup";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cleanup [radius] [blacklist|whitelist] [item1, item2, ...]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        // preconditions
        if (sender instanceof MinecraftServer)
            throw new CommandException("This command cannot be run from the console.");

        int count;
        if (args.length == 0) {
            // Clear all items within the default radius of 25 blocks.
            count = clearItemsInRadius(sender, 25, true);
        } else if (args.length == 1) {
            // Clear all items within the target radius.
            int radius = Integer.parseInt(args[0]);
            if (radius < 0 || radius > 100)
                throw new NumberInvalidException("Radius must be between 0 and 100 blocks.");
            count = clearItemsInRadius(sender, radius, true);
        } else if (args.length == 2) {
            throw new WrongUsageException("/cleanup [radius] [blacklist|whitelist] [item1, item2, ...]");
        } else {

            ICommandSender dummy = new FakePlayer(
                (WorldServer) sender.getEntityWorld(),
                new GameProfile(null, "GerverFakePlayer"));
            Item[] items = new Item[args.length - 2];

            for (int idx = 0; idx < args.length - 2; idx++) items[idx] = getItemByText(dummy, args[idx + 2]);

            int radius = Integer.parseInt(args[0]);
            if (radius < 0 || radius > 100)
                throw new NumberInvalidException("Radius must be between 0 and 100 blocks.");
            if (Objects.equals(args[1], "blacklist")) {
                count = clearItemsInRadius(sender, radius, true, items);
            } else if (Objects.equals(args[1], "whitelist")) {
                count = clearItemsInRadius(sender, radius, false, items);
            } else {
                throw new WrongUsageException("/cleanup [radius] [blacklist|whitelist] [item1, item2, ...]");
            }
        }

        sender.addChatMessage(new ChatComponentText("Cleared " + count + " items."));
    }

    private static final BitSet selectedItems = new BitSet();

    private int clearItemsInRadius(ICommandSender sender, int radius, boolean blacklist, Item... items) {

        World world = sender.getEntityWorld();
        EntityPlayer player = (EntityPlayer) sender;
        int playerX = (int) (player.posX + 0.5);
        int playerY = (int) (player.posY + 0.5);
        int playerZ = (int) (player.posZ + 0.5);

        GerverMod.LOG.info("/-- Initiated by: {}", sender.getCommandSenderName());
        GerverMod.LOG.info("|   Coords: {}, {}, {}", playerX, playerY, playerZ);
        GerverMod.LOG.info("|   Clearing items with radius (blocks): {}", radius);
        GerverMod.LOG.info("|   Mode: {}", blacklist ? "blacklist" : "whitelist");
        GerverMod.LOG.info("|   Filter count: {}", items.length);
        GerverMod.LOG.info("|   Clearing...");

        selectedItems.set(
            0,
            Item.itemRegistry.getKeys()
                .size() - 1,
            blacklist);
        for (Item item : items) selectedItems.set(Item.itemRegistry.getIDForObject(item), !blacklist);

        // circular radius
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
            playerX - radius,
            playerY - radius,
            playerZ - radius,
            playerX + radius,
            playerY + radius,
            playerZ + radius);

        List<EntityItem> itemsInRadius = world.getEntitiesWithinAABB(EntityItem.class, aabb);

        int count = 0;
        for (EntityItem entityItem : itemsInRadius) {
            Item item = entityItem.getEntityItem()
                .getItem();
            if (selectedItems.get(Item.itemRegistry.getIDForObject(item))) {
                entityItem.setDead();
                count++;
            }
        }

        GerverMod.LOG.info("|   Checked {} items in radius.", itemsInRadius.size());
        GerverMod.LOG.info("\\-- Cleared {} items.", count);

        return count;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
