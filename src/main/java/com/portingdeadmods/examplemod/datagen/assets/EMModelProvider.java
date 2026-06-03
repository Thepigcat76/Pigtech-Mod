package com.portingdeadmods.examplemod.datagen.assets;

import com.portingdeadmods.examplemod.ExampleMod;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public final class EMModelProvider extends ModelProvider {
    private final EMItemModelSubProvider itemModelSubProvider;
    private final EMBlockModelSubProvider blockModelSubProvider;

    public EMModelProvider(PackOutput output) {
        super(output, ExampleMod.MODID);
        this.itemModelSubProvider = new EMItemModelSubProvider(ExampleMod.MODID);
        this.blockModelSubProvider = new EMBlockModelSubProvider(ExampleMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.itemModelSubProvider.registerModels(itemModels);
        this.blockModelSubProvider.registerModels(blockModels, itemModels);
    }

    @Override
    protected @NonNull Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.empty();
    }
}
