package com.auracraftmc.auramagnetized.items;

import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auramagnetized.capabilities.MagnetCapabilityProvider;
import com.auracraftmc.auramagnetized.client.screens.AdvancedMagnetScreen;
import com.auracraftmc.auramagnetized.registries.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class AdvancedMagnetItem extends MagnetItem implements IMagnetConfigurable {

    public AdvancedMagnetItem(@Nonnull ModInfo info, @Nonnull String name, @Nullable CreativeModeTab tab) {
        super(info, name, tab, Configs.COMMON.advancedMagnetRange.getTwo());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new MagnetCapabilityProvider(stack, getDefaultRange(), getMinRange(), getMaxRange());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if(player.isCrouching()) {
            if(world.isClientSide) Minecraft.getInstance().setScreen(new AdvancedMagnetScreen(player.getItemInHand(hand)));

            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else return super.use(world, player, hand);
    }

    @Override
    public void setRange(ItemStack stack, double range) {
        stack.getCapability(MAGNET).ifPresent(handler -> handler.setRange(range));
    }

    @Override
    public double getMinRange() {
        return Configs.COMMON.advancedMagnetRange.getOne().get();
    }

    @Override
    public double getMaxRange() {
        return Configs.COMMON.advancedMagnetRange.getTwo().get();
    }
}