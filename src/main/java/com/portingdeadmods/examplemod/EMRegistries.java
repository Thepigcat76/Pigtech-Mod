package com.portingdeadmods.examplemod;

import com.portingdeadmods.examplemod.api.multiblock.MultiblockLayout;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public final class EMRegistries {
    public static final ResourceKey<Registry<MultiblockLayout>> MULTIBLOCK_KEY = ResourceKey.createRegistryKey(ExampleMod.id("multiblock"));
    public static final Registry<MultiblockLayout> MULTIBLOCK = new RegistryBuilder<>(MULTIBLOCK_KEY).create();
}
