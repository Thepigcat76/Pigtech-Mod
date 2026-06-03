package com.portingdeadmods.examplemod.impl.recipes.components;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.List;

public record FluidOutputComponent(List<FluidStackTemplate> outputs) implements RecipeComponent {
    public static final Type<FluidOutputComponent> TYPE = new Type<>(
            ExampleMod.id("fluid_output"),
            RecipeComponent.OUTPUT_ROLE,
            FluidStackTemplate.CODEC.listOf().xmap(FluidOutputComponent::new, FluidOutputComponent::outputs),
            FluidStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).map(FluidOutputComponent::new, FluidOutputComponent::outputs)
    );

    @Override
    public Type<FluidOutputComponent> type() {
        return TYPE;
    }
}
