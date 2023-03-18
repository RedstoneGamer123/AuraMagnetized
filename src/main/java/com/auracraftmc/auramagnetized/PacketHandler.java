package com.auracraftmc.auramagnetized;

import com.auracraftmc.auramagnetized.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    
    private static final String PROTOCOL_VERSION = "1";
    private static int id = -1;
    
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(AuraMagnetizedMod.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    
    public static void register() {
        HANDLER.registerMessage(id++, PacketRangeChange.class, PacketRangeChange::encode, PacketRangeChange::decode, PacketRangeChange::handle);
    }
    
    public static void send(Object msg) {
        HANDLER.sendToServer(msg);
    }
}