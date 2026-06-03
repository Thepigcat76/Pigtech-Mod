package com.portingdeadmods.examplemod.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.content.blockentities.ExampleBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.blockentities.PDLBERenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ExampleBERenderer extends PDLBERenderer<ExampleBlockEntity, ExampleBERenderer.State> {
    public ExampleBERenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NonNull State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ExampleBlockEntity blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    }

    private float lastGoodYaw = 0f;

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.translate(-1, 1.2, 0.5);
        Minecraft mc0 = Minecraft.getInstance();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        BlockPos blockPos = state.blockPos;

        double dx = mc0.player.getX() - blockPos.getX(); // x: quad center/world position
        double dy = ((mc0.player.getY() + mc0.player.getEyeHeight()) - (blockPos.getY() + 1.5f));
        double dz = mc0.player.getZ() - blockPos.getZ();

        double distance = Math.sqrt(dx * dx + dz * dz);

        double minDist = 1.55f; // threshold in world units

        float yaw = (float) Math.toDegrees(Math.atan2(dx, dz));
        if (distance > minDist) {
            lastGoodYaw = Mth.rotLerp(0.1f, lastGoodYaw, yaw);
            ExampleMod.LOGGER.debug("Yaw: {}", yaw);
        }
        float pitch = (float) Math.clamp((Math.atan2(dy, distance) * Mth.RAD_TO_DEG), -33.75f, 33.75f); // Camera X rotation (pitch)

// Translate to quad center if needed
        poseStack.translate(1.5, 1.5, 0); // Position in world

// Apply billboarding: yaw, then pitch (note the negatives)
        poseStack.mulPose(Axis.YP.rotationDegrees((lastGoodYaw + 180) % 360));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.translate(-1.5, -1.5, 0);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.translucentMovingBlock(), (pose, vertexConsumer) -> {
            Minecraft mc = Minecraft.getInstance();

            TextureAtlasSprite sprite = mc.getAtlasManager().get(Sheets.BLOCKS_MAPPER.apply(ExampleMod.id("screen")));
            float u0 = sprite.getU0();
            float u1 = sprite.getU1();
            float v0 = sprite.getV0();
            float v1 = sprite.getV1();

            int fullLight = 0xF000F0;

            int x0 = 3;
            int x1 = 0;
            int y0 = 3;
            int y1 = 0;

            int baseAlpha = 155; // min alpha
            int maxAlpha = 255;  // max alpha
            float speed = 16.0f; // controls pulse speed

            float pulse = (float) ((Math.sin(mc.level.getGameTime() / speed) + 1.0) * 0.5); // -> 0..1
            int alpha = (int) (baseAlpha + pulse * (maxAlpha - baseAlpha)); // smoothly vary alpha

            int col = ARGB.color(alpha, 255, 255, 255);

            vertexConsumer.addVertex(pose.pose(), x0, y0, 0)
                    .setColor(col)
                    .setUv(u0, v0)
                    .setUv2(0xF0, 0xF0);

            vertexConsumer.addVertex(pose.pose(), x0, y1, 0)
                    .setColor(col)
                    .setUv(u0, v1)
                    .setUv2(0xF0, 0xF0);

            vertexConsumer.addVertex(pose.pose(), x1, y1, 0)
                    .setColor(col)
                    .setUv(u1, v1)
                    .setUv2(0xF0, 0xF0);

            vertexConsumer.addVertex(pose.pose(), x1, y0, 0)
                    .setColor(col)
                    .setUv(u1, v0)
                    .setUv2(0xF0, 0xF0);

            vertexConsumer.addVertex(pose.pose(), x1, y0, 0).setColor(col).setUv(u1, v0).setUv2(0xF0, 0xF0);
            vertexConsumer.addVertex(pose.pose(), x1, y1, 0).setColor(col).setUv(u1, v1).setUv2(0xF0, 0xF0);
            vertexConsumer.addVertex(pose.pose(), x0, y1, 0).setColor(col).setUv(u0, v1).setUv2(0xF0, 0xF0);
            vertexConsumer.addVertex(pose.pose(), x0, y0, 0).setColor(col).setUv(u0, v0).setUv2(0xF0, 0xF0);
        });

    }

    float lerpAngle(float t, float a, float b) {
        float delta = ((b - a + 540) % 360) - 180;
        return a + t * delta;
    }

    public static class State extends BlockEntityRenderState {

    }
}
