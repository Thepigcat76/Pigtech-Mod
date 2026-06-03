package com.portingdeadmods.examplemod.impl.recipes.components;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public record FluidInputComponent(List<SizedFluidIngredient> inputs) implements RecipeComponent {
    public static final Type<FluidInputComponent> TYPE = new Type<>(
            ExampleMod.id("fluid_input"),
            RecipeComponent.INPUT_ROLE,
            SizedFluidIngredient.CODEC.listOf().xmap(FluidInputComponent::new, FluidInputComponent::inputs),
            SizedFluidIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()).map(FluidInputComponent::new, FluidInputComponent::inputs)
    );

    @Override
    public Type<FluidInputComponent> type() {
        return TYPE;
    }
}
