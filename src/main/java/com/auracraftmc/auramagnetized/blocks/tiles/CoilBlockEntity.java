package com.auracraftmc.auramagnetized.blocks.tiles;

import java.util.concurrent.*;
import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auraapi.api.blocks.tiles.AuraBlockEntity;
import com.auracraftmc.auramagnetized.blocks.ICoilBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.auracraftmc.auramagnetized.blocks.ICoilBlock.*;

public class CoilBlockEntity extends AuraBlockEntity {

    public CoilBlockEntity(@Nonnull ModInfo info, @Nonnull BlockEntityType<?> type, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        super(info, type, pos, state);
    }

    public static <T extends BlockEntity> void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull BlockEntity blockEntity) {
        if(state.getBlock() instanceof ICoilBlock block) {
            double range = blockEntity.getCapability(COIL).map(Handler -> Handler.getRange()).orElse(block.getDefaultRange());

            for(ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range))) {
                item.getCapability(COIL_ITEMS).ifPresent(handler -> {
                    handler.setDemagnetization(true);

                    try {
                        Executors.newSingleThreadScheduledExecutor().schedule(() -> handler.setDemagnetization(false), 50, TimeUnit.MILLISECONDS);
                    } catch(RejectedExecutionException e) {
                        handler.setDemagnetization(false);
                    }
                });
            }
        }
    }
}