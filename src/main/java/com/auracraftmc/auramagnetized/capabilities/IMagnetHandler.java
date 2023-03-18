package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IMagnetHandler {

    @Nonnull
    ItemStack getStack();

    double getMinRange();

    double getMaxRange();

    boolean isPowered();

    void setPowered(boolean powered);

    double getRange();

    void setRange(double range);
}