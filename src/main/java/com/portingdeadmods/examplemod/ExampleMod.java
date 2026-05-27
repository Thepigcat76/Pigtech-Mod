package com.portingdeadmods.examplemod;

import com.portingdeadmods.examplemod.registries.*;
import com.portingdeadmods.portingdeadlibs.api.config.PDLConfigHelper;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(ExampleMod.MODID)
public final class ExampleMod {
    public static final String MODID = "examplemod";
    public static final String MODNAME = "Example Mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::registerCapabilities);

        EMItems.ITEMS.register(modEventBus);
        EMBlocks.BLOCKS.register(modEventBus);
        EMTranslations.TRANSLATIONS.register(modEventBus);
        EMCreativeTabs.TABS.register(modEventBus);
        EMBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EMMenuTypes.MENU_TYPES.register(modEventBus);

        PDLConfigHelper.registerConfig(ExampleModConfig.class, ModConfig.Type.COMMON, modContainer);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, EMBlockEntityTypes.EXAMPLE.get(), (be, ctx) -> be.getHandlerOnSide(Capabilities.Item.BLOCK,  ctx));
    }

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
