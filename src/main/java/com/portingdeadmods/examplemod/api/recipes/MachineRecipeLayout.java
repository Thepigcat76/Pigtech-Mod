package com.portingdeadmods.examplemod.api.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.portingdeadmods.examplemod.impl.recipes.components.EnergyOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.FluidOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.ItemOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.MachineRecipeImpl;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MachineRecipeLayout<R extends MachineRecipe<?> & Recipe<?>> {
    private final Identifier id;
    private final BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory;
    private RecipeType<@NonNull R> recipeType;
    private RecipeSerializer<@NonNull R> recipeSerializer;
    protected final Map<RecipeComponent.Type<?>, String> components;
    protected final Map<RecipeComponent.Type<?>, Supplier<? extends RecipeComponent>> defaultComponentValues;

    public MachineRecipeLayout(Identifier id, RecipeType<@NonNull R> recipeType, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
        this.id = id;
        this.recipeFactory = recipeFactory;
        this.components = new LinkedHashMap<>();
        this.defaultComponentValues = new LinkedHashMap<>();
        this.recipeType = recipeType;
    }

    public MachineRecipeLayout(Identifier id, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
        this(id, null, recipeFactory);
    }

    public String getComponentKey(RecipeComponent.Type<?> componentType) {
        return this.components.get(componentType);
    }

    public Map<RecipeComponent.Type<?>, String> getComponents() {
        return components;
    }

    public Identifier getId() {
        return id;
    }

    private MapCodec<R> createMapCodec(BiFunction<Identifier, Map<String, RecipeComponent>, R> factory) {
        return new MapCodec<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return components.values().stream().map(ops::createString);
            }

            @Override
            public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                Map<String, RecipeComponent> recipeComponents = new LinkedHashMap<>();

                for (Map.Entry<RecipeComponent.Type<?>, String> entry : components.entrySet()) {
                    T val = input.get(entry.getValue());
                    RecipeComponent.Type<?> type = entry.getKey();
                    Codec<? extends RecipeComponent> codec = type.codec();
                    Supplier<? extends RecipeComponent> defaultComponentValue = defaultComponentValues.get(entry.getKey());
                    if (codec != null) {
                        DataResult<? extends Pair<? extends RecipeComponent, T>> result = codec.decode(ops, val);
                        if (result.isSuccess()) {
                            RecipeComponent component = result.getOrThrow().getFirst();
                            recipeComponents.put(entry.getValue(), component);
                        } else {
                            if (defaultComponentValue != null) {
                                recipeComponents.put(entry.getValue(), defaultComponentValue.get());
                            } else {
                                return DataResult.error(() -> "Failed to decode Recipe Component: " + result.error().get().message(), factory.apply(id, recipeComponents));
                            }
                        }
                    } else {
                        if (defaultComponentValue != null) {
                            recipeComponents.put(entry.getValue(), defaultComponentValue.get());
                        } else {
                            return DataResult.error(() -> "Failed to decode Recipe Component, neither a codec nor default value was provided for component of type: " + entry.getValue(), factory.apply(id, recipeComponents));
                        }
                    }
                }
                return DataResult.success(factory.apply(id, recipeComponents));
            }

            @Override
            public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                for (Map.Entry<String, ? extends RecipeComponent> entry : input.getComponents().entrySet()) {
                    RecipeComponent.Type<?> type = entry.getValue().type();
                    Codec<RecipeComponent> rawCodec = type.rawCodec();
                    if (rawCodec != null) {
                        DataResult<T> result = rawCodec.encodeStart(ops, entry.getValue());
                        if (result.isSuccess()) {
                            if (entry.getKey() == null) {
                                System.out.println("Oops key is null");
                            }
                            prefix.add(entry.getKey(), result.getOrThrow());
                        } else {
                            throw new IllegalStateException("Failed to encode recipe");
                        }
                    }
                }
                return prefix;
            }
        };
    }

    public R createRecipe(Identifier id, Map<String, RecipeComponent> components) {
        return this.recipeFactory.apply(id, components);
    }

    public RecipeSerializer<@NonNull R> getRecipeSerializer() {
        if (this.recipeSerializer == null) {
            MapCodec<R> mapCodec = this.createMapCodec(this::createRecipe);
            StreamCodec<RegistryFriendlyByteBuf, @NonNull R> streamCodec = ByteBufCodecs.fromCodecWithRegistriesTrusted(mapCodec.codec()).cast();
            this.recipeSerializer = new RecipeSerializer<>(mapCodec, streamCodec);
        }
        return this.recipeSerializer;
    }

    public RecipeType<@NonNull R> getRecipeType() {
        if (this.recipeType == null) {
            this.recipeType = RecipeType.simple(this.id);
        }
        return this.recipeType;
    }

    /* Recipe related methods */

    public boolean matches(R recipe, MachineRecipe.Input input, Level level) {
        Collection<RecipeComponent.InputRole> inputComps = recipe.getComponentsByRole(RecipeComponent.INPUT_ROLE);
        if (inputComps != null) {
            for (RecipeComponent.InputRole inputComp : inputComps) {
                if (!inputComp.test(input, false)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Collection<ItemStackTemplate> createResultItems(R recipe, @Nullable MachineRecipe.Input input, HolderLookup.Provider provider) {
        ItemOutputComponent output = recipe.getComponent(ItemOutputComponent.TYPE);
        if (output != null) {
            return output.outputs();
        }
        return List.of();
    }

    public Collection<FluidStackTemplate> createResultFluids(R recipe, @Nullable MachineRecipe.Input input, HolderLookup.Provider provider) {
        FluidOutputComponent output = recipe.getComponent(FluidOutputComponent.TYPE);
        if (output != null) {
            return output.outputs();
        }
        return List.of();
    }

    public int createResultEnergy(R recipe, @Nullable MachineRecipe.Input input, HolderLookup.Provider provider) {
        EnergyOutputComponent output = recipe.getComponent(EnergyOutputComponent.TYPE);
        if (output != null) {
            return output.energy();
        }
        return 0;
    }

    public <C extends RecipeComponent> C getComponent(R recipe, RecipeComponent.Type<C> type) {
        for (RecipeComponent value : recipe.getComponents().values()) {
            if (value.type().equals(type)) {
                return (C) value;
            }
        }
        return null;
    }

    public static <R extends MachineRecipe<?> & Recipe<?>> Builder<R> builder(Identifier id, RecipeType<@NonNull R> recipeType, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
        return new Builder<>(id, recipeType, recipeFactory);
    }

    public static <R extends MachineRecipe<?> & Recipe<?>> Builder<R> builder(Identifier id, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
        return new Builder<>(id, null, recipeFactory);
    }

    public static Builder<MachineRecipeImpl> builder(Identifier id) {
        return new Builder<>(id, null, MachineRecipeImpl::new);
    }

    public static class Builder<R extends MachineRecipe<?> & Recipe<?>> {
        public final Identifier _id;
        public final RecipeType<R> _type;
        public final BiFunction<Identifier, Map<String, RecipeComponent>, R> _recipeFactory;
        public final Map<RecipeComponent.Type<?>, String> _components;
        public final Map<RecipeComponent.Type<?>, Supplier<? extends RecipeComponent>> _defaultComponentValues;

        private Builder(Identifier id, RecipeType<R> type, BiFunction<Identifier, Map<String, RecipeComponent>, R> recipeFactory) {
            this._id = id;
            this._type = type;
            this._recipeFactory = recipeFactory;
            this._components = new HashMap<>();
            this._defaultComponentValues = new HashMap<>();
        }

        public <C extends RecipeComponent> Builder<R> component(RecipeComponent.Type<C> type, String id) {
            this._components.put(type, id);
            return this;
        }

        public <C extends RecipeComponent> Builder<R> component(RecipeComponent.Type<C> type, String id, Supplier<C> defaultComponent) {
            this.component(type, id);
            this._defaultComponentValues.put(type, defaultComponent);
            return this;
        }

        public <C extends RecipeComponent> Builder<R> component(RecipeComponent.Type<C> type, String id, C defaultComponent) {
            this.component(type, id);
            this._defaultComponentValues.put(type, () -> defaultComponent);
            return this;
        }

        public <L extends MachineRecipeLayout<R>> L build(Function<Builder<R>, L> factory) {
            return factory.apply(this);
        }

    }

}