package com.gamma.gervermod.asm.interfaces;

import org.objectweb.asm.tree.ClassNode;

public interface ISuperclassTransformer {

    /**
     * Transforms the superclass for a certain class node.
     *
     * @return Was the class changed?
     */
    boolean transformSuperclass(String transformedName, ClassNode cn);
}
