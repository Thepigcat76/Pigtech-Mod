package com.portingdeadmods.examplemod;

import com.portingdeadmods.examplemod.client.renderers.blockentity.ExampleBERenderer;
import com.portingdeadmods.examplemod.client.renderers.blockentity.MachineControllerBERenderer;
import com.portingdeadmods.examplemod.client.screens.ExampleScreen;
import com.portingdeadmods.examplemod.client.renderers.gui.GuiMultiblockRenderer;
import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.examplemod.registries.EMMenuTypes;
import com.portingdeadmods.portingdeadlibs.api.config.PDLConfigHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ExampleMod.MODID, dist = Dist.CLIENT)
public final class ExampleModClient {
    public ExampleModClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::registerMenuScreens);
        modEventBus.addListener(this::registerBERenderers);
        modEventBus.addListener(this::registerPip);

        PDLConfigHelper.registerConfig(ExampleModClientConfig.class, ModConfig.Type.CLIENT, container);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(EMMenuTypes.EXAMPLE.get(), ExampleScreen::new);
    }

    private void registerPip(RegisterPictureInPictureRenderersEvent event) {
        event.register(GuiMultiblockRenderer.State.class, GuiMultiblockRenderer::new);
    }

    private void registerBERenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EMBlockEntityTypes.MACHINE_CONTROLLER.get(), MachineControllerBERenderer::new);
        event.registerBlockEntityRenderer(EMBlockEntityTypes.EXAMPLE.get(), ExampleBERenderer::new);
    }
}
