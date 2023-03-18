package com.auracraftmc.auramagnetized.packets;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.items.IMagnetConfigurable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketRangeChange {

    private final double range;

    public PacketRangeChange(double range) {
        this.range = range;
    }

    public static void encode(@Nonnull PacketRangeChange msg, @Nonnull FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.range);
    }

    @Nonnull
    public static PacketRangeChange decode(@Nonnull FriendlyByteBuf buffer) {
        return new PacketRangeChange(buffer.readDouble());
    }

    public static void handle(@Nonnull PacketRangeChange msg, @Nonnull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if(player == null) return;

            ItemStack stack = player.getMainHandItem().getItem() instanceof IMagnetConfigurable ? player.getMainHandItem() : (player.getOffhandItem().getItem() instanceof IMagnetConfigurable ? player.getOffhandItem() : null);

            if(stack != null) ((IMagnetConfigurable) stack.getItem()).setRange(stack, msg.range);
        });

        ctx.get().setPacketHandled(true);
    }
}