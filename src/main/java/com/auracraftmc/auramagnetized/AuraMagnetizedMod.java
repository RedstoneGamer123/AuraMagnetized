package com.auracraftmc.auramagnetized;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import com.auracraftmc.auraapi.api.AuraAPI.Mods;
import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auraapi.api.Pair;
import com.auracraftmc.auraapi.api.tags.IAuraItemTags;
import com.auracraftmc.auramagnetized.capabilities.*;
import com.auracraftmc.auramagnetized.registries.*;
import com.auracraftmc.auramagnetized.tabs.AuraMagnetizedTab;
import com.auracraftmc.auramagnetized.tags.AMItemTags;
import com.mojang.logging.LogUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(AuraMagnetizedMod.MODID)
public final class AuraMagnetizedMod {

    public static final String MODID = "aura_magnetized";
    public static final String NAME = "AuraMagnetized";
    public static final String VERSION = "1.0.0";

    public static final ModInfo MODINFO = new ModInfo(MODID, NAME, VERSION);

    public static AuraMagnetizedMod instance;
    public static final Logger logger = LogUtils.getLogger();

    public static final CreativeModeTab MAIN_TAB = new AuraMagnetizedTab(MODINFO);

    public static final List<Pair<String, String>> curios = new ArrayList<>();

    public AuraMagnetizedMod() {
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addGenericListener(Block.class, this::registerBlocks);
        modEventBus.addGenericListener(BlockEntityType.class, this::registerBlockEntities);
        modEventBus.addGenericListener(Item.class, this::registerItems);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::onEnqueue);

        Configs.register(modLoadingContext, modEventBus);
    }

    public void registerCapabilities(@Nonnull RegisterCapabilitiesEvent event) {
        CoilCapabilityProvider.register(event);
        ItemEntityCapabilityProvider.register(event);
        MagnetCapabilityProvider.register(event);
    }

    public void registerBlocks(@Nonnull Register<Block> event) {
        Blocks.register();
        event.getRegistry().registerAll(MODINFO.getBlocks().toArray(new Block[0]));
    }

    public void registerBlockEntities(@Nonnull Register<BlockEntityType<?>> event) {
        for(BlockEntityType<?> type : MODINFO.getBlockEntities().keySet()) {
            event.getRegistry().register(type);
        }
    }

    public void registerItems(@Nonnull Register<Item> event) {
        Items.register();
        event.getRegistry().registerAll(MODINFO.getItems().toArray(new Item[0]));
    }

    private void setup(@Nonnull FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    private void onEnqueue(@Nonnull InterModEnqueueEvent event) {
        for(String slot : Configs.COMMON.curiosSlots.get()) {
            Pair<String, String> curio = new Pair<>(Mods.CURIOS.getId(), slot);

            int i = slot.indexOf(":");
            if(i > -1) {
                curio.setTwo(slot.substring(i + 1));

                if(i > 0) curio.setOne(slot.substring(0, i));
            }

            curios.add(curio);

            InterModComms.sendTo(Mods.CURIOS.getId(), SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.findPreset(curio.getTwo()).map(type -> type.getMessageBuilder().build()).orElse(new SlotTypeMessage.Builder(curio.getTwo()).icon(new ResourceLocation(curio.getOne(), "item/empty_" + curio.getTwo() + "_slot")).build()));
        }
    }

    private void gatherData(@Nonnull GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if(event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new BlockTagsProvider(gen, MODID, event.getExistingFileHelper()) {
                @Override
                protected void addTags() {
                    this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Blocks.BASIC_COIL);
                    this.tag(BlockTags.NEEDS_STONE_TOOL).add(Blocks.BASIC_COIL);
                }

                @Override
                public String getName() {
                    return "AuraMagnetized's Block Tags";
                }
            };

            gen.addProvider(blockTagsProvider);
            gen.addProvider(new ItemTagsProvider(gen, blockTagsProvider, MODID, event.getExistingFileHelper()) {
                @Override
                protected void addTags() {
                    if(Mods.CURIOS.isLoaded()) {
                        for(Pair<String, String> curio : curios) {
                            this.tag(IAuraItemTags.create(curio.asString())).add(Items.BASIC_MAGNET, Items.EMPOWERED_MAGNET, Items.ADVANCED_MAGNET);
                        }
                    }

                    this.tag(AMItemTags.MAGNET).add(Items.BASIC_MAGNET, Items.EMPOWERED_MAGNET, Items.ADVANCED_MAGNET);
                    this.tag(AMItemTags.MAGNET_CONFIGURABLE).add(Items.ADVANCED_MAGNET);
                }

                @Override
                public String getName() {
                    return "AuraMagnetized's Item Tags";
                }
            });
        }
    }

    @EventBusSubscriber(modid = MODID)
    protected static class EventHandler {

        @SubscribeEvent
        public static void attachEntityCapabilities(@Nonnull AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof ItemEntity item && !event.getCapabilities().containsKey(ItemEntityCapabilityProvider.RESOURCE_LOCATION)) event.addCapability(ItemEntityCapabilityProvider.RESOURCE_LOCATION, new ItemEntityCapabilityProvider(item, false, true));
        }
    }
}