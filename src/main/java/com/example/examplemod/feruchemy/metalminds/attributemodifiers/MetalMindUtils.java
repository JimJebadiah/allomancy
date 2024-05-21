package com.example.examplemod.feruchemy.metalminds.attributemodifiers;

import com.example.examplemod.Allomancy;
import com.example.examplemod.feruchemy.metalminds.MetalMind;
import com.example.examplemod.feruchemy.metalminds.MetalMinds;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.*;

public class MetalMindUtils {
    private static Logger LOGGER = LogUtils.getLogger();

    private static CompoundTag dummyTag = new CompoundTag();
    private static final Set<UUID> set = new HashSet<>();


    public static void applyModifier(AttributeInstance attr, CompoundTag modifier, UUID uuid, AttributeMetalMind mind, boolean boost) {
        set.add(uuid);
        CompoundTag tag = new CompoundTag();
        tag.merge(modifier);
        tag.remove("UUID");
        tag.putUUID("UUID", uuid);
        AttributeModifier attrModifier = AttributeModifier.load(tag);

        if (!attr.hasModifier(attrModifier)) {
            attr.addTransientModifier(attrModifier);
        }
    }

    public static void removeModifier(AttributeInstance attr, UUID uuid, AttributeMetalMind mind) {
        removeModifierConcurrent(attr, uuid);
        set.remove(uuid);
    }

    public static void removeModifierConcurrent(AttributeInstance attr, UUID uuid) {
        attr.removeModifier(uuid);
    }

    private static ArrayList<UUID> dummyList = new ArrayList<>();
    public static void removeAllModifiersNotInInventory(AttributeMetalMind mind, Player player) {
        findUUIDSInInventory(mind, player, set, dummyList);
        set.stream().filter(uuid -> !dummyList.contains(uuid)).forEach(uuid -> removeModifierConcurrent(player.getAttribute(mind.attributeInstance()), uuid));
        set.removeIf(uuid -> !dummyList.contains(uuid));
        dummyList.clear();
    }

    public static void findUUIDSInInventory(AttributeMetalMind mind, Player player, Set<UUID> set, List<UUID> list) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.is(mind)) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains(MetalMind.STORE_UUID_KEY)) {
                    UUID store = UUID.fromString(tag.getString(MetalMind.STORE_UUID_KEY));
                    if (set.contains(store)) list.add(store);
                }
                if (tag.contains(MetalMind.TAP_UUID_KEY)) {
                    UUID tap = UUID.fromString(tag.getString(MetalMind.TAP_UUID_KEY));
                    if (set.contains(tap)) list.add(tap);
                }
            }
        }
    }

    // O(n^2) where n is number of inventory slots
    public static void preventDuplicateUUIDs(AttributeMetalMind mind, Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            for (int j = 0; j < inventory.getContainerSize(); j++) {
                ItemStack stack = inventory.getItem(i);
                ItemStack stack1 = inventory.getItem(j);
                if (stack.hasTag() && stack1.hasTag() && stack.is(mind) && stack1.is(mind)) {
                    CompoundTag tag = stack.getTag();
                    CompoundTag tag1 = stack1.getTag();
                    check(tag, tag1, player, mind, MetalMind.STORE_UUID_KEY);
                    check(tag, tag1, player, mind, MetalMind.TAP_UUID_KEY);
                }
            }
        }
    }

    private static void check(CompoundTag tag, CompoundTag tag1, Player player, AttributeMetalMind mind, String key) {
        if (tag.contains(key) && tag1.contains(key)) {
            UUID store1 = UUID.fromString(tag.getString(key));
            UUID store2 = UUID.fromString(tag1.getString(key));
            if (store1.equals(store2)) {
                removeModifier(player.getAttribute(mind.attributeInstance()), store1, mind);
                removeModifier(player.getAttribute(mind.attributeInstance()), store2, mind);
                store2 = UUID.randomUUID();
                tag1.putString(key, store2.toString());
            }
        }
    }
}
