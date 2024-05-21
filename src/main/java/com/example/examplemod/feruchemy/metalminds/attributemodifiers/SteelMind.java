package com.example.examplemod.feruchemy.metalminds.attributemodifiers;

import com.example.examplemod.Allomancy;
import com.example.examplemod.feruchemy.metalminds.MetalMind;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.UUID;

public class SteelMind extends AttributeMetalMind {
    protected static CompoundTag STORE_MODIFIER = new AttributeModifier(UUID.randomUUID(), "Storing Speed Deficit", (double)-0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL).save();
    protected static CompoundTag TAP_MODIFIER = new AttributeModifier(UUID.randomUUID(), "Tapping Speed Boost", (double)1f, AttributeModifier.Operation.MULTIPLY_TOTAL).save();

    private static final Logger LOGGER = LogUtils.getLogger();

    public SteelMind(Item.Properties properties) {
        super(properties);
        ATTRIBUTE = Attributes.MOVEMENT_SPEED;
    }

    @Override
    public Attribute attributeInstance() {
        return Attributes.MOVEMENT_SPEED;
    }

    @Override
    public String attribute() {
        return "Speed";
    }

    @Override
    public int maxCharge() {
        return 20000;
    }

    @Override
    protected float rate() {
        return 1;
    }

    @Override
    public AttributeModifier storeModifier(ItemStack stack) {
        CompoundTag stackTag = stack.getOrCreateTag();
        if (stackTag.contains(STORE_UUID_KEY)) {
            CompoundTag tag = new CompoundTag();
            tag.merge(STORE_MODIFIER);
            tag.remove("UUID");
            tag.putUUID("UUID", UUID.fromString(stackTag.getString(STORE_UUID_KEY)));
            return AttributeModifier.load(tag);
        }
        return null;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public MetalMind getStandard() {
        return (MetalMind) Allomancy.STEEL_MIND.get();
    }

    @Override
    public AttributeModifier tapModifier(ItemStack stack) {
        CompoundTag stackTag = stack.getOrCreateTag();
        if (stackTag.contains(TAP_UUID_KEY)) {
            CompoundTag tag = new CompoundTag();
            tag.merge(TAP_MODIFIER);
            tag.remove("UUID");
            tag.putUUID("UUID", UUID.fromString(stackTag.getString(TAP_UUID_KEY)));
            return AttributeModifier.load(tag);
        }
        return null;
    }
}
