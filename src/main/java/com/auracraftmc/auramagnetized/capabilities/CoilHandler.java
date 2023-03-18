package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.NBTKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.auracraftmc.auramagnetized.blocks.ICoilBlock.POWERED;

public class CoilHandler implements ICoilHandler {

    private final Level level;
    private final BlockPos position;
    private final double minRange;
    private final double maxRange;

    public CoilHandler(@Nonnull Level level, @Nonnull BlockPos pos, double range, double minRange, double maxRange) {
        this.level = level;
        this.position = pos;
        this.minRange = minRange;
        this.maxRange = maxRange;

        if(!level.getBlockEntity(pos).getTileData().contains(NBTKeys.RANGE) || (minRange == maxRange && level.getBlockEntity(pos).getTileData().getDouble(NBTKeys.RANGE) != range)) setRange(range);
    }

    public CoilHandler(@Nonnull Level level, @Nonnull BlockPos pos, double defaultRange) {
        this(level, pos, defaultRange, defaultRange, defaultRange);
    }

    @Nonnull
    @Override
    public Level getLevel() {
        return this.level;
    }

    @Nonnull
    @Override
    public BlockPos getPos() {
        return this.position;
    }

    @Nonnull
    @Override
    public BlockState getState() {
        return this.level.getBlockState(this.position);
    }

    @Nonnull
    @Override
    public BlockEntity getBlockEntity() {
        return this.level.getBlockEntity(this.position);
    }

    @Override
    public double getMinRange() {
        return this.minRange;
    }

    @Override
    public double getMaxRange() {
        return this.maxRange;
    }

    @Override
    public boolean isPowered() {
        return getState().getValue(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        getState().setValue(POWERED, powered);
    }

    @Override
    public double getRange() {
        return getBlockEntity().getTileData().getDouble(NBTKeys.RANGE);
    }

    @Override
    public void setRange(double range) {
        getBlockEntity().getTileData().putDouble(NBTKeys.RANGE, range);
    }
}