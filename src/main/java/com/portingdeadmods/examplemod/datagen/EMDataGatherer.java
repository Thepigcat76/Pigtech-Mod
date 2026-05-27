package com.portingdeadmods.examplemod.datagen;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.datagen.assets.EMBlockModelSubProvider;
import com.portingdeadmods.examplemod.datagen.assets.EMEnUsLangProvider;
import com.portingdeadmods.examplemod.datagen.assets.EMItemModelSubProvider;
import com.portingdeadmods.examplemod.datagen.assets.EMModelProvider;
import com.portingdeadmods.examplemod.datagen.data.EMBlockLootTableProvider;
import com.portingdeadmods.examplemod.datagen.data.EMRecipeProvider;
import com.portingdeadmods.examplemod.datagen.data.EMTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ExampleMod.MODID)
public final class EMDataGatherer {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new EMModelProvider(packOutput));
        generator.addProvider(true, new EMEnUsLangProvider(packOutput));

        EMTagsProvider.createTagProviders(generator, packOutput, lookupProvider);
        generator.addProvider(true, new EMRecipeProvider.Runner(packOutput, lookupProvider));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(EMBlockLootTableProvider::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
    }
}
