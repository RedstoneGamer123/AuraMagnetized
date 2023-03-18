package com.auracraftmc.auramagnetized.tabs;

import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auramagnetized.registries.Items;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AuraMagnetizedTab extends CreativeModeTab {

    public AuraMagnetizedTab(@Nonnull ModInfo info) {
        super(info.getModId() + ".main");
    }

    @Nonnull
    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.BASIC_MAGNET);
    }
}