package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemEntityHandler {

    @Nonnull
    ItemEntity getItemEntity();

    @Nonnull
    ItemStack getStack();

    @Nonnull
    Item getItem();

    boolean isDemagnetized();

    void setDemagnetization(boolean demagnetize);

    boolean canRemagnetize();

    void setRemagnetizable(boolean remagnetizable);
}