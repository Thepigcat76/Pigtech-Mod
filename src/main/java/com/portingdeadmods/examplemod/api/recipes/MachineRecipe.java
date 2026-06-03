package com.portingdeadmods.examplemod.api.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MachineRecipe<I extends RecipeInput> {
    Identifier id();

    Map<String, RecipeComponent> getComponents();

    MachineRecipeLayout<? extends MachineRecipe<I>> getLayout();

    <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type);

    @SuppressWarnings("unchecked")
    default <R extends RecipeComponent.Role> Collection<R> getComponentsByRole(RecipeComponent.RoleType<R> role) {
        List<R> components = new ArrayList<>();
        for (RecipeComponent component : this.getComponents().values()) {
            if (component.type().role() == role) {
                components.add((R) component);
            }
        }
        return components;
    }

    boolean hasResultItem(HolderLookup.Provider provider);

    boolean hasResultFluid(HolderLookup.Provider provider);

    boolean hasResultEnergy(HolderLookup.Provider provider);

    Collection<FluidStack> assembleResultFluids(I input, HolderLookup.Provider provider);

    Collection<ItemStack> assembleResultItems(I input, HolderLookup.Provider provider);

    default int getResultEnergy(HolderLookup.Provider provider) {
        return this.assembleResultEnergy(null, provider);
    }

    int assembleResultEnergy(I input, HolderLookup.Provider provider);

    boolean hasProgress();

    int getMaxProgress();

    record Input(List<ItemStack> items, List<FluidStack> fluids) implements RecipeInput {
        public Input(List<ItemStack> items) {
            this(items, List.of());
        }

        public Input(FluidStack fluid) {
            this(List.of(), List.of(fluid));
        }

        public Input(ItemStack item) {
            this(List.of(item));
        }

        public Input() {
            this(List.of());
        }

        @Override
        @Deprecated
        public @NonNull ItemStack getItem(int i) {
            return this.items().get(i);
        }

        @Override
        @Deprecated
        public int size() {
            return this.items().size();
        }

        @Override
        public boolean isEmpty() {
            for(FluidStack fluid : this.fluids) {
                if (!fluid.isEmpty()) {
                    return false;
                }
            }
            return RecipeInput.super.isEmpty();
        }
    }

}