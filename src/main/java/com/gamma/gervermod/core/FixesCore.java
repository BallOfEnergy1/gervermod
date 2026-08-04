package com.gamma.gervermod.core;

import com.gamma.gervermod.accessors.RandomTickSpeedAccessor;
import com.hbm.handler.ArmorModHandler;

import ganymedes01.etfuturum.gamerule.RandomTickSpeed;

public class FixesCore {

    public static boolean eidLoaded = false;
    public static boolean ae2Loaded = false;
    public static boolean etFuturumLoaded = false;
    public static boolean ntmLoaded = false;

    public static void onPreTick() {
        if (etFuturumLoaded) ((RandomTickSpeedAccessor) RandomTickSpeed.INSTANCE).gervermod$resetCache();
        if (ntmLoaded) {
            // Clear every second to avoid getting too full (while maintaining performance).
            ArmorModHandler.pryMods(null);
        }
    }
}
