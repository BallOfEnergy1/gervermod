package com.gamma.gervermod.asm;

import java.util.Arrays;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.CheckClassAdapter;

import com.gamma.gervermod.asm.interfaces.IConstructorTransformer;
import com.gamma.gervermod.asm.interfaces.IFieldTransformer;
import com.gamma.gervermod.asm.interfaces.IMethodTransformer;
import com.gamma.gervermod.asm.interfaces.ISuperclassTransformer;
import com.gamma.gervermod.asm.interfaces.ITransformer;
import com.gamma.gervermod.asm.registry.TransformerRegistry;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

@SuppressWarnings("unused")
public class GervermodTransformerHandler implements IClassTransformer {

    private static final ObjectSet<String> processedClasses = new ObjectOpenHashSet<>();

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (processedClasses.contains(transformedName)) return basicClass;
        if (basicClass == null) {
            processedClasses.add(transformedName);
            return null;
        }

        String className = transformedName.replace(".", "/");

        if (transformedName.contains("asm")) {
            processedClasses.add(transformedName);
            return basicClass;
        }

        final ClassReader classReader = new ClassReader(basicClass);
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        boolean changed = transformByGervermod(transformedName, classNode, basicClass);

        if (changed) {
            ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);

            final byte[] bytes;

            bytes = cw.toByteArray();

            ClassReader checker = new ClassReader(bytes);
            checker.accept(new CheckClassAdapter(new ClassNode()), 0);
            processedClasses.add(transformedName);
            return bytes;
        }
        processedClasses.add(transformedName);
        return basicClass;
    }

    public boolean transformByGervermod(String transformedName, ClassNode classNode, byte[] bytecode) {

        // Get all valid transformers (transformers that would like to transform this class).
        ITransformer[] validTransformers = new ITransformer[TransformerRegistry.length()];
        int i = 0;
        for (ITransformer transformer : TransformerRegistry.getRegistered()) {
            if (!transformer.getTargetClasses()
                .find(bytecode, true)) continue;
            validTransformers[i] = transformer;
            i++;
        }

        if (i == 0) return false;

        // Trim valid transformers array to size.
        System.arraycopy(validTransformers, 0, validTransformers = new ITransformer[i], 0, i);

        boolean changed = false;

        for (ITransformer validTransformer : validTransformers) {
            if (validTransformer instanceof ISuperclassTransformer)
                changed |= ((ISuperclassTransformer) validTransformer).transformSuperclass(transformedName, classNode);

            boolean isFieldTransformationAllowed = false;
            if (validTransformer instanceof IFieldTransformer) {
                String[] excludedNodes = ((IFieldTransformer) validTransformer).getExcludedClassNodes();
                if (!Arrays.asList(excludedNodes)
                    .contains(classNode.name)) {
                    isFieldTransformationAllowed = true;
                }
            }

            for (MethodNode mn : classNode.methods) {
                if (validTransformer instanceof IConstructorTransformer) {
                    boolean[] results = ((IConstructorTransformer) validTransformer)
                        .transformConstructors(transformedName, mn);
                    changed |= results[0];
                    if (results[1]) {
                        throw new IllegalStateException(
                            "Failed to transform " + transformedName + " due to missing constructor call");
                    }
                }
                if (validTransformer instanceof IMethodTransformer) {
                    changed |= ((IMethodTransformer) validTransformer).transformMethod(transformedName, mn);
                }
                if (isFieldTransformationAllowed) {
                    changed |= ((IFieldTransformer) validTransformer).transformFieldAccesses(transformedName, mn);
                }
            }
        }

        return changed;
    }
}
