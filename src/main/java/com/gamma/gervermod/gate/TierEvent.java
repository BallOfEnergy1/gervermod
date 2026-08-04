package com.gamma.gervermod.gate;

@FunctionalInterface
public interface TierEvent {

    void call(boolean opened);
}
