package com.portingdeadmods.examplemod.datagen.assets;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.registries.EMBlocks;
import com.portingdeadmods.examplemod.registries.EMItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class EMItemModelSubProvider {
    private final String modid;

    public EMItemModelSubProvider(String modid) {
        this.modid = modid;
    }

    protected void registerModels(ItemModelGenerators models) {
        EMBlocks.BLOCKS.getBlockItems().stream().map(Supplier::get).map(BlockItem::getBlock).forEach(block -> emitSimpleBlockItem(models, block, block.asItem()));

        emitFlatItem(models, EMItems.EXAMPLE_ITEM);
        emitExistingModelItem(models, EMItems.CONSTRUCTOR);
    }

    public void emitFlatItem(ItemModelGenerators generators, ItemLike item) {
        Identifier identifier = ModelTemplates.FLAT_ITEM.create(item.asItem(), TextureMapping.layer0(item.asItem()), generators.modelOutput);
        generators.itemModelOutput.accept(item.asItem(), ItemModelUtils.plainModel(identifier));
    }

    public void emitExistingModelItem(ItemModelGenerators generators, ItemLike item) {
        generators.itemModelOutput.accept(item.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item.asItem())));
    }

    private static void emitSimpleBlockItem(ItemModelGenerators itemModelGenerators, Block block, Item blockItem) {
        itemModelGenerators.itemModelOutput.accept(blockItem, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block)));
    }
}
