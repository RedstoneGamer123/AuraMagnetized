package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import static com.auracraftmc.auramagnetized.blocks.ICoilConfigurable.COIL;

public class CoilCapabilityProvider implements ICapabilityProvider {

    private final LazyOptional<ICoilHandler> entityCapability;

    public CoilCapabilityProvider(@Nonnull ICoilHandler handler) {
        this.entityCapability = LazyOptional.of(() -> handler);
    }

    public CoilCapabilityProvider(@Nonnull Level level, @Nonnull BlockPos pos, double defaultRange, double minRange, double maxRange) {
        this(new CoilHandler(level, pos, defaultRange, minRange, maxRange));
    }

    public CoilCapabilityProvider(@Nonnull Level level, @Nonnull BlockPos pos, double defaultRange) {
        this(new CoilHandler(level, pos, defaultRange, defaultRange, defaultRange));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == COIL ? entityCapability.cast() : LazyOptional.empty();
    }

    public static void register(@Nonnull RegisterCapabilitiesEvent event) {
        event.register(ICoilHandler.class);
    }
}