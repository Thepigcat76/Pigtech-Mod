package com.portingdeadmods.examplemod.api.events;

import com.portingdeadmods.examplemod.api.recipes.MachineRecipeLayout;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashMap;
import java.util.Map;

public class RegisterRecipeLayoutEvent extends Event implements IModBusEvent {
    public static final Map<Identifier, MachineRecipeLayout<?>> LAYOUTS = new HashMap<>();

    public <L extends MachineRecipeLayout<?>> L register(Identifier id, L layout) {
        LAYOUTS.put(id, layout);
        return layout;
    }
}
