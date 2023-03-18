package com.auracraftmc.auramagnetized.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auraapi.api.blocks.AuraEntityBlock;
import com.auracraftmc.auraapi.api.blocks.IAuraEntityBlock;
import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import com.auracraftmc.auramagnetized.blocks.tiles.CoilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class CoilBlock extends AuraEntityBlock implements ICoilBlock {

    private final DoubleValue defaultRange;

    public CoilBlock(@Nonnull ModInfo info, @Nonnull String name, @Nullable CreativeModeTab tab, @Nonnull DoubleValue defaultRange) {
        super(info, name, tab, Properties.of(Material.METAL).noOcclusion().randomTicks().requiresCorrectToolForDrops());

        this.defaultRange = defaultRange;

        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, true));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        if(state.getBlock() instanceof IAuraEntityBlock<?> block) return type == block.getBlockEntityType() ? CoilBlockEntity::tick : null;

        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CoilBlockEntity(getModInfo(), getBlockEntityType(), pos, state);
    }

    @Override
    public double getDefaultRange() {
        return this.defaultRange.get();
    }

    @Override
    public double getRange(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos) {
        return getDefaultRange();
    }

    @Override
    public boolean isPowered(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos) {
        return state.hasProperty(POWERED) && state.getValue(POWERED);
    }

    @Override
    public void setPowered(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, boolean powered) {
        if(state.hasProperty(POWERED)) state.setValue(POWERED, powered);
    }

    @EventBusSubscriber(modid = AuraMagnetizedMod.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onEntityTeleport(@Nonnull EntityTeleportEvent event) {
            if(event.getEntity() instanceof ItemEntity item) {
                item.getCapability(COIL_ITEMS).ifPresent(handler -> {
                    handler.setRemagnetizable(true);
                    handler.setDemagnetization(false);
                });
            }
        }
    }
}