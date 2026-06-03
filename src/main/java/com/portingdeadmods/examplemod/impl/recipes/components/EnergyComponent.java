package com.portingdeadmods.examplemod.impl.recipes.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;

public record EnergyComponent(int energy) implements RecipeComponent {
    public static final Type<EnergyComponent> TYPE = new Type<>(
            ExampleMod.id("energy"),
            RecipeComponent.OTHER_ROLE,
            Codec.INT.xmap(EnergyComponent::new, EnergyComponent::energy),
            ByteBufCodecs.INT.map(EnergyComponent::new, EnergyComponent::energy)
    );

    @Override
    public Type<EnergyComponent> type() {
        return TYPE;
    }
}
