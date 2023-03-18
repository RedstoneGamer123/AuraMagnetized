package com.auracraftmc.auramagnetized.registries;

import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import com.auracraftmc.auramagnetized.blocks.CoilBlock;
import net.minecraft.world.level.block.Block;

import static com.auracraftmc.auramagnetized.AuraMagnetizedMod.MODINFO;

public class Blocks extends net.minecraft.world.level.block.Blocks {

    public static final Block BASIC_COIL = new CoilBlock(MODINFO, "basic_coil_block", AuraMagnetizedMod.MAIN_TAB, Configs.COMMON.basicCoilRange);

    public static void register() {
        //MinecraftForge.EVENT_BUS.register(CoilBlock.EventHandler.class);
    }
}