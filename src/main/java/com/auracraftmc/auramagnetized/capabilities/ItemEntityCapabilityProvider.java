package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import static com.auracraftmc.auramagnetized.blocks.ICoilBlock.COIL_ITEMS;

public class ItemEntityCapabilityProvider implements ICapabilityProvider {

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(AuraMagnetizedMod.MODID, "itementity_magnet_capability");

    private final LazyOptional<IItemEntityHandler> entityCapability;

    public ItemEntityCapabilityProvider(@Nonnull IItemEntityHandler handler) {
        this.entityCapability = LazyOptional.of(() -> handler);
    }

    public ItemEntityCapabilityProvider(@Nonnull ItemEntity itemEntity, boolean demagnetize, boolean remagnetize) {
        this(new ItemEntityHandler(itemEntity, demagnetize, remagnetize));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == COIL_ITEMS ? entityCapability.cast() : LazyOptional.empty();
    }

    public static void register(@Nonnull RegisterCapabilitiesEvent event) {
        event.register(IItemEntityHandler.class);
    }
}