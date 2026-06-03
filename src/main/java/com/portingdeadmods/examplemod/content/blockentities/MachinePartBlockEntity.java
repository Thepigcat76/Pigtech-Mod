package com.portingdeadmods.examplemod.content.blockentities;

import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ResourceHandlerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ResourceHandlerHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MachinePartBlockEntity extends PDLBlockEntity implements ResourceHandlerBlockEntity {
    private static final ResourceHandlerHolder emptyResourceHandlerHolder = new ResourceHandlerHolder();
    private BlockPos controllerPos;

    public MachinePartBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(EMBlockEntityTypes.MACHINE_PART.get(), worldPosition, blockState);
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public ResourceHandlerHolder getHandlerHolder() {
        if (level != null && controllerPos != null) {
            BlockEntity be = level.getBlockEntity(controllerPos);
            if (be instanceof ResourceHandlerBlockEntity resourceHandlerBlockEntity) {
                return resourceHandlerBlockEntity.getHandlerHolder();
            }
        }

        return emptyResourceHandlerHolder;
    }

}
