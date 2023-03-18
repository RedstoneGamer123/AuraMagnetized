package com.auracraftmc.auramagnetized.capabilities;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.NBTKeys;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemEntityHandler implements IItemEntityHandler {

    private final ItemEntity item;

    public ItemEntityHandler(@Nonnull ItemEntity item, boolean demagnetize, boolean remagentize) {
        this.item = item;

        if(!item.getPersistentData().contains(NBTKeys.DEMAGNETIZED)) item.getPersistentData().putBoolean(NBTKeys.DEMAGNETIZED, demagnetize);
        if(!item.getPersistentData().contains(NBTKeys.REMAGNETIZE)) item.getPersistentData().putBoolean(NBTKeys.REMAGNETIZE, remagentize);
    }

    @Nonnull
    @Override
    public ItemEntity getItemEntity() {
        return this.item;
    }

    @Nonnull
    @Override
    public ItemStack getStack() {
        return this.item.getItem();
    }

    @Nonnull
    @Override
    public Item getItem() {
        return this.item.getItem().getItem();
    }

    @Override
    public boolean isDemagnetized() {
        return this.item.getPersistentData().getBoolean(NBTKeys.DEMAGNETIZED);
    }

    @Override
    public void setDemagnetization(boolean demagnetize) {
        this.item.getPersistentData().putBoolean(NBTKeys.DEMAGNETIZED, demagnetize);
    }

    @Override
    public boolean canRemagnetize() {
        return this.item.getPersistentData().getBoolean(NBTKeys.REMAGNETIZE);
    }

    @Override
    public void setRemagnetizable(boolean remagnetizable) {
        this.item.getPersistentData().putBoolean(NBTKeys.REMAGNETIZE, remagnetizable);
    }
}