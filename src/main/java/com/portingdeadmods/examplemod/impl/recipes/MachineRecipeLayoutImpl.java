package com.portingdeadmods.examplemod.impl.recipes;

import com.portingdeadmods.examplemod.api.recipes.MachineRecipe;
import com.portingdeadmods.examplemod.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MachineRecipeLayoutImpl<R extends MachineRecipe<?> & Recipe<?>> extends MachineRecipeLayout<R> {
    public MachineRecipeLayoutImpl(MachineRecipeLayout.Builder<R> builder) {
        super(builder._id, builder._type, builder._recipeFactory);
        this.components.putAll(builder._components);
        this.defaultComponentValues.putAll(builder._defaultComponentValues);
    }
}
