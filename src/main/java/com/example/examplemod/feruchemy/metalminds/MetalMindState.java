package com.example.examplemod.feruchemy.metalminds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum MetalMindState {
    NONE,
    TAPPING,
    STORING;

    public void tick(MetalMind mind, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        switch (this) {
            case TAPPING -> {
                mind.tap(stack);
                if (!tag.contains("charge")) {
                    tag.putInt("charge", 0);
                }
                int charge = tag.getInt("charge");
                charge -= mind.rate();

                if (charge <= 0) {
                    charge = 0;
                    mind.setState(NONE, stack);
                }
                stack.setDamageValue(100 - ((charge * 100) / mind.maxCharge()));

                tag.putInt("charge", charge);
            }
            case STORING -> {
                mind.store(stack);
                if (!tag.contains("charge")) {
                    tag.putInt("charge", 0);
                }
                int charge = tag.getInt("charge");
                charge += mind.rate();

                if (charge >= mind.maxCharge()) {
                    charge = mind.maxCharge();
                    mind.setState(NONE, stack);
                }
                stack.setDamageValue(100 - ((charge * 100) / mind.maxCharge()));

                tag.putInt("charge", charge);
            }
            default -> mind.stopUsing(stack);
        }
    }

    public boolean hasFoil() {
        return switch (this) {
            case TAPPING, STORING -> true;
            default -> false;
        };
    }

    public void tap(MetalMind mind, ItemStack stack) {
        if (this != MetalMindState.NONE || mind.getCharge(stack) == 0) {
            mind.setState(NONE, stack);
        } else {
            mind.setState(TAPPING, stack);
        }
    }

    public void store(MetalMind mind, ItemStack stack) {
        if (this != MetalMindState.NONE || mind.getCharge(stack) == mind.maxCharge()) {
            mind.setState(NONE, stack);
        } else {
            mind.setState(STORING, stack);
        }
    }

    public String getHoverText(MetalMind mind) {
        return switch (this) {
            case TAPPING -> "Tapping " + mind.attribute();
            case STORING -> "Storing " + mind.attribute();
            case NONE -> "";
        };
    }
}