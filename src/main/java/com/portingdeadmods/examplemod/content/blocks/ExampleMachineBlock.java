package com.portingdeadmods.examplemod.content.blocks;

import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.PDLEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ExampleMachineBlock extends PDLEntityBlock {
    public ExampleMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<? extends PDLBlockEntity> getBlockEntityType() {
        return EMBlockEntityTypes.MACHINE_CONTROLLER.get();
    }

    @Override
    protected boolean tickingEnabled() {
        return false;
    }

    @Override
    protected RotationType getRotationType() {
        return RotationType.NONE;
    }

}
