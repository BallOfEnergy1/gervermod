package com.gamma.gervermod.asm.interfaces;

import org.objectweb.asm.tree.MethodNode;

public interface IMethodTransformer extends ITransformer {

    /**
     * Transforms instructions in a certain method node.
     *
     * @return Was the class changed?
     */
    boolean transformMethod(String transformedName, MethodNode mn);
}
