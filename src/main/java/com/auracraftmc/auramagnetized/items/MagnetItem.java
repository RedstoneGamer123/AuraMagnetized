package com.auracraftmc.auramagnetized.items;

import java.util.concurrent.*;
import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.AuraAPI.Mods;
import com.auracraftmc.auraapi.api.*;
import com.auracraftmc.auraapi.api.items.AuraItem;
import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import com.auracraftmc.auramagnetized.capabilities.IMagnetHandler;
import com.auracraftmc.auramagnetized.capabilities.MagnetCapabilityProvider;
import com.auracraftmc.auramagnetized.configs.CommonConfig.RequirementOptions;
import com.auracraftmc.auramagnetized.registries.Configs;
import com.auracraftmc.auramagnetized.registries.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import static com.auracraftmc.auramagnetized.blocks.ICoilBlock.COIL_ITEMS;

public class MagnetItem extends AuraItem implements IMagnetItem {

    private final DoubleValue defaultRange;

    public MagnetItem(@Nonnull ModInfo info, @Nonnull String name, @Nullable CreativeModeTab tab, @Nonnull DoubleValue defaultRange) {
        super(info, name, tab, new Properties().stacksTo(1));

        this.defaultRange = defaultRange;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new MagnetCapabilityProvider(stack, getDefaultRange());
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public boolean isRepairable(@Nonnull ItemStack magnetStack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack magnetStack, @Nonnull ItemStack stack) {
        return stack.getItem() == Items.IRON_INGOT;
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return stack.getCapability(MAGNET).map(IMagnetHandler::isPowered).orElse(false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if(!world.isClientSide && !player.isCrouching()) {
            setPowered(player.getItemInHand(hand), !isPowered(player.getItemInHand(hand)));

            return InteractionResultHolder.success(player.getItemInHand(hand));
        } else return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull Entity e, int slot, boolean inHand) {
        if((e instanceof LivingEntity living) && !living.isSpectator() && isPowered(stack)) {
            boolean allowCurios = Mods.CURIOS.isLoaded() && (Configs.COMMON.requirement.get() == RequirementOptions.CURIOS || Configs.COMMON.allowCurios.get());

            double range = getRange(stack);

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                boolean isActive = allowCurios && hasCurio(living);

                if(!isActive) {
                    switch (Configs.COMMON.requirement.get()) {
                        case INVENTORY -> isActive = true;
                        case HOTBAR -> isActive = slot < 9;
                        case HAND -> isActive = inHand;
                        case CURIOS -> {
                            if (!allowCurios) isActive = (slot < 9);

                            isActive = hasCurio(living);
                        }
                    }
                }

                if(isActive) {
                    double x = living.getX();
                    double y = living.getY();
                    double z = living.getZ();

                    for(ItemEntity item : living.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range))) {
                        if(item.getCapability(COIL_ITEMS).map(handler -> !handler.isDemagnetized()).orElse(true))
                            VectorAPI.moveEntity(x, y, z, 0, 0.75, 0, 0.65F, item, entity -> {
                                if(entity.getLevel().isClientSide) {
                                    // Particles?
                                }
                            });
                    }
                }
            }, 50, TimeUnit.MILLISECONDS);
        }
    }

    private boolean hasCurio(LivingEntity living) {
        return living.getCapability(CuriosCapability.INVENTORY).map(handler -> {
            for(Pair<String, String> curio : AuraMagnetizedMod.curios) {
                ICurioStacksHandler stacksHandler = handler.getCurios().get(curio.getTwo());

                if(stacksHandler != null && this == stacksHandler.getStacks().getStackInSlot(0).getItem()) return true;
            }

            return false;
        }).orElse(false);
    }

    @Override
    public double getDefaultRange() {
        return this.defaultRange.get();
    }

    @Override
    public double getRange(ItemStack stack) {
        return stack.getCapability(MAGNET).map(IMagnetHandler::getRange).orElse(this.defaultRange.get());
    }

    @Override
    public boolean isPowered(ItemStack stack) {
        return stack.getCapability(MAGNET).map(IMagnetHandler::isPowered).orElse(false);
    }

    @Override
    public void setPowered(ItemStack stack, boolean powered) {
        stack.getCapability(MAGNET).ifPresent(handler -> handler.setPowered(powered));
    }

    @EventBusSubscriber(modid = AuraMagnetizedMod.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onItemToss(@Nonnull ItemTossEvent event) {
            event.getEntity().getCapability(COIL_ITEMS).ifPresent(handler -> {
                handler.setRemagnetizable(false);
                handler.setDemagnetization(true);

                try {
                    Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                        handler.setRemagnetizable(true);
                        handler.setDemagnetization(false);
                    }, Configs.COMMON.itemDropDelay.get(), TimeUnit.MILLISECONDS);
                } catch(RejectedExecutionException e) {
                    handler.setRemagnetizable(true);
                    handler.setDemagnetization(false);
                }
            });
        }
    }
}