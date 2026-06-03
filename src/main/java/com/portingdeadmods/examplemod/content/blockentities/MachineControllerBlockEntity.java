package com.portingdeadmods.examplemod.content.blockentities;

import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MachineControllerBlockEntity extends PDLBlockEntity {
    public MachineControllerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(EMBlockEntityTypes.MACHINE_CONTROLLER.get(), worldPosition, blockState);
    }
}
