package com.portingdeadmods.examplemod.content.blockentities;

import com.portingdeadmods.examplemod.content.menus.ExampleMenu;
import com.portingdeadmods.examplemod.registries.EMBlockEntityTypes;
import com.portingdeadmods.examplemod.registries.EMTranslations;
import com.portingdeadmods.portingdeadlibs.api.blockentities.SimpleContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.data.transfer.PDLItemStacksHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public class ExampleBlockEntity extends SimpleContainerBlockEntity implements MenuProvider {
    private final PDLItemStacksHandler itemHandler;

    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EMBlockEntityTypes.EXAMPLE.get(), blockPos, blockState);
        this.itemHandler = addHandler(Capabilities.Item.BLOCK, new PDLItemStacksHandler(9));
        this.itemHandler.setOnChangeFunction((_, _) -> this.setChanged());
    }

    public PDLItemStacksHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return EMTranslations.EXAMPLE_SCREEN_TITLE.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ExampleMenu(i, inventory, this);
    }
}
