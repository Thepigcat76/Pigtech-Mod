package com.portingdeadmods.examplemod.api.multiblock;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class MultiblockLayout {
    private final List<Layer> layers;
    private final List<BlockWithCount> uniqueBlocks;
    private final AABB cachedRenderBox;

    private MultiblockLayout(Builder builder) {
        List<BlockWithCount> uniqueBlocks = new ArrayList<>();
        this.computeUniqueBlocks(uniqueBlocks, builder.layers, builder.definition);

        this.uniqueBlocks = ImmutableList.copyOf(uniqueBlocks);
        this.layers = builder.layers.stream()
                .map(pLayer -> Layer.build(pLayer, builder.definition))
                .toList();
        this.cachedRenderBox = this.computeRenderBox();
    }

    public List<BlockWithCount> getUniqueBlocks() {
        return uniqueBlocks;
    }

    public List<BlockWithCount> getUniqueBlocksWithMaxSize(Function<Holder<Block>, Integer> maxSizeFunction) {
        List<BlockWithCount> blocks = new ArrayList<>(this.uniqueBlocks.size() * 2);
        for (BlockWithCount uniqueBlock : this.uniqueBlocks) {
            int count = uniqueBlock.count();
            int maxSize = maxSizeFunction.apply(uniqueBlock.block());
            int remainder = count % maxSize;
            count -= remainder;
            count /= maxSize;
            for (int i = 0; i < count; i++) {
                blocks.add(new BlockWithCount(uniqueBlock.block(), maxSize));
            }
            blocks.add(new BlockWithCount(uniqueBlock.block(), remainder));
        }
        return blocks;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public AABB getRenderBox() {
        return this.cachedRenderBox;
    }

    private void computeUniqueBlocks(List<BlockWithCount> uniqueBlocks, List<ProtoLayer> layers, Map<Character, BlockDefinition> definition) {
        Map<Holder<Block>, Integer> blocksWithCount = new HashMap<>();
        for (ProtoLayer _layer : layers) {
            char[][] layer = _layer.layer();
            for (char[] row : layer) {
                for (char key : row) {
                    BlockDefinition blockDefinition = definition.get(key);
                    blocksWithCount.compute(blockDefinition.defaultBlock(), (_, v) -> (v == null ? 0 : v) + 1);
                }
            }
        }

        blocksWithCount.entrySet().stream()
                .map(e -> new BlockWithCount(e.getKey(), e.getValue()))
                .forEach(uniqueBlocks::add);

    }

    private AABB computeRenderBox() {
        int maxRowSize = 0;
        int maxColSize = 0;
        for (Layer layer : layers) {
            BlockDefinition[][] layerBlocks = layer.layer();
            // Calculate maximum row size
            for (BlockDefinition[] row : layerBlocks) {
                if (row.length > maxRowSize) {
                    maxRowSize = row.length;
                }

//                int rowSize = 0;
//                for (int x = 0; x < row.length; x++) {
//                    BlockDefinition block = row[x];
//                    if (!block.isEmpty()) {
//                        rowSize = x + 1;
//                    }
//                }
//
//                if (rowSize > maxRowSize) {
//                    maxRowSize = rowSize;
//                }

            }

            if (layer.layer().length > maxColSize) {
                maxColSize = layer.layer().length;
            }

            //for (int x = 0; x < maxRowSize; x++) {

//                int colSize = 0;
//                for (int y = 0; y < layer.layer().length; y++) {
//                    if (x < layer.layer()[y].length) {
//                        BlockDefinition block = layer.layer[y][x];
//                        if (!block.isEmpty()) {
//                            colSize = y + 1;
//                        }
//                    }
//                }
//
//                if (colSize > maxColSize) {
//                    maxColSize = colSize;
//                }
            //}

        }
        return new AABB(0, 0, 0, maxRowSize, layers.size(), maxColSize);
    }

    public static MultiblockLayout.Builder builder() {
        return new MultiblockLayout.Builder();
    }

    public static class Builder {
        private final List<ProtoLayer> layers = new ArrayList<>();
        private final Set<Character> blocks = new HashSet<>();
        private final Map<Character, BlockDefinition> definition = new HashMap<>();

        public Builder pattern(UnaryOperator<MultiblockLayout.LayoutBuilder> layoutBuilder) {
            LayoutBuilder builder = layoutBuilder.apply(new LayoutBuilder(this));
            builder.nextLayer();
            return this;
        }

        public Builder definition(UnaryOperator<MultiblockLayout.DefinitionBuilder> definitionBuilder) {
            DefinitionBuilder builder = new DefinitionBuilder(this);
            definitionBuilder.apply(builder);
            Preconditions.checkState(builder.controllerDefined, "No controller defined");
            return this;
        }

        public MultiblockLayout build() {
            return new MultiblockLayout(this);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < layers.size(); i++) {
                ProtoLayer layer = layers.get(i);
                builder.append("Layer ").append(i).append('\n');
                builder.append(layer);
            }
            return builder.toString();
        }
    }

    public record Layer(BlockDefinition[][] layer) {
        private static Layer build(ProtoLayer protoLayer, Map<Character, BlockDefinition> definition) {
            BlockDefinition[][] newLayer = new BlockDefinition[protoLayer.layer().length][];
            for (int i = 0; i < protoLayer.layer().length; i++) {
                char[] layer = protoLayer.layer()[i];
                newLayer[i] = new BlockDefinition[layer.length];
                for (int j = 0; j < layer.length; j++) {
                    if (layer[j] == ' ') {
                        newLayer[i][j] = BlockDefinition.EMPTY;
                    } else {
                        newLayer[i][j] = definition.get(layer[j]);
                    }
                }
            }

            return new Layer(newLayer);
        }
    }

    public record BlockWithCount(Holder<Block> block, int count) {

    }

    private record ProtoLayer(char[][] layer, boolean empty) {
        public ProtoLayer(List<String> layer) {
            char[][] newLayer = new char[layer.size()][];
            boolean empty = true;

            for (int i = 0; i < layer.size(); i++) {
                String row = layer.get(i);
                if (!row.isBlank()) {
                    empty = false;
                }
                newLayer[i] = row.toCharArray();
            }
            this(newLayer, empty);
        }

        @Override
        public @NonNull String toString() {
            StringBuilder builder = new StringBuilder();
            for (char[] layer : layer) {
                for (char block : layer) {
                    builder.append("[").append(block).append("]");
                }
                builder.append('\n');
            }
            return builder.toString();
        }
    }

    public record BlockDefinition(Predicate<Block> validBlocks, Holder<Block> defaultBlock) {
        public static final BlockDefinition EMPTY = new BlockDefinition(block -> true, Blocks.AIR.builtInRegistryHolder());

        public boolean isEmpty() {
            return defaultBlock.value() == Blocks.AIR;
        }

    }

    public static class LayoutBuilder {
        private final MultiblockLayout.Builder inner;
        private final List<String> curLayer = new ArrayList<>();
        private boolean curLayerEmpty = true;

        private LayoutBuilder(MultiblockLayout.Builder inner) {
            this.inner = inner;
        }

        public MultiblockLayout.LayoutBuilder pattern(String pattern) {
            if (!pattern.isEmpty()) {
                this.curLayer.add(pattern);
                for (int i = 0; i < pattern.length(); i++) {
                    this.inner.blocks.add(pattern.charAt(i));
                }
                this.curLayerEmpty = false;
            }
            return this;
        }

        public MultiblockLayout.LayoutBuilder nextLayer() {
            if (!this.curLayerEmpty) {
                this.inner.layers.add(new ProtoLayer(this.curLayer));
                this.curLayer.clear();
            }
            return this;
        }

        public MultiblockLayout.LayoutBuilder repeatX(int repeats) {
            if (!this.curLayerEmpty) {
                String last = this.curLayer.getLast();
                if (!last.isEmpty()) {
                    this.curLayer.set(this.curLayer.size() - 1, last.repeat(repeats));
                } else {
                    throw new IllegalStateException("Cannot repeat multiblock layout on x-axis, since prev pattern is empty");
                }
            } else {
                throw new IllegalStateException("Cannot repeat multiblock layout on x-axis, since current layer is empty");
            }
            return this;
        }

        public MultiblockLayout.LayoutBuilder repeatZ(int repeats) {
            if (!this.curLayerEmpty) {
                String last = this.curLayer.getLast();
                if (!last.isEmpty()) {
                    for (int i = 0; i < repeats - 1; i++) {
                        this.curLayer.add(last);
                    }
                } else {
                    throw new IllegalStateException("Cannot repeat multiblock layout on z-axis, since prev pattern is empty");
                }
            } else {
                throw new IllegalStateException("Cannot repeat multiblock layout on z-axis, since current layer is empty");
            }
            return this;
        }

        public MultiblockLayout.LayoutBuilder repeatY(int repeats) {
            if (!this.curLayerEmpty) {
                List<String> layer = List.copyOf(this.curLayer);
                for (int i = 0; i < repeats - 1; i++) {
                    this.nextLayer();
                    this.curLayer.addAll(layer);
                }
            } else {
                throw new IllegalStateException("Cannot repeat multiblock layout on y-axis, since current layer is empty");
            }
            return this;
        }

    }

    public static class DefinitionBuilder {
        private final MultiblockLayout.Builder inner;
        private boolean controllerDefined = false;

        private DefinitionBuilder(MultiblockLayout.Builder inner) {
            Preconditions.checkArgument(!inner.layers.isEmpty(), "Pattern needs to be constructed before the definition");
            this.inner = inner;
        }

        public DefinitionBuilder define(char key, Predicate<Block> validBlocks, Holder<Block> defaultBlock, boolean controller) {
            Preconditions.checkArgument(this.inner.blocks.contains(key), "Definition for char " + key + " that is not part of any pattern");
            Preconditions.checkArgument(!(controller && this.controllerDefined), "Definition has multiple controllers");
            if (controller) {
                this.controllerDefined = true;
            }
            this.inner.definition.put(key, new BlockDefinition(validBlocks, defaultBlock));
            return this;
        }

        public DefinitionBuilder define(char key, Block... validBlocks) {
            Preconditions.checkArgument(validBlocks.length != 0, "No valid blocks provided");
            return this.define(key, block -> {
                for (Block validBlock : validBlocks) {
                    if (block == validBlock) {
                        return true;
                    }
                }
                return false;
            }, validBlocks[0].builtInRegistryHolder(), false);
        }

        public DefinitionBuilder define(char key, Holder<Block> block, boolean controller) {
            return this.define(key, b -> b == block.value(), block, controller);
        }

        public DefinitionBuilder define(char key, Holder<Block> block) {
            return this.define(key, b -> b == block.value(), block, false);
        }

    }

}
