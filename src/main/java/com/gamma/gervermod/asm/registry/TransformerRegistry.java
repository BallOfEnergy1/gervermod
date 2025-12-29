package com.gamma.gervermod.asm.registry;

import java.util.List;

import com.gamma.gervermod.asm.interfaces.ITransformer;
import com.gamma.gervermod.asm.transformers.NBTStructureTransformer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class TransformerRegistry {

    private static final List<ITransformer> transformers;

    static {
        transformers = ObjectArrayList.of(new NBTStructureTransformer());
    }

    public static List<ITransformer> getRegistered() {
        return transformers;
    }

    public static boolean inRegistry(ITransformer obj) {
        return transformers.contains(obj);
    }

    public static int length() {
        return transformers.size();
    }
}
