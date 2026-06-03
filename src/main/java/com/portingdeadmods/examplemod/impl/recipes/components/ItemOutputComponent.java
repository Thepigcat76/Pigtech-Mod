package com.portingdeadmods.examplemod.impl.recipes.components;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;

public record ItemOutputComponent(List<ItemStackTemplate> outputs) implements RecipeComponent {
    public static final Type<ItemOutputComponent> TYPE = new Type<>(
            ExampleMod.id("item_output"),
            RecipeComponent.OUTPUT_ROLE,
            ItemStackTemplate.CODEC.listOf().xmap(ItemOutputComponent::new, ItemOutputComponent::outputs).fieldOf("item_outputs"),
            ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemOutputComponent::new, ItemOutputComponent::outputs)
    );

    @Override
    public Type<ItemOutputComponent> type() {
        return TYPE;
    }
}
