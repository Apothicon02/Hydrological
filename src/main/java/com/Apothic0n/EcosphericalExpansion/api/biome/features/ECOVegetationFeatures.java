package com.Apothic0n.EcosphericalExpansion.api.biome.features;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;

import java.util.List;

public class ECOVegetationFeatures {
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_LUSH_JUNGLE = FeatureUtils.register("trees_lush_jungle", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.TWISTED_OAK_CHECKED, 0.06F), new WeightedPlacedFeature(TreePlacements.JUNGLE_BUSH, 0.8F)), ECOTreePlacements.BRANCHING_JUNGLE_TREE_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_LUSH_OAK = FeatureUtils.register("trees_lush_oak", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.OAK_BEES_002, 0.03F), new WeightedPlacedFeature(TreePlacements.OAK_CHECKED, 0.07F), new WeightedPlacedFeature(ECOTreePlacements.TOWERING_OAK_CHECKED, 0.9F)), ECOTreePlacements.BRANCHING_OAK_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_MEGA_ACACIA = FeatureUtils.register("trees_mega_acacia", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.BRANCHING_OAK_CHECKED, 0.01F), new WeightedPlacedFeature(ECOTreePlacements.MULTI_ACACIA_CHECKED, 0.25F), new WeightedPlacedFeature(ECOTreePlacements.MEGA_ACACIA_CHECKED, 0.7F)), ECOTreePlacements.AZALEA_BUSH_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_MEGA_SWAMP = FeatureUtils.register("trees_mega_swamp", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.BRANCHING_OAK_CHECKED, 0.01F), new WeightedPlacedFeature(ECOTreePlacements.MULTI_SWAMP_CHECKED, 0.25F), new WeightedPlacedFeature(ECOTreePlacements.MEGA_SWAMP_BIG_CHECKED, 0.11F), new WeightedPlacedFeature(ECOTreePlacements.MEGA_SWAMP_SMALL_CHECKED, 0.6F)), ECOTreePlacements.MULTI_ACACIA_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_THIN_SPRUCE = FeatureUtils.register("trees_thin_spruce", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.OAK_CHECKED, 0.08F), new WeightedPlacedFeature(ECOTreePlacements.TOWERING_SPRUCE_CHECKED, 0.52F), new WeightedPlacedFeature(ECOTreePlacements.OAK_BUSH_CHECKED, 0.3F)), ECOTreePlacements.OAK_BUSH_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_AZALEA = FeatureUtils.register("trees_azalea", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.MEGA_AZALEA_CHECKED, 0.1F), new WeightedPlacedFeature(ECOTreePlacements.AZALEA_BUSH_CHECKED, 0.6F)), TreePlacements.FANCY_OAK_BEES_0002)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_BIG_DARK_OAK = FeatureUtils.register("trees_big_dark_oak", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.TILTED_DARK_OAK_CHECKED, 0.6F), new WeightedPlacedFeature(ECOTreePlacements.SHORT_DARK_OAK_CHECKED, 0.5F)), TreePlacements.FANCY_OAK_BEES_0002)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_SMALL_DARK_OAK = FeatureUtils.register("trees_small_dark_oak", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.TWISTED_OAK_CHECKED, 0.05F), new WeightedPlacedFeature(ECOTreePlacements.SHORT_DARK_OAK_CHECKED, 0.9F)), ECOTreePlacements.SHORT_DARK_OAK_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_NORMAL_DARK_OAK = FeatureUtils.register("trees_normal_dark_oak", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.SHORT_DARK_OAK_CHECKED, 0.1F), new WeightedPlacedFeature(ECOTreePlacements.NORMAL_DARK_OAK_CHECKED, 0.9F)), ECOTreePlacements.NORMAL_DARK_OAK_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_TALL_BIRCH = FeatureUtils.register("trees_tall_birch", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.TILTED_TALL_BIRCH_CHECKED, 0.3F), new WeightedPlacedFeature(ECOTreePlacements.BIG_TALL_BIRCH_CHECKED, 0.7F)), ECOTreePlacements.NORMAL_TALL_BIRCH_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_TALL_MUSHROOMS = FeatureUtils.register("trees_tall_mushrooms", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.TILTED_TALL_MUSHROOM_CHECKED, 0.3F), new WeightedPlacedFeature(ECOTreePlacements.BIG_TALL_MUSHROOM_CHECKED, 0.7F)), ECOTreePlacements.NORMAL_TALL_MUSHROOM_CHECKED)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> MISC_AMETHYSTS = FeatureUtils.register("misc_amethysts", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ECOTreePlacements.MISC_AMETHSYT_GEODE, 0.02F), new WeightedPlacedFeature(ECOTreePlacements.DEEPSLATE_BLOCK_STACK, 0.03F), new WeightedPlacedFeature(ECOTreePlacements.COPPER_DEEPSLATE_STACK, 0.01F), new WeightedPlacedFeature(ECOTreePlacements.MISC_AMETHYST_CLUSTER, 0.15F), new WeightedPlacedFeature(ECOTreePlacements.MISC_AMETHYST_LARGE, 0.2F), new WeightedPlacedFeature(ECOTreePlacements.MISC_AMETHYST_NORMAL, 0.24F), new WeightedPlacedFeature(ECOTreePlacements.MISC_AMETHYST_SMALL, 0.35F)), ECOTreePlacements.MISC_AMETHYST_SMALL)));
    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> OAK_BUSH = FeatureUtils.register("oak_bush", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(TreePlacements.OAK_BEES_002, 0.03F), new WeightedPlacedFeature(ECOTreePlacements.OAK_BUSH_CHECKED, 0.97F)), ECOTreePlacements.OAK_BUSH_CHECKED)));
    public static final ConfiguredFeature<?, ?> TALL_CACTI = FeatureUtils.register("tall_cacti", Feature.RANDOM_PATCH.configured(FeatureUtils.simpleRandomPatchConfiguration(10, Feature.BLOCK_COLUMN.configured(BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 7), BlockStateProvider.simple(Blocks.CACTUS))).placed(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), BlockPos.ZERO)))))));
    public static final ConfiguredFeature<RandomPatchConfiguration, ?> BASIC_FLOWERS = FeatureUtils.register("basic_flowers", Feature.FLOWER.configured(grassPatch(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.POPPY.defaultBlockState(), 5).add(Blocks.DANDELION.defaultBlockState(), 3).add(Blocks.ORANGE_TULIP.defaultBlockState(), 2).add(Blocks.PINK_TULIP.defaultBlockState(), 1)), 64)));

    private static RandomPatchConfiguration grassPatch(BlockStateProvider state, int tries) {
        return FeatureUtils.simpleRandomPatchConfiguration(tries, Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(state)).onlyWhenEmpty());
    }
}