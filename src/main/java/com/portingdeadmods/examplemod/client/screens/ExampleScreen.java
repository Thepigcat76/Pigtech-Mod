package com.portingdeadmods.examplemod.client.screens;

import com.portingdeadmods.examplemod.content.menus.ExampleMenu;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PDLAbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ExampleScreen extends PDLAbstractContainerScreen<ExampleMenu> {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");

    public ExampleScreen(ExampleMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, this.getBackgroundTexture(), this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public @NotNull Identifier getBackgroundTexture() {
        return BACKGROUND_TEXTURE;
    }
}
