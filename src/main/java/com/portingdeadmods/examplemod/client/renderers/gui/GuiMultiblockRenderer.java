package com.portingdeadmods.examplemod.client.renderers.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.portingdeadmods.examplemod.api.multiblock.MultiblockLayout;
import com.portingdeadmods.examplemod.client.renderers.blockentity.MultiblockGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class GuiMultiblockRenderer extends PictureInPictureRenderer<GuiMultiblockRenderer.State> {
    public GuiMultiblockRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public @NonNull Class<State> getRenderStateClass() {
        return State.class;
    }

    @Override
    protected void renderToTexture(State state, PoseStack poseStack) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.LEVEL);

        poseStack.pushPose();
        {
            AABB renderBox = state.layout.getRenderBox();

            float dx = (float) Math.abs(renderBox.maxX - renderBox.minX);
            float dy = (float) Math.abs(renderBox.maxY - renderBox.minY);
            float dz = (float) Math.abs(renderBox.maxZ - renderBox.minZ);

            poseStack.translate(-(dx / 2.0f), -(dy / 2.0f), -(dz / 2.0f));

            poseStack.translate(dx / 2.0f, dy / 2.0f, dz / 2.0f);

            setupOrthographicProjection(poseStack);

            poseStack.mulPose(Axis.YP.rotationDegrees(minecraft.level.getGameTime() % 360));
            poseStack.translate(-(dx / 2.0f), -(dy / 2.0f), -(dz / 2.0f));

            ModelBlockRenderer blockRenderer = new ModelBlockRenderer(true, true, minecraft.getBlockColors());
            VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.solidMovingBlock());
            BlockQuadOutput output = new MultiblockGeometry.QuadOutput(poseStack, buffer);
            BlockStateModelSet modelSet = minecraft.getModelManager().getBlockStateModelSet();

            List<MultiblockLayout.Layer> layers = state.layout().getLayers();

            BlockPos.MutableBlockPos curPos = new BlockPos.MutableBlockPos();
            for (int i = 0; i < state.renderLayers(); i++) {
                MultiblockLayout.Layer layer = layers.get(i);
                MultiblockLayout.BlockDefinition[][] layerBlocks = layer.layer();
                for (MultiblockLayout.BlockDefinition[] row : layerBlocks) {
                    for (MultiblockLayout.BlockDefinition blockDef : row) {
                        renderBlock(curPos.immutable(), blockDef.defaultBlock().value().defaultBlockState(), output, blockRenderer, modelSet);
                        curPos.move(1, 0, 0);
                    }
                    curPos.setX(0);
                    curPos.move(0, 0, 1);
                }
                curPos.setX(0);
                curPos.setZ(0);
                curPos.move(0, 1, 0);
            }

        }
        poseStack.popPose();
    }

    private void renderBlock(BlockPos pos, BlockState state, BlockQuadOutput output, ModelBlockRenderer renderer, BlockStateModelSet modelSet) {
        renderer.tesselateBlock(
                output,
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ()),
                BlockAndTintGetter.EMPTY,
                pos,
                state,
                modelSet.get(state),
                42L
        );
    }

    private static void setupOrthographicProjection(PoseStack poseStack) {
        // Set up orthographic rendering for the block
        float angle = 36;
        float rotation = 45;

        //poseStack.scale(1, 1, -1);
        poseStack.mulPose(new Quaternionf().rotationY(Mth.DEG_TO_RAD * -180));

        Quaternionf flip = new Quaternionf().rotationZ(Mth.DEG_TO_RAD * 180);
        flip.mul(new Quaternionf().rotationX(Mth.DEG_TO_RAD * angle));

        Quaternionf rotate = new Quaternionf().rotationY(Mth.DEG_TO_RAD * rotation);
        poseStack.mulPose(flip);
        poseStack.mulPose(rotate);

        // Move into the center of the block for the transforms
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0F;
    }

    @Override
    protected @NonNull String getTextureLabel() {
        return "multiblock";
    }

    public record State(Matrix3x2f pose, MultiblockLayout layout, int renderLayers, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
    }
}
