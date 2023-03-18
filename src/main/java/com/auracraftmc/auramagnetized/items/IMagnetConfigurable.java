package com.auracraftmc.auramagnetized.items;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IMagnetConfigurable extends IMagnetItem {

    void setRange(@Nonnull ItemStack stack, double range);

    double getMinRange();

    double getMaxRange();
}