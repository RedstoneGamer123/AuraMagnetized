package com.auracraftmc.auramagnetized.configs;


import java.util.*;
import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.AuraAPI;
import com.auracraftmc.auraapi.api.AuraAPI.Mods;
import com.auracraftmc.auraapi.api.Pair;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class CommonConfig {

    public final EnumValue<RequirementOptions> requirement;
    public final BooleanValue allowCurios;
    public final ConfigValue<List<? extends String>> curiosSlots;

    public final DoubleValue basicMagnetRange;
    public final DoubleValue empoweredMagnetRange;
    public final Pair<DoubleValue, DoubleValue> advancedMagnetRange = new Pair<>();

    public final DoubleValue basicCoilRange;
    public final DoubleValue empoweredCoilRange;

    public final IntValue itemDropDelay;

    public CommonConfig(@Nonnull Builder builder) {
        builder.comment("General configuration settings").push("general");

        List<String> comment = new ArrayList<>();

        comment.add(".");
        comment.add(" Where is the magnet required to be in the player's inventory to attract items?");
        if(AuraAPI.isModLoaded("curios")) {
            comment.add(" The 'CURIOS' value will only allow magnets to work in a curios slot.");
            comment.add(" If the 'CURIOS' option is selected, allowCurios must be 'true'.");
        }
        comment.add(" The default is 'HOTBAR'.");

        requirement = builder.comment(comment.toArray(new String[0]))
                .translation("aura_magnetized.magnets.requirement")
                .defineEnum("magnets.requirement", RequirementOptions.HOTBAR, RequirementOptions.getValues());

        allowCurios = builder.comment(
                    ".",
                    " Can a magnet be used in a curios slot?",
                    " If enabled, this will work regardless of the requirement value.",
                    " The default is 'true'."
                ).translation("aura_magnetized.magnets.allowCurios")
                .define("magnets.curios.allowCurios", true);

        curiosSlots = builder.comment(
                    ".",
                    " What curios slot(s) can a magnet be put in?",
                    " Presets: https://github.com/TheIllusiveC4/Curios/wiki/Frequently-Used-Slots",
                    " Must RESTART after changing this value.",
                    " *Note*: If you input a custom slot you must format it as 'modid:curio'",
                    " *Note*: To create your own slot, you should put an image in a resource pack under 'assets/*modid*/textures/item/empty_*curio*_slot'",
                    " and set the priority and amount in the server curios config.",
                    " The default is 'charm'."
                ).translation("aura_magnetized.magnets.curiosSlot")
                .defineList("magnets.curios.slots", List.of("charm"), entry -> true);

        basicMagnetRange = builder.comment(
                    ".",
                    " The block radius from the player to attract items.",
                    " The default is '4.0'."
                ).translation("aura_magnetized.magnets.basic.range")
                .defineInRange("magnets.basic.range", 4.0, 1.0, Double.MAX_VALUE);

        empoweredMagnetRange = builder.comment(
                    ".",
                    " The block radius from the player to attract items.",
                    " The default is '8.0'."
                ).translation("aura_magnetized.magnets.empowered.range")
                .defineInRange("magnets.empowered.range", 8.0, 1.0, Double.MAX_VALUE);

        advancedMagnetRange.setOne(builder.comment(
                    ".",
                    " The block radius from the player to attract items.",
                    " The default is '2.0'."
                ).translation("aura_magnetized.magnets.advanced.range.min")
                .defineInRange("magnets.advanced.range.min", 2.0, 0.0, Double.MAX_VALUE));

        advancedMagnetRange.setTwo(builder.comment(
                    ".",
                    " The block radius from the player to attract items.",
                    " The default is '16.0'."
                ).translation("aura_magnetized.magnets.advanced.range.max")
                .defineInRange("magnets.advanced.range.max", 16.0, 1.0, Double.MAX_VALUE));

        basicCoilRange = builder.comment(
                    ".",
                    " The block radius to stop magnets from attracting in.",
                    " The default is '8.0'."
                ).translation("aura_magnetized.coils.basic.range")
                .defineInRange("coils.basic.range", 8.0, 1.0, Double.MAX_VALUE);

        empoweredCoilRange = builder.comment(
                    ".",
                    " The block radius to stop magnets from attracting in.",
                    " The default is '16.0'."
                ).translation("aura_magnetized.coils.empowered.range")
                .defineInRange("coils.empowered.range", 16.0, 1.0, Double.MAX_VALUE);

        itemDropDelay = builder.comment(
                    ".",
                    " The delay for dropped items to be magnetized again.",
                    " The value is in milliseconds.",
                    " The default is '2000'."
                ).translation("aura_magnetized.items.dropDelay")
                .defineInRange("items.dropDelay", 2000, 0, Integer.MAX_VALUE);

        builder.pop();
    }

    public enum RequirementOptions {
        INVENTORY,
        HOTBAR,
        HAND,
        CURIOS;

        @Nonnull
        public static RequirementOptions[] getValues() {
            List<RequirementOptions> values = new ArrayList<>(Arrays.asList(values()));

            if(!Mods.CURIOS.isLoaded()) values.remove(CURIOS);

            return values.toArray(new RequirementOptions[0]);
        }
    }
}