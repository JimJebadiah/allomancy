package com.example.examplemod.feruchemy.metalminds;

import com.example.examplemod.feruchemy.metalminds.MetalMind;
import com.example.examplemod.feruchemy.metalminds.attributemodifiers.SteelMind;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class CreativeDecorator extends MetalMind {
    private MetalMind mind;

    public CreativeDecorator(Properties properties, MetalMind mind) {
        super(properties);
        this.mind = mind;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
        mind.inventoryTick(stack, level, entity, p_41407_, p_41408_);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("charge", maxCharge() / 2);
    }

    @Override
    public void store(ItemStack stack) {
        mind.store(stack);
    }

    @Override
    public void tap(ItemStack stack) {
        mind.tap(stack);
    }

    @Override
    public void stopUsing(ItemStack stack) {
        mind.stopUsing(stack);
    }

    @Override
    public String attribute() {
        return mind.attribute();
    }

    @Override
    protected float rate() {
        return 0;
    }

    @Override
    public int maxCharge() {
        return mind.maxCharge();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    @Override
    public MetalMind getStandard() {
        return mind;
    }
}
