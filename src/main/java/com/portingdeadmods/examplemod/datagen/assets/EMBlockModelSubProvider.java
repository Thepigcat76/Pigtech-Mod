package com.portingdeadmods.examplemod.datagen.assets;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.registries.EMBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class EMBlockModelSubProvider {
    private final String modid;

    public EMBlockModelSubProvider(String modid) {
        this.modid = modid;
    }

    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(EMBlocks.EXAMPLE_BLOCK.get());
    }
}
