package com.portingdeadmods.examplemod.datagen.assets;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.registries.EMBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;

public class EMBlockModelSubProvider {
    public static final Identifier EMPTY_BLOCK_MODEL = ExampleMod.id("block/empty");
    private final String modid;

    public EMBlockModelSubProvider(String modid) {
        this.modid = modid;
    }

    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.modelOutput.accept(EMPTY_BLOCK_MODEL, () -> {
            JsonObject object = new JsonObject();
            object.addProperty("loader", "neoforge:empty");
            return object;
        });

        blockModels.createTrivialCube(EMBlocks.EXAMPLE_BLOCK.get());
        blockModels.createTrivialCube(EMBlocks.MACHINE_CASING.get());

        emitObjModelWithEmptyState(EMBlocks.EXAMPLE_MACHINE.get(), blockModels);
    }

    public void emitObjModelWithEmptyState(Block block, BlockModelGenerators blockModels) {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(block);

        ObjModelBuilder builder = new ObjModelBuilder()
                .modelLocation(ExampleMod.id("models/" + modelLocation.getPath() + ".obj"))
                .automaticCulling(false)
                .shadeQuads(true)
                .flipV(true)
                .emissiveAmbient(true);

        blockModels.modelOutput.accept(modelLocation, () -> builder.toJson(ModelTemplates.create()
                .createBaseTemplate(modelLocation, ImmutableMap.<TextureSlot, Material>builder()
                        .put(TextureSlot.TEXTURE, new Material(ExampleMod.id("block/example_block")))
                        .build()
                )));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(EMPTY_BLOCK_MODEL)));
    }

}
