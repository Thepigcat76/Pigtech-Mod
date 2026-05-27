package com.portingdeadmods.examplemod.content.blocks;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.examplemod.content.blockentities.ExampleBlockEntity;
import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.portingdeadlibs.api.blockentities.PDLBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.PDLEntityBlock;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class ExampleBlock extends PDLEntityBlock {
    public ExampleBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        player.openMenu(BlockUtils.getBE(ExampleBlockEntity.class, level, pos), pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean tickingEnabled() {
        return false;
    }

    @Override
    protected RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Override
    public BlockEntityType<? extends PDLBlockEntity> getBlockEntityType() {
        return EMBlockEntityTypes.EXAMPLE.get();
    }
}
