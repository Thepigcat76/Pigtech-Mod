package com.portingdeadmods.examplemod.compat.jei;

import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.api.multiblock.MultiblockLayout;
import com.portingdeadmods.examplemod.client.renderers.gui.GuiMultiblockRenderer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MultiblockCategory extends AbstractRecipeCategory<MultiblockLayout> {
    public static final IRecipeType<MultiblockLayout> TYPE = IRecipeType.create(ExampleMod.id("multiblock"), MultiblockLayout.class);
    public static final WidgetSprites BUTTON_LAYER_UP_SPRITES = new WidgetSprites(ExampleMod.id("button_layer_up"), ExampleMod.id("button_layer_up_focused"));
    public static final WidgetSprites BUTTON_LAYER_DOWN_SPRITES = new WidgetSprites(ExampleMod.id("button_layer_down"), ExampleMod.id("button_layer_down_focused"));;
    public static final WidgetSprites BUTTON_LAYERS_ALL_SPRITES = new WidgetSprites(ExampleMod.id("button_layer_all"), ExampleMod.id("button_layer_all_focused"));

    private static final float DEFAULT_ZOOM = 20.0f;
    private static final float MIN_ZOOM = 8.0f;
    private static final float MAX_ZOOM = 30.0f;

    private final Map<MultiblockLayout, Float> zoomLevels = new HashMap<>();
    private final Map<MultiblockLayout, Integer> displayedLayers = new HashMap<>();

    public MultiblockCategory(IGuiHelper helper) {
        super(TYPE, Component.literal("Multiblocks"), helper.createDrawableItemLike(Items.ENCHANTING_TABLE), 174, 130);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MultiblockLayout layout, IFocusGroup focuses) {
        this.displayedLayers.put(layout, layout.getLayers().size());
        this.zoomLevels.put(layout, DEFAULT_ZOOM);

        for (MultiblockLayout.BlockWithCount block : layout.getUniqueBlocksWithMaxSize(b -> b.value().asItem().getDefaultMaxStackSize())) {
            ItemStack stack = new ItemStack(block.block().value(), block.count());
            builder.addInputSlot().add(stack).setStandardSlotBackground();
        }
    }

    @Override
    public void draw(MultiblockLayout layout, IRecipeSlotsView view, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        int x = 0;
        int y = 0;

        int size = 116;
        int x0 = 22;
        int y0 = (this.getHeight() - size) / 2;
        guiGraphics.enableScissor(x0, y0, x0 + size, y0 + size);
        {
            var rect = new ScreenRectangle(x - 23, y - 16, this.getWidth() + 32, this.getHeight() + 32);
            var screenBounds = rect.transformMaxBounds(guiGraphics.pose());
            var scissorArea = guiGraphics.peekScissorStack();
            // Pre-apply scissor area
            screenBounds = scissorArea != null ? scissorArea.intersection(screenBounds) : screenBounds;
            guiGraphics.submitPictureInPictureRenderState(new GuiMultiblockRenderer.State(
                    new Matrix3x2f(guiGraphics.pose()),
                    layout,
                    this.getLayers(layout),
                    rect.left(),
                    rect.top(),
                    rect.right(),
                    rect.bottom(),
                    this.getZoomLevel(layout),
                    scissorArea,
                    screenBounds
            ));
        }
        guiGraphics.disableScissor();
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, MultiblockLayout layout, IFocusGroup focuses) {
        super.createRecipeExtras(builder, layout, focuses);

        builder.addInputHandler(new IJeiInputHandler() {
            @Override
            public @NonNull ScreenRectangle getArea() {
                return new ScreenRectangle(0, 0, MultiblockCategory.this.getWidth(), MultiblockCategory.this.getHeight());
            }

            @Override
            public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
                MultiblockCategory.this.setZoomLevel(layout, Math.clamp(MultiblockCategory.this.getZoomLevel(layout) + (float) (scrollDeltaY * 0.3f), MIN_ZOOM, MAX_ZOOM));
                return true;
            }
        });

        int height = 116;

        this.addRenderableWidget(builder, new JeiImageButton(BUTTON_LAYER_UP_SPRITES, 0, height / 2 - 30, 20, 20, () -> this.setLayers(layout, Math.min(layout.getLayers().size(), this.getLayers(layout) + 1))));
        this.addRenderableWidget(builder, new JeiImageButton(BUTTON_LAYER_DOWN_SPRITES, 0,height / 2 - 10, 20, 20, () -> this.setLayers(layout, Math.max(1, this.getLayers(layout) - 1))));
        this.addRenderableWidget(builder, new JeiImageButton(BUTTON_LAYERS_ALL_SPRITES, 0,height / 2 + 10, 20, 20, () -> {
            this.setLayers(layout, layout.getLayers().size());
            this.setZoomLevel(layout, DEFAULT_ZOOM);
        }));

        IScrollGridWidget widget = builder.addScrollGridWidget(builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT), 1, 5);
        widget.setPosition(24 + height, (this.getHeight() - widget.getHeight()) / 2);

    }

    private void setZoomLevel(MultiblockLayout layout, float zoom) {
        this.zoomLevels.put(layout, zoom);
    }

    private void setLayers(MultiblockLayout layout, int layers) {
        this.displayedLayers.put(layout, layers);
    }

    private float getZoomLevel(MultiblockLayout layout) {
        return this.zoomLevels.getOrDefault(layout, DEFAULT_ZOOM);
    }

    private int getLayers(MultiblockLayout layout) {
        return this.displayedLayers.getOrDefault(layout, layout.getLayers().size());
    }

    private <W extends IRecipeWidget & IJeiInputHandler> void addRenderableWidget(IRecipeExtrasBuilder builder, W widget) {
        builder.addWidget(widget);
        builder.addInputHandler(widget);
    }

}
