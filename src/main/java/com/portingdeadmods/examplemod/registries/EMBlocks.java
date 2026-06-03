package com.portingdeadmods.examplemod.registries;

import ca.weblite.objc.Proxy;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.content.blocks.ExampleBlock;
import com.portingdeadmods.examplemod.content.blocks.ExampleMachineBlock;
import com.portingdeadmods.portingdeadlibs.api.misc.PDLDeferredRegisterBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EMBlocks {
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(ExampleMod.MODID, EMItems.ITEMS);

    public static final DeferredBlock<ExampleBlock> EXAMPLE_BLOCK = BLOCKS.registerBlockWithItem("example_block", ExampleBlock::new);
    public static final DeferredBlock<Block> MACHINE_CASING = BLOCKS.registerSimpleBlockWithItem("machine_casing");
    public static final DeferredBlock<ExampleMachineBlock> EXAMPLE_MACHINE = BLOCKS.registerBlockWithItem("example_machine", ExampleMachineBlock::new);

    public static final DeferredBlock<Block> MACHINE_PART = BLOCKS.registerBlock("machine_part", Block::new);
}
