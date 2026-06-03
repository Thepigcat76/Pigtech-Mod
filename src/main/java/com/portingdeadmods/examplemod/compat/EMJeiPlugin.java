package com.portingdeadmods.examplemod.compat;

import com.portingdeadmods.examplemod.EMRegistries;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.compat.jei.MultiblockCategory;
import com.portingdeadmods.examplemod.registries.EMMultiblocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.List;

@JeiPlugin
public final class EMJeiPlugin implements IModPlugin {
    public static final Identifier UID = ExampleMod.id("jei_plugin");

    @Override
    public @NonNull Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MultiblockCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MultiblockCategory.TYPE, EMRegistries.MULTIBLOCK.stream().toList());
    }

}
