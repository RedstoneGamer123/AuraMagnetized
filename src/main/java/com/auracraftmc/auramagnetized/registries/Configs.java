package com.auracraftmc.auramagnetized.registries;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.configs.ClientConfig;
import com.auracraftmc.auramagnetized.configs.CommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public final class Configs {

    public static void register(@Nonnull ModLoadingContext context, IEventBus modEventBus) {
        context.registerConfig(ModConfig.Type.CLIENT, clientSpec);
        context.registerConfig(ModConfig.Type.COMMON, commonSpec);

        //modEventBus.register(Configs.class);
    }

    static final ForgeConfigSpec clientSpec;
    public static final ClientConfig CLIENT;
    static {
        Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static final ForgeConfigSpec commonSpec;
    public static final CommonConfig COMMON;
    static {
        Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    /*@SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {

    }*/
}