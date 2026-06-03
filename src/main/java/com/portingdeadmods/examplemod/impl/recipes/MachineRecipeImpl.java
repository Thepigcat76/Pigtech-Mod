package com.portingdeadmods.examplemod.impl.recipes;

import com.portingdeadmods.examplemod.api.events.RegisterRecipeLayoutEvent;
import com.portingdeadmods.examplemod.api.recipes.MachineRecipe;
import com.portingdeadmods.examplemod.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.examplemod.api.recipes.RecipeComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.EnergyOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.FluidOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.ItemOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.TimeComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class MachineRecipeImpl implements MachineRecipe<MachineRecipe.Input>, Recipe<MachineRecipe.Input> {
    private RecipeSerializer<? extends MachineRecipeImpl> serializer;
    private RecipeType<? extends MachineRecipeImpl> type;
    private MachineRecipeLayout<MachineRecipeImpl> layout;
    private final Map<String, RecipeComponent> components;
    private final Identifier id;

    public MachineRecipeImpl(Identifier id, Map<String, RecipeComponent> components) {
        this.id = id;
        this.components = components;
    }

    public Identifier id() {
        return id;
    }

    public MachineRecipeImpl(Identifier id) {
        this(id, new LinkedHashMap<>());
    }

    public <R extends RecipeComponent> R getComponent(RecipeComponent.Type<R> type) {
        return this.getLayout().getComponent(this, type);
    }

    @Override
    public boolean matches(@NotNull MachineRecipe.Input input, @NotNull Level level) {
        return matchesItems(input, level);
    }

    private <R extends MachineRecipeImpl> boolean matchesItems(MachineRecipe.Input input, Level level) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.matches((R) this, input, level);
    }

    @Override
    public boolean hasResultItem(HolderLookup.Provider provider) {
        return this.getComponent(ItemOutputComponent.TYPE) != null;
    }

    @Override
    public boolean hasResultFluid(HolderLookup.Provider provider) {
        return this.getComponent(FluidOutputComponent.TYPE) != null;
    }

    @Override
    public boolean hasResultEnergy(HolderLookup.Provider provider) {
        return this.getComponent(EnergyOutputComponent.TYPE) != null;
    }

    @Override
    public Collection<ItemStack> assembleResultItems(Input input, HolderLookup.Provider provider) {
        return createResultItems(input, provider);
    }

    @Override
    public ItemStack assemble(Input input) {
        return createResultItems(input, null).getFirst();
    }

    @Override
    public Collection<FluidStack> assembleResultFluids(@NotNull MachineRecipe.Input container, HolderLookup.Provider provider) {
        return createResultFluids(container, provider);
    }

    @Override
    public int getResultEnergy(HolderLookup.Provider provider) {
        return createResultEnergy(null, provider);
    }

    @Override
    public int assembleResultEnergy(@NotNull MachineRecipe.Input container, HolderLookup.Provider provider) {
        return createResultEnergy(container, provider);
    }

    private <R extends MachineRecipeImpl> int createResultEnergy(MachineRecipe.Input input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultEnergy((R) this, input, provider);
    }

    private <R extends MachineRecipeImpl> List<FluidStack> createResultFluids(MachineRecipe.Input input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultFluids((R) this, input, provider).stream().map(FluidStackTemplate::create).toList();
    }

    private <R extends MachineRecipeImpl> List<ItemStack> createResultItems(MachineRecipe.Input input, HolderLookup.Provider provider) {
        MachineRecipeLayout<R> layout = (MachineRecipeLayout<R>) this.getLayout();
        return layout.createResultItems((R) this, input, provider).stream().map(ItemStackTemplate::create).toList();
    }

    @Override
    public @NonNull RecipeSerializer<? extends MachineRecipeImpl> getSerializer() {
        if (this.serializer == null) {
            this.serializer = getLayout().getRecipeSerializer();
        }
        return this.serializer;
    }

    @Override
    public MachineRecipeLayout<MachineRecipeImpl> getLayout() {
        if (this.layout == null) {
            this.layout = (MachineRecipeLayout<MachineRecipeImpl>) RegisterRecipeLayoutEvent.LAYOUTS.get(this.id);
        }
        return this.layout;
    }

    @Override
    public RecipeType<? extends MachineRecipeImpl> getType() {
        if (this.type == null) {
            this.type = getLayout().getRecipeType();
        }
        return this.type;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public Map<String, RecipeComponent> getComponents() {
        return this.components;
    }

    public static <R extends MachineRecipeImpl> Builder<R> builder(Identifier id, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
        return new Builder<>(id, recipeFactory);
    }

    public static Builder<MachineRecipeImpl> builder(Identifier id) {
        return new Builder<>(id, MachineRecipeImpl::new);
    }

    public static Builder<MachineRecipeImpl> builder(MachineRecipeLayout<MachineRecipeImpl> layout) {
        return new Builder<>(layout.getId(), MachineRecipeImpl::new);
    }

    @Override
    public boolean hasProgress() {
        return this.getComponent(TimeComponent.TYPE) != null;
    }

    @Override
    public int getMaxProgress() {
        return this.getComponent(TimeComponent.TYPE).time();
    }

    public static class Builder<R extends MachineRecipeImpl> {
        private final Identifier id;
        private final BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory;
        private final Map<String, RecipeComponent> components;

        private Builder(Identifier id, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
            this.id = id;
            this.recipeFactory = recipeFactory;
            this.components = new LinkedHashMap<>();
        }

        public Builder<R> component(RecipeComponent component) {
            this.components.put(this.getLayout().getComponentKey(component.type()), component);
            return this;
        }

        private MachineRecipeLayout<?> getLayout() {
            return RegisterRecipeLayoutEvent.LAYOUTS.get(this.id);
        }

        public R build() {
            return this.recipeFactory.apply(this.id, this.components);
        }

    }

}