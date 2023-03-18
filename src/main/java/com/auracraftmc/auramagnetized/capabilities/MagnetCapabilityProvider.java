package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.auracraftmc.auramagnetized.items.IMagnetItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

public class MagnetCapabilityProvider implements ICapabilityProvider {

    private final LazyOptional<IMagnetHandler> magnetCapability;

    public MagnetCapabilityProvider(@Nonnull IMagnetHandler handler) {
        this.magnetCapability = LazyOptional.of(() -> handler);
    }

    public MagnetCapabilityProvider(@Nonnull ItemStack stack, double defaultRange, double minRange, double maxRange) {
        this(new MagnetHandler(stack, defaultRange, minRange, maxRange));
    }

    public MagnetCapabilityProvider(@Nonnull ItemStack stack, double defaultRange) {
        this(new MagnetHandler(stack, defaultRange));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == IMagnetItem.MAGNET ? magnetCapability.cast() : LazyOptional.empty();
    }

    public static void register(@Nonnull RegisterCapabilitiesEvent event) {
        event.register(IMagnetHandler.class);
    }
}