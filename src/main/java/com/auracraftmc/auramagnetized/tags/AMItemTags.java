package com.auracraftmc.auramagnetized.tags;

import com.auracraftmc.auraapi.api.ModInfo;
import com.auracraftmc.auraapi.api.tags.IAuraItemTags;
import com.auracraftmc.auramagnetized.AuraMagnetizedMod;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class AMItemTags extends IAuraItemTags {
    
    public static final ModInfo info = AuraMagnetizedMod.MODINFO;
    
    public static final TagKey<Item> MAGNET = create(info.getModId(), "magnet");
    public static final TagKey<Item> MAGNET_CONFIGURABLE = create(info.getModId(), "magnet/configurable");
}
