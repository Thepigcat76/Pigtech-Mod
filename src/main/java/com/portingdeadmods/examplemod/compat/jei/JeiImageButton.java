package com.portingdeadmods.examplemod.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;

public class JeiImageButton implements IRecipeWidget, IJeiInputHandler {
    private final ScreenRectangle area;
    private final WidgetSprites sprites;
    private final Runnable onPress;
    private boolean enabled = true;
    private boolean hovered = false;

    public JeiImageButton(WidgetSprites sprites, int x, int y, int width, int height, Runnable onPress) {
        this.sprites = sprites;
        this.onPress = onPress;
        this.area = new ScreenRectangle(x, y, width, height);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHovered() {
        return hovered;
    }

    @Override
    public void drawWidget(GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.hovered = guiGraphics.containsPointInScissor((int) mouseX, (int) mouseY) && new ScreenRectangle(0, 0, this.getArea().width(), this.getArea().height()).containsPoint((int) mouseX, (int) mouseY);

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites.get(this.enabled, this.hovered), 0, 0, this.getArea().width(), this.getArea().height());
    }

    @Override
    public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
        if (input.getKey().getValue() == InputConstants.MOUSE_BUTTON_LEFT && this.isHovered()) {
            if (!input.isSimulate()) {
                this.onPress.run();
            }
            return true;
        }
        return IJeiInputHandler.super.handleInput(mouseX, mouseY, input);
    }

    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(this.getArea().left(), this.getArea().top());
    }

    @Override
    public ScreenRectangle getArea() {
        return this.area;
    }

}
