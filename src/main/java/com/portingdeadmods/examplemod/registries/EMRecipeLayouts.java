package com.portingdeadmods.examplemod.registries;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.recipes.MachineRecipeLayout;
import com.portingdeadmods.examplemod.impl.recipes.*;
import com.portingdeadmods.examplemod.impl.recipes.components.EnergyComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.ItemInputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.ItemOutputComponent;
import com.portingdeadmods.examplemod.impl.recipes.components.TimeComponent;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class EMRecipeLayouts {
    public static final Map<Identifier, MachineRecipeLayout<?>> LAYOUTS = new HashMap<>();

    public static final MachineRecipeLayoutImpl<?> CRUSHER = registerLayout("crusher", builder -> builder
            .component(ItemInputComponent.TYPE, "input")
            .component(ItemOutputComponent.TYPE, "output")
            .component(EnergyComponent.TYPE, "energy", new EnergyComponent(10))
            .component(TimeComponent.TYPE, "duration", new TimeComponent(200)));

    public static MachineRecipeLayoutImpl<?> registerLayout(String path, UnaryOperator<MachineRecipeLayout.Builder<?>> layoutBuilder) {
        Identifier id = ExampleMod.id(path);
        MachineRecipeLayoutImpl<?> layout = layoutBuilder.apply(MachineRecipeLayout.builder(id)).build(MachineRecipeLayoutImpl::new);
        LAYOUTS.put(id, layout);
        return layout;
    }
}
