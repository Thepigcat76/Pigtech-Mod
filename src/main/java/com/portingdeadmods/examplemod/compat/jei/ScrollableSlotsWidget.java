package com.portingdeadmods.examplemod.compat.jei;

import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import net.minecraft.client.gui.navigation.ScreenPosition;

import java.util.Optional;

public class ScrollableSlotsWidget implements ISlottedRecipeWidget {
    @Override
    public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
        return Optional.empty();
    }

    @Override
    public ScreenPosition getPosition() {
        return null;
    }
}
