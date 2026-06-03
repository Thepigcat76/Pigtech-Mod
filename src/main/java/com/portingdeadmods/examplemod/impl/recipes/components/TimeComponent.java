package com.portingdeadmods.examplemod.impl.recipes.components;

import com.mojang.serialization.Codec;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.network.codec.ByteBufCodecs;

public record TimeComponent(int time) implements RecipeComponent {
    public static final Type<TimeComponent> TYPE = new Type<>(
            ExampleMod.id("time"),
            RecipeComponent.OTHER_ROLE,
            Codec.INT.xmap(TimeComponent::new, TimeComponent::time),
            ByteBufCodecs.INT.map(TimeComponent::new, TimeComponent::time)
    );

    @Override
    public Type<?> type() {
        return TYPE;
    }
}