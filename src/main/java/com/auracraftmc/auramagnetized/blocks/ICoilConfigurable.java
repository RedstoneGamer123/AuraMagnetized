package com.auracraftmc.auramagnetized.blocks;

import javax.annotation.Nonnull;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ICoilConfigurable extends ICoilBlock {

    void setRange(@Nonnull Level level, @Nonnull BlockState state, @Nonnull double range);

    double getMinRange();

    double getMaxRange();
}