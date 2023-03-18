package com.auracraftmc.auramagnetized.registries;

import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import com.auracraftmc.auramagnetized.items.AdvancedMagnetItem;
import com.auracraftmc.auramagnetized.items.MagnetItem;
import net.minecraft.world.item.Item;

import static com.auracraftmc.auramagnetized.AuraMagnetizedMod.MODINFO;

public class Items extends net.minecraft.world.item.Items {

    public static final Item BASIC_MAGNET = new MagnetItem(MODINFO, "basic_magnet", AuraMagnetizedMod.MAIN_TAB, Configs.COMMON.basicMagnetRange);
    public static final Item EMPOWERED_MAGNET = new MagnetItem(MODINFO, "empowered_magnet", AuraMagnetizedMod.MAIN_TAB, Configs.COMMON.empoweredMagnetRange);
    public static final Item ADVANCED_MAGNET = new AdvancedMagnetItem(MODINFO, "advanced_magnet",AuraMagnetizedMod.MAIN_TAB);

    public static void register() {}
}