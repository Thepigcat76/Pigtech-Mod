package com.portingdeadmods.examplemod.api.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public interface RecipeComponent {
    Type<?> type();

    record Type<C extends RecipeComponent>(Identifier id, RecipeComponent.RoleType<?> role, Codec<C> codec, StreamCodec<? super RegistryFriendlyByteBuf, C> streamCodec) {
        public Type(Identifier id, RecipeComponent.RoleType<?> role, MapCodec<C> codec, StreamCodec<RegistryFriendlyByteBuf, C> streamCodec) {
            this(id, role, codec.codec(), streamCodec);
        }

        public Type(Identifier id, RecipeComponent.RoleType<?> role) {
            this(id, role, (Codec<C>) null, null);
        }

        public Codec<RecipeComponent> rawCodec() {
            return (Codec<RecipeComponent>) codec;
        }

        public StreamCodec<RegistryFriendlyByteBuf, RecipeComponent> rawStreamCodec() {
            return (StreamCodec<RegistryFriendlyByteBuf, RecipeComponent>) streamCodec;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Type<?> that)
                return Objects.equals(this.id, that.id);
            return false;
        }
    }

    RoleType<InputRole> INPUT_ROLE = new RoleType<>();
    RoleType<OutputRole> OUTPUT_ROLE = new RoleType<>();
    RoleType<OtherRole> OTHER_ROLE = new RoleType<>();

    interface Role {

    }

    interface InputRole extends Role {
        boolean test(MachineRecipe.Input input, boolean strict);
    }

    interface OutputRole extends Role {
    }

    interface OtherRole extends Role {
    }

    class RoleType<R extends Role> {
        private RoleType() {
        }
    }

}