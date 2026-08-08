package com.gamma.gervermod.gate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import com.gamma.gervermod.core.GerverMod;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.util.Tuple;

public class GateManager {

    public static File dataFile;

    public static void init(MinecraftServer server) {
        dataFile = new File(server.getFile("gerver"), "gateData.bin");
        // noinspection ResultOfMethodCallIgnored
        dataFile.getParentFile()
            .mkdirs();
        boolean isNew = true;
        try {
            isNew = dataFile.createNewFile();
        } catch (IOException e) {
            GerverMod.LOG.warn("Could not create gate data file", e);
        }

        try {
            if (isNew) {
                write();
                GerverMod.LOG.info("Initialized gate data.");
            } else {
                read();
                GerverMod.LOG.info("Read gate data.");
            }
        } catch (IOException | ClassNotFoundException e) {
            GerverMod.LOG.warn("Could not initialize gate data file", e);
        }
    }

    private static void write() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            for (int i = 0; i < TierGates.values().length; i++) {
                oos.writeBoolean(TierGates.values()[i].satisfiedHolder.get());
            }
        }
    }

    private static void read() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            for (int i = 0; i < TierGates.values().length; i++) {
                setGate(TierGates.values()[i], ois.readBoolean());
            }
        }
    }

    public static void setGate(TierGates gate, boolean value) {
        gate.satisfiedHolder.set(value);
        // call event
        if (gate.event != null) gate.event.call(value);

        if (gate.recipes != null) {
            if (value) {
                for (Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe> recipe : gate.recipes) {
                    recipe.getX().recipeNameMap.put(recipe.getY(), recipe.getZ());
                }
            } else {
                for (Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe> recipe : gate.recipes) {
                    recipe.getX().recipeNameMap.remove(recipe.getY());
                }
            }
        }
        try {
            write();
        } catch (IOException e) {
            GerverMod.LOG.warn("Could not write to gate data file", e);
        }
    }

    public enum TierGates {

        OIL1("Oil 1", of("ass.refinery")),
        OIL2("Oil 2", of("ass.crackingtower"), of("ass.radiolysis")),
        RBMK("RBMK"),
        // Disables processing of tier 1 drives
        PLANET1("Drive Processor Tier 1"),
        OIL3("Oil 3", of("ass.vaccumrefinery")),
        // Disables processing of tier 2 drives
        PLANET2("Drive Processor Tier 2"),
        // Disables processing of tier 3 drives
        PLANET3("Drive Processor Tier 3"),
        PA("Particle Accelerator", of("ass.source")),
        GERALD("Gerald", of("ass.gerald"));

        public final String name;
        public final Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe>[] recipes;
        public final TierEvent event;
        public final AtomicBoolean satisfiedHolder = new AtomicBoolean(false);

        TierGates(String name) {
            this.name = name;
            this.recipes = null;
            this.event = null;
        }

        TierGates(String name, TierEvent event) {
            this.name = name;
            this.recipes = null;
            this.event = event;
        }

        @SafeVarargs
        TierGates(String name, Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe>... recipes) {
            this(name, null, recipes);
        }

        @SafeVarargs
        TierGates(String name, TierEvent event,
            Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe>... recipes) {
            this.name = name;
            this.recipes = recipes;
            this.event = event;
        }
    }

    public static String[] getInformation() {
        List<String> info = new ArrayList<>();
        info.add(EnumChatFormatting.GOLD + "Gates:" + EnumChatFormatting.RESET);
        for (TierGates tierGate : TierGates.values()) {
            info.add(
                tierGate.name + ": "
                    + (tierGate.satisfiedHolder.get() ? (EnumChatFormatting.GREEN + "Opened")
                        : (EnumChatFormatting.RED + "Closed"))
                    + EnumChatFormatting.RESET);
        }
        return info.toArray(new String[0]);
    }

    private static Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe> of(String name) {
        return new Tuple.Triplet<>(
            AssemblyMachineRecipes.INSTANCE,
            name,
            AssemblyMachineRecipes.INSTANCE.recipeNameMap.get(name));
    }

    private static Tuple.Triplet<GenericRecipes<GenericRecipe>, String, GenericRecipe> of(
        GenericRecipes<GenericRecipe> handler, String name) {
        return new Tuple.Triplet<>(handler, name, handler.recipeNameMap.get(name));
    }
}
