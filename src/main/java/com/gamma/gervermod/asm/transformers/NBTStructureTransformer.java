package com.gamma.gervermod.asm.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.gamma.gervermod.asm.interfaces.IMethodTransformer;
import com.gtnewhorizon.gtnhlib.asm.ClassConstantPoolParser;

public class NBTStructureTransformer implements IMethodTransformer {

    private static final ClassConstantPoolParser cstPoolParser = new ClassConstantPoolParser(
        "com/hbm/world/gen/nbt/NBTStructure");

    @Override
    public ClassConstantPoolParser getTargetClasses() {
        return cstPoolParser;
    }

    /** @return Was the class changed? */
    public boolean transformMethod(String transformedName, MethodNode mn) {

        if (!(mn.name.equals("registerStructure"))) return false;
        if (!(mn.desc.equals("(ILcom/hbm/world/gen/nbt/SpawnCondition;)V"))) return false;

        InsnList list = new InsnList();

        list.add(new VarInsnNode(Opcodes.ILOAD, 0));
        LabelNode notEqual = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, notEqual));
        list.add(
            new FieldInsnNode(Opcodes.GETSTATIC, "com/gamma/gervermod/dim/struct/StructDimHandler", "structDim", "I"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "com/hbm/world/gen/nbt/NBTStructure",
                "registerStructure",
                "(ILcom/hbm/world/gen/nbt/SpawnCondition;)V",
                false));
        list.add(notEqual);

        mn.instructions.insertBefore(mn.instructions.getFirst(), list);

        return true;
    }
}
