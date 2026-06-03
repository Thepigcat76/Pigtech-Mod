package com.portingdeadmods.examplemod.registries;

import ca.weblite.objc.Proxy;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.content.blockentities.ExampleBlockEntity;
import com.portingdeadmods.examplemod.content.blockentities.MachineControllerBlockEntity;
import com.portingdeadmods.examplemod.content.blockentities.MachinePartBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class EMBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);

    public static final Supplier<BlockEntityType<ExampleBlockEntity>> EXAMPLE = BLOCK_ENTITY_TYPES.register("example", () -> new BlockEntityType<>(ExampleBlockEntity::new, EMBlocks.EXAMPLE_BLOCK.get()));
    public static final Supplier<BlockEntityType<MachinePartBlockEntity>> MACHINE_PART = BLOCK_ENTITY_TYPES.register("machine_part", () -> new BlockEntityType<>(MachinePartBlockEntity::new, EMBlocks.MACHINE_PART.get()));
    public static final Supplier<BlockEntityType<MachineControllerBlockEntity>> MACHINE_CONTROLLER = BLOCK_ENTITY_TYPES.register("machine_controller", () -> new BlockEntityType<>(MachineControllerBlockEntity::new, EMBlocks.EXAMPLE_MACHINE.get()));
}
