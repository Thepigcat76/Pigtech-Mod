package com.portingdeadmods.examplemod.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.examplemod.ExampleMod;
import com.portingdeadmods.examplemod.content.blockentities.MachineControllerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.ao.EnhancedBlockModelLighter;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
public class MachineControllerBERenderer implements BlockEntityRenderer<MachineControllerBlockEntity, MachineControllerRenderState> {
    BlockModelLighter lighter;

    public MachineControllerBERenderer(BlockEntityRendererProvider.Context context) {
        this.lighter = EnhancedBlockModelLighter.newInstance();
    }

    @Override
    public void extractRenderState(MachineControllerBlockEntity blockEntity, MachineControllerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.state = blockEntity.getBlockState();
    }

    @Override
    public MachineControllerRenderState createRenderState() {
        return new MachineControllerRenderState();
    }

    private static final Identifier MODEL_ID = ExampleMod.id("block/example_machine");
    public static final StandaloneModelKey<BlockStateModel> MODEL_KEY = new StandaloneModelKey<>(MODEL_ID::toString);

    @SubscribeEvent
    private static void registerStandalone(ModelEvent.RegisterStandalone event) {
        event.register(MODEL_KEY, SimpleUnbakedStandaloneModel.blockStateModel(MODEL_ID));
    }

    public static int calculateCombinedLightFor3x3x3(Level level, BlockPos controllerPos) {
        int minLight = Integer.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 2; dy++) { // e.g. controller is at (x, y, z), bottom-center, so dy starts at 0
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = controllerPos.offset(dx, dy, dz);
                    int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
                    int skyLight = level.getBrightness(LightLayer.SKY, pos);
                    int packed = (skyLight << 20) | (blockLight << 4); // matches OverlayTexture or Minecraft's packed method
                    if (packed < minLight) {
                        minLight = packed;
                    }
                }
            }
        }
        return minLight;
    }

    @Override
    public void submit(MachineControllerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);

        ClientLevel level = Minecraft.getInstance().level;
//        submitNodeCollector.submitModel(
//                poseStack, RenderTypes.solidMovingBlock(), modelParts, BlockModelRenderState.EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0
//        );

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.solidMovingBlock(), new MultiblockGeometry(MODEL_KEY, new MultiblockGeometry.State(state.state, state.blockPos)));

        poseStack.popPose();
    }
}
