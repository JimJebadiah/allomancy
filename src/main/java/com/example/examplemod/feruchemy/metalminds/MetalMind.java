package com.example.examplemod.feruchemy.metalminds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.TooltipFlag;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class MetalMind extends Item {
    protected static final String STATE_KEY = "state";
    public static final String STORE_UUID_KEY = "store_uuid";
    public static final String TAP_UUID_KEY = "tap_uuid";
    protected static final String PLAYER_UUID_KEY = "player_uuid";

    protected static final Logger LOGGER = LogUtils.getLogger();

    public static Attribute ATTRIBUTE;

    private boolean using;
    private float rate;

    public MetalMind(Item.Properties properties) {
        super(properties.defaultDurability(100));
        rate = 1;
    }

    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(PLAYER_UUID_KEY, player.getUUID());
        super.onCraftedBy(stack, level, player);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            // boolean belongsToPlayer = stack.getOrCreateTag().getUUID(PLAYER_UUID_KEY) == player.getUUID();
            if (!using /*&& belongsToPlayer*/) {
                using = true;
                MetalMindState state = getState(stack);
                if (player.isCrouching()) {
                    state.tap(this, stack);
                } else {
                    state.store(this, stack);
                }
            }

            player.startUsingItem(interactionHand);
        }
        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getState(stack).hasFoil();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (entity instanceof Player player) {
         //   getState(stack).tick(this, player, stack);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        stopUsing(item);
        stopUsing();
        return super.onDroppedByPlayer(item, player);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        this.stopUsing();
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        String text = getState(stack).getHoverText(this);
        if (!Strings.isBlank(text)) {
            components.add(Component.literal(text));
        }

        int charge = tag.contains("charge") ? tag.getInt("charge") : 0;

        components.add(Component.literal("Charge: " + charge + "/" + maxCharge()));
        components.add(Component.literal("Right Click to Store!"));
        components.add(Component.literal("Shift + Right Click to Tap!"));
        super.appendHoverText(stack, level, components, flag);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int duration) {
        this.stopUsing();
    }

    public abstract void store(ItemStack stack);

    public abstract void tap(ItemStack stack);

    public abstract void stopUsing(ItemStack stack);

    public abstract String attribute();

    protected abstract float rate();

    public abstract int maxCharge();

    public int getCharge(ItemStack stack) {
        return stack.getOrCreateTag().getInt("charge");
    }

    private void stopUsing() {
        using = false;
    }

    protected void setState(MetalMindState state, ItemStack stack) {
        stack.getOrCreateTag().putString(STATE_KEY, state.toString());
    }

    public abstract boolean isCreative();

    public abstract MetalMind getStandard();

    protected MetalMindState getState(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(STATE_KEY)) {
            stack.getOrCreateTag().putString(STATE_KEY, MetalMindState.NONE.toString());
        }
        return MetalMindState.valueOf(stack.getTag().getString(STATE_KEY));
    }
}
