package com.portingdeadmods.examplemod.impl.recipes.components;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public record ItemInputComponent(List<SizedIngredient> inputs) implements RecipeComponent {
    public static final Type<ItemInputComponent> TYPE = new Type<>(
            ExampleMod.id("item_input"),
            RecipeComponent.INPUT_ROLE,
            SizedIngredient.NESTED_CODEC.listOf().xmap(ItemInputComponent::new, ItemInputComponent::inputs),
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemInputComponent::new, ItemInputComponent::inputs)
    );

    @Override
    public Type<ItemInputComponent> type() {
        return TYPE;
    }
}
