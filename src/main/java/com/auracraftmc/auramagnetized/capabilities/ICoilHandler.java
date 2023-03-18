package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ICoilHandler {

    @Nonnull
    Level getLevel();

    @Nonnull
    BlockPos getPos();

    @Nonnull
    BlockState getState();

    @Nonnull
    BlockEntity getBlockEntity();

    double getMinRange();

    double getMaxRange();

    boolean isPowered();

    void setPowered(boolean powered);

    double getRange();

    void setRange(double range);
}