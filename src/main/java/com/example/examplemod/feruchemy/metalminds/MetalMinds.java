package com.example.examplemod.feruchemy.metalminds;

import com.example.examplemod.Allomancy;
import com.example.examplemod.feruchemy.metalminds.attributemodifiers.AttributeMetalMind;
import com.example.examplemod.feruchemy.metalminds.attributemodifiers.MetalMindUtils;
import com.example.examplemod.feruchemy.metalminds.attributemodifiers.PewterMind;
import com.example.examplemod.feruchemy.metalminds.attributemodifiers.SteelMind;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MetalMinds {
    public static final DeferredRegister<Item> METAL_MINDS = DeferredRegister.create(ForgeRegistries.ITEMS, Allomancy.MODID);

    public static final RegistryObject<Item> STEEL_MIND = METAL_MINDS.register("steel_mind", () -> new SteelMind(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PEWTER_MIND = METAL_MINDS.register("pewter_mind", () -> new PewterMind(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus modEventBus) {
        registerCreativeItems();
        METAL_MINDS.register(modEventBus);
    }

    private static void registerCreativeItems() {
        List<RegistryObject<Item>> copy = new LinkedList<>(METAL_MINDS.getEntries());
        copy.forEach(item -> METAL_MINDS.register("creative_" + item.getId().getPath(), () -> new CreativeDecorator(new Item.Properties().stacksTo(1), (MetalMind) item.get())));
    }

    @Mod.EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT)
    public static class MetalMindEvents {
        @SubscribeEvent
        public static void onPlayerTick (TickEvent.PlayerTickEvent event) {
            if (event.player.isLocalPlayer()) {
                // METAL_MINDS.getEntries().forEach(item -> MetalMindUtils.removeAllModifiersNotInInventory((AttributeMetalMind) item.get(), event.player));
                // METAL_MINDS.getEntries().forEach(item -> MetalMindUtils.preventDuplicateUUIDs((AttributeMetalMind) item.get(), event.player));
            }
        }
    }
}
