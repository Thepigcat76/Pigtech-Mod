package com.portingdeadmods.examplemod.registries;

import com.portingdeadmods.examplemod.EMRegistries;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.multiblock.MultiblockLayout;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class EMMultiblocks {
    public static final DeferredRegister<MultiblockLayout> MULTIBLOCKS = DeferredRegister.create(EMRegistries.MULTIBLOCK_KEY, ExampleMod.MODID);

    public static final DeferredHolder<MultiblockLayout, MultiblockLayout> CRUSHER = MULTIBLOCKS.register("crusher", () -> MultiblockLayout.builder()
            .pattern(builder -> builder
                    .pattern("EEE")
                    .pattern("EWE")
                    .pattern("EEE")
                    .nextLayer()
                    .pattern("WWW")
                    .pattern("WAW")
                    .pattern("WWW")
                    .nextLayer()
                    .pattern("W")
                    .repeatX(3)
                    .repeatZ(3)
            )
            .definition(builder -> builder
                    .define('W', Blocks.DIRT, Blocks.COARSE_DIRT)
                    .define('A', EMBlocks.EXAMPLE_BLOCK, true)
                    .define('E', Blocks.IRON_BLOCK)
            )
            .build());
    public static final DeferredHolder<MultiblockLayout, MultiblockLayout> TEST = MULTIBLOCKS.register("test", () -> MultiblockLayout.builder()
            .pattern(builder -> builder
                    .pattern("C")
                    .repeatX(4)
                    .repeatZ(5)
                    // IMPORTANT: Y repeating must always be called last
                    .repeatY(4)
            )
            .definition(builder -> builder
                    .define('C', EMBlocks.MACHINE_CASING, true)
            )
            .build());

}
