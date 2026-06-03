package com.portingdeadmods.examplemod.events;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.events.RegisterRecipeLayoutEvent;
import com.portingdeadmods.examplemod.api.recipes.MachineRecipeLayout;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;

@EventBusSubscriber(modid = ExampleMod.MODID)
public final class CommonEvents {
    @SubscribeEvent
    private static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            ModLoader.postEvent(new RegisterRecipeLayoutEvent());

            for (Map.Entry<Identifier, MachineRecipeLayout<?>> entry : RegisterRecipeLayoutEvent.LAYOUTS.entrySet()) {
                event.register(Registries.RECIPE_SERIALIZER, entry.getKey(), () -> entry.getValue().getRecipeSerializer());
            }
        }
    }

}
