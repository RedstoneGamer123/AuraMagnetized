package com.auracraftmc.auramagnetized.blocks;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.capabilities.ICoilHandler;
import com.auracraftmc.auramagnetized.capabilities.IItemEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.*;

public interface ICoilBlock {

    Capability<ICoilHandler> COIL = CapabilityManager.get(new CapabilityToken<>() {});
    Capability<IItemEntityHandler> COIL_ITEMS = CapabilityManager.get(new CapabilityToken<>() {});

    BooleanProperty POWERED = BooleanProperty.create("powered");

    double getDefaultRange();

    double getRange(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos);

    boolean isPowered(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos);

    void setPowered(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, boolean powered);
}