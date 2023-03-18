package com.auracraftmc.auramagnetized.items;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.capabilities.IMagnetHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;

public interface IMagnetItem {

    Capability<IMagnetHandler> MAGNET = CapabilityManager.get(new CapabilityToken<>() {});

    double getDefaultRange();

    double getRange(@Nonnull ItemStack stack);

    boolean isPowered(@Nonnull ItemStack stack);

    void setPowered(@Nonnull ItemStack stack, boolean powered);
}