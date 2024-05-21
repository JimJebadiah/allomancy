package com.example.examplemod.feruchemy.metalminds.attributemodifiers;

import com.example.examplemod.feruchemy.metalminds.MetalMind;
import com.example.examplemod.feruchemy.metalminds.MetalMindState;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class AttributeMetalMind extends MetalMind {

    public AttributeMetalMind(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
        createTags(stack);
    }

    @Override
    public void tap(ItemStack stack) {
//        AttributeInstance attr = player.getAttribute(attributeInstance());
//        CompoundTag tag = stack.getTag();
//        MetalMindUtils.removeModifier(attr, UUID.fromString(tag.getString(TAP_UUID_KEY)), this);
//        MetalMindUtils.applyModifier(attr, tapModifier(), UUID.fromString(tag.getString(TAP_UUID_KEY)), this, true);
    }

    @Override
    public void store(ItemStack stack) {
//        AttributeInstance attr = player.getAttribute(attributeInstance());
//        CompoundTag tag = stack.getTag();
//        MetalMindUtils.removeModifier(attr, UUID.fromString(tag.getString(STORE_UUID_KEY)), this);
//        MetalMindUtils.applyModifier(attr, storeModifier(), UUID.fromString(tag.getString(STORE_UUID_KEY)), this, false);
    }

    @Override
    public void stopUsing(ItemStack stack) {
//        AttributeInstance attr = player.getAttribute(ATTRIBUTE);
//        CompoundTag tag = stack.getTag();
//
//        if (tag.contains(TAP_UUID_KEY) && tag.contains(STORE_UUID_KEY)) {
//            MetalMindUtils.removeModifier(attr, UUID.fromString(tag.getString(TAP_UUID_KEY)), this);
//            MetalMindUtils.removeModifier(attr, UUID.fromString(tag.getString(STORE_UUID_KEY)), this);
//        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMa;
        MetalMindState state = getState(stack);
        switch (state) {
            case STORING ->
        }
        state.tick(this, stack);
    }

    private void createTags(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        UUID storeRandom = UUID.randomUUID();
        UUID tapRandom = UUID.randomUUID();

        // if no modifier uuid, create one
        if (!tag.contains(STORE_UUID_KEY)) {
            tag.putString(STORE_UUID_KEY, storeRandom.toString());
        }
        if (!tag.contains(TAP_UUID_KEY)) {
            tag.putString(TAP_UUID_KEY, tapRandom.toString());
        }
    }

    public abstract Attribute attributeInstance();
    public abstract CompoundTag storeModifier();
    public abstract CompoundTag tapModifier();
}
