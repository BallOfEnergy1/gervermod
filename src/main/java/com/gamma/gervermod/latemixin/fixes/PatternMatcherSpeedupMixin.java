package com.gamma.gervermod.latemixin.fixes;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.hbm.module.ModulePatternMatcher;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

@Mixin(ModulePatternMatcher.class)
public abstract class PatternMatcherSpeedupMixin {

    @Shadow(remap = false)
    public String[] modes;
    @Unique
    private ByteBuf gervermod$cached;
    // Hash of the `modes` string array, NOT the bytebuffer.
    @Unique
    private int gervermod$cachedHash;

    @WrapMethod(method = "serialize", remap = false)
    public void wrapped(ByteBuf buf, Operation<Void> original) {
        int hash = Arrays.hashCode(modes);
        if (gervermod$cached != null) {
            if (hash == gervermod$cachedHash) {
                buf.writeBytes(gervermod$cached, 0, gervermod$cached.writerIndex());
                return;
            }
            gervermod$cached.release();
        }
        gervermod$cached = PooledByteBufAllocator.DEFAULT.heapBuffer();
        original.call(gervermod$cached);
        gervermod$cachedHash = hash;
        buf.writeBytes(gervermod$cached, 0, gervermod$cached.writerIndex());
    }
}
