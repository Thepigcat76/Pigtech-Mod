package com.portingdeadmods.examplemod.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MultiblockGeometry implements SubmitNodeCollector.CustomGeometryRenderer {
    private final BlockStateModel model;
    private final State state;

    public MultiblockGeometry(StandaloneModelKey<BlockStateModel> key, State state) {
        Minecraft mc = Minecraft.getInstance();
        ModelManager modelManager = mc.getModelManager();
        this.model = modelManager.getStandaloneModel(key);
        this.state = state;
    }

    @Override
    public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {
        PoseStack poseStack1 = new PoseStack();
        poseStack1.setIdentity();
        poseStack1.mulPose(pose.pose());
        Minecraft mc = Minecraft.getInstance();

        boolean ambientOcclusion = mc.options.ambientOcclusion().get();
        ModelBlockRenderer blockRenderer = new ModelBlockRenderer(ambientOcclusion, false, mc.getBlockColors());

        BlockQuadOutput output = new QuadOutput(poseStack1, vertexConsumer);

        long blockSeed = state.state.getSeed(state.pos());
        blockRenderer.tesselateBlock(
                output, 0.0F, 0.0F, 0.0F, mc.level, state.pos(), state.state, this.model, blockSeed
        );
    }

    public static class QuadOutput implements BlockQuadOutput {
        private final PoseStack poseStack;
        private final VertexConsumer buffer;

        public QuadOutput(PoseStack poseStack, VertexConsumer buffer) {
            this.poseStack = poseStack;
            this.buffer = buffer;
        }

        @Override
        public void put(float x, float y, float z, @NonNull BakedQuad quad, @NonNull QuadInstance instance) {
            poseStack.pushPose();
            poseStack.translate(x, y, z);

            buffer.putBakedQuad(poseStack.last(), quad, instance);
            poseStack.popPose();
        }
    }

    public record State(BlockState state, BlockPos pos) {
    }

}
