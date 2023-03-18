package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.NBTKeys;
import net.minecraft.world.item.ItemStack;

public class MagnetHandler implements IMagnetHandler {

    private final ItemStack stack;
    private final double minRange;
    private final double maxRange;

    public MagnetHandler(@Nonnull ItemStack stack, boolean powered, double defaultRange, double minRange, double maxRange) {
        this.stack = stack;
        this.minRange = minRange;
        this.maxRange = maxRange;

        if(!stack.getOrCreateTag().contains(NBTKeys.POWERED)) setPowered(powered);
        if(!stack.getOrCreateTag().contains(NBTKeys.RANGE) || (minRange == maxRange && stack.getOrCreateTag().getDouble(NBTKeys.RANGE) != defaultRange)) setRange(defaultRange);
    }

    public MagnetHandler(@Nonnull ItemStack stack, boolean powered, double defaultRange) {
        this(stack, powered, defaultRange, defaultRange, defaultRange);
    }

    public MagnetHandler(@Nonnull ItemStack stack, double defaultRange, double minRange, double maxRange) {
        this(stack, false, defaultRange, minRange, maxRange);
    }

    public MagnetHandler(@Nonnull ItemStack stack, double defaultRange) {
        this(stack, false, defaultRange, defaultRange, defaultRange);
    }

    @Nonnull
    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public double getMinRange() {
        return minRange;
    }

    @Override
    public double getMaxRange() {
        return maxRange;
    }

    @Override
    public boolean isPowered() {
        return stack.getOrCreateTag().getBoolean(NBTKeys.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        stack.getOrCreateTag().putBoolean(NBTKeys.POWERED, powered);
    }

    @Override
    public double getRange() {
        return stack.getOrCreateTag().getDouble(NBTKeys.RANGE);
    }

    @Override
    public void setRange(double range) {
        stack.getOrCreateTag().putDouble(NBTKeys.RANGE, Math.max(minRange, Math.min(range, maxRange)));
    }
}