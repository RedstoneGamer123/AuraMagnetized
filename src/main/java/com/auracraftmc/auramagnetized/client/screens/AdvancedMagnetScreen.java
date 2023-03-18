package com.auracraftmc.auramagnetized.client.screens;

import javax.annotation.Nonnull;

import com.auracraftmc.auramagnetized.PacketHandler;
import com.auracraftmc.auramagnetized.items.IMagnetConfigurable;
import com.auracraftmc.auramagnetized.items.IMagnetItem;
import com.auracraftmc.auramagnetized.packets.PacketRangeChange;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class AdvancedMagnetScreen extends Screen {

    private double minRange;
    private double maxRange;
    private final ItemStack stack;
    private final IMagnetItem item;

    public AdvancedMagnetScreen(@Nonnull ItemStack stack) {
        super(new TextComponent(""));

        this.stack = stack;
        this.item = stack.getItem() instanceof IMagnetItem ? (IMagnetItem) stack.getItem() : null;

        if(this.item != null && this.item instanceof IMagnetConfigurable magnet) {
            this.minRange = magnet.getMinRange();
            this.maxRange = magnet.getMaxRange();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        addRangeSlider();
    }

    private void addRangeSlider() {
        int width = 100;
        int height = 20;
        int offsetX = 0;
        int offsetY = 0;

        double increment = 0.5;

        ForgeSlider slider;
        Component prefix = new TextComponent("Range: ");
        Component suffix = TextComponent.EMPTY;
        Component plus = new TextComponent("+");
        Component minus = new TextComponent("-");

        addRenderableWidget(slider = new ForgeSlider((this.width / 2) - (width / 2) + offsetX, (this.height / 2) - (height / 2) + offsetY, width, height, prefix, suffix, this.minRange, this.maxRange, this.item.getRange(this.stack), increment, 0, true) {
            @Override
            protected void applyValue() {
                sendRangeUpdate(getValue());
            }
        });

        addRenderableWidget(new Button((this.width / 2) - (width / 2) - height + offsetX, (this.height / 2) - (height / 2) + offsetY, height, height, minus, b -> {
            slider.setValue(slider.getValue() - increment);

            sendRangeUpdate(slider.getValue());
        }));

        addRenderableWidget(new Button((this.width / 2) + (width / 2) + offsetX, (this.height / 2) - (height / 2) + offsetY, height, height, plus, b -> {
            slider.setValue(slider.getValue() + increment);

            sendRangeUpdate(slider.getValue());
        }));
    }

    private void sendRangeUpdate(double value) {
        PacketHandler.send(new PacketRangeChange(value));
    }
}