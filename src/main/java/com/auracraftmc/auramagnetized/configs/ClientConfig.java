package com.auracraftmc.auramagnetized.configs;

import javax.annotation.Nonnull;

import net.minecraftforge.common.ForgeConfigSpec.*;

public class ClientConfig {

    public final BooleanValue spawnParticles;
    public final EnumValue<ParticleTypes> particleType;

    public ClientConfig(@Nonnull Builder builder) {
        builder.comment("General configuration settings").push("general");

        spawnParticles = builder.comment(
                        ".",
                        " Should particles spawn around attracted items?",
                        " The default is 'true'."
                ).translation("aura_magnetized.particles.spawnParticles")
                .define("particles.spawnParticles", true);

        particleType = builder.comment(
                        ".",
                        " The type of particles that should spawn when attracting items.",
                        " The default is 'MAGNET_WARP'."
                ).translation("aura_magnetized.particles.particleType")
                .defineEnum("particles.particleType", ParticleTypes.MAGNET_WARP, ParticleTypes.values());

        builder.pop();
    }

    public enum ParticleTypes {
        MAGNET_WARP,
        ITEM_SPARKLE
    }
}