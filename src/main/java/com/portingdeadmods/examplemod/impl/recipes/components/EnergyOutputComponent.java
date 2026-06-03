package com.portingdeadmods.examplemod.impl.recipes.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;

public record EnergyOutputComponent(int energy) implements RecipeComponent {
    public static final Type<EnergyOutputComponent> TYPE = new Type<>(
            ExampleMod.id("energy_output"),
            RecipeComponent.OUTPUT_ROLE,
            Codec.INT.xmap(EnergyOutputComponent::new, EnergyOutputComponent::energy),
            ByteBufCodecs.INT.map(EnergyOutputComponent::new, EnergyOutputComponent::energy)
    );

    @Override
    public Type<EnergyOutputComponent> type() {
        return TYPE;
    }
}
