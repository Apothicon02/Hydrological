package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.configurations.ColumnConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class ColumnFeature extends Feature<ColumnConfiguration> {
    public ColumnFeature(Codec<ColumnConfiguration> p_159960_) {
        super(p_159960_);
    }

    public boolean isMaterialBaseOrLava(BlockState state) {
        return state.is(Blocks.DRIPSTONE_BLOCK) || state.is(BlockTags.DRIPSTONE_REPLACEABLE) || state.is(BlockTags.TERRACOTTA) || state.is(BlockTags.ICE) || state.is(Blocks.SCULK);
    }

    public boolean place(FeaturePlaceContext<ColumnConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        ColumnConfiguration config = context.config();
        RandomSource random = context.random();
        if (!DripstoneUtils.isEmptyOrWater(level, origin)) {
            return false;
        } else {
            Optional<Column> $$5 = Column.scan(level, origin, config.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, this::isMaterialBaseOrLava);
            if (!$$5.isEmpty() && $$5.get() instanceof Column.Range) {
                Column.Range $$6 = (Column.Range)$$5.get();
                if ($$6.height() < 4) {
                    return false;
                } else {
                    int $$7 = (int)((float)$$6.height() * config.maxColumnRadiusToCaveHeightRatio);
                    int $$8 = Mth.clamp($$7, config.columnRadius.getMinValue(), config.columnRadius.getMaxValue());
                    int $$9 = Mth.randomBetweenInclusive(random, config.columnRadius.getMinValue(), $$8);
                    ColumnFeature.LargeDripstone level0 = makeDripstone(origin.atY($$6.ceiling() - 1), false, random, $$9, config.stalactiteBluntness, config.heightScale);
                    ColumnFeature.LargeDripstone level1 = makeDripstone(origin.atY($$6.floor() + 1), true, random, $$9, config.stalagmiteBluntness, config.heightScale);
                    ColumnFeature.WindOffsetter level3;
                    if (level0.isSuitableForWind(config) && level1.isSuitableForWind(config)) {
                        level3 = new ColumnFeature.WindOffsetter(origin.getY(), random, config.windSpeed);
                    } else {
                        level3 = ColumnFeature.WindOffsetter.noWind();
                    }

                    boolean level4 = level0.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, level3);
                    boolean level5 = level1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, level3);
                    if (level4) {
                        level0.placeBlocks(level, random, level3, config, random);
                    }

                    if (level5) {
                        level1.placeBlocks(level, random, level3, config, random);
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private static ColumnFeature.LargeDripstone makeDripstone(BlockPos p_225139_, boolean p_225140_, RandomSource p_225141_, int p_225142_, FloatProvider p_225143_, FloatProvider p_225144_) {
        return new ColumnFeature.LargeDripstone(p_225139_, p_225140_, p_225142_, (double)p_225143_.sample(p_225141_), (double)p_225144_.sample(p_225141_));
    }

    static final class LargeDripstone {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargeDripstone(BlockPos p_197116_, boolean p_197117_, int p_197118_, double p_197119_, double p_197120_) {
            this.root = p_197116_;
            this.pointingUp = p_197117_;
            this.radius = p_197118_;
            this.bluntness = p_197119_;
            this.scale = p_197120_;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        private int getMinY() {
            return this.pointingUp ? this.root.getY() : this.root.getY() - this.getHeight();
        }

        private int getMaxY() {
            return !this.pointingUp ? this.root.getY() : this.root.getY() + this.getHeight();
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel p_159990_, ColumnFeature.WindOffsetter p_159991_) {
            while(this.radius > 1) {
                BlockPos.MutableBlockPos origin = this.root.mutable();
                int config = Math.min(10, this.getHeight());

                for(int random = 0; random < config; ++random) {
                    if (p_159990_.getBlockState(origin).is(Blocks.LAVA)) {
                        return false;
                    }

                    if (DripstoneUtils.isCircleMostlyEmbeddedInStone(p_159990_, p_159991_.offset(origin), this.radius)) {
                        this.root = origin;
                        return true;
                    }

                    origin.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeightAtRadius(float p_159988_) {
            return (int)DripstoneUtils.getDripstoneHeight((double)p_159988_, (double)this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel p_225146_, RandomSource p_225147_, ColumnFeature.WindOffsetter p_225148_, ColumnConfiguration configuration, RandomSource randomSource) {
            for(int config = -this.radius; config <= this.radius; ++config) {
                for(int random = -this.radius; random <= this.radius; ++random) {
                    float $$5 = Mth.sqrt((float)(config * config + random * random));
                    if (!($$5 > (float)this.radius)) {
                        int $$6 = this.getHeightAtRadius($$5);
                        if ($$6 > 0) {
                            if ((double)p_225147_.nextFloat() < 0.2) {
                                $$6 = (int)((float)$$6 * Mth.randomBetween(p_225147_, 0.8F, 1.0F));
                            }

                            BlockPos.MutableBlockPos $$7 = this.root.offset(config, 0, random).mutable();
                            boolean $$8 = false;
                            int $$9 = this.pointingUp ? p_225146_.getHeight(Heightmap.Types.WORLD_SURFACE_WG, $$7.getX(), $$7.getZ()) : Integer.MAX_VALUE;

                            for(int level0 = 0; level0 < $$6 && $$7.getY() < $$9; ++level0) {
                                BlockPos level1 = p_225148_.offset($$7);
                                BlockState existing = p_225146_.getBlockState(level1);
                                if (DripstoneUtils.isEmptyOrWaterOrLava(p_225146_, level1)) {
                                    $$8 = true;
                                    BlockState level2 = configuration.material.getState(randomSource, level1);
                                    if (level2.is(Blocks.MANGROVE_ROOTS) && level1.getY() < -49) {
                                        level2 = Blocks.MUDDY_MANGROVE_ROOTS.defaultBlockState();
                                    }
                                    p_225146_.setBlock(level1, level2, 2);
                                } else if ($$8 && (existing.is(BlockTags.BASE_STONE_OVERWORLD) || existing.is(BlockTags.TERRACOTTA))) {
                                    break;
                                }

                                $$7.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }

        }

        boolean isSuitableForWind(ColumnConfiguration p_159997_) {
            return this.radius >= p_159997_.minRadiusForWind && this.bluntness >= (double)p_159997_.minBluntnessForWind;
        }
    }

    private static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3 windSpeed;

        WindOffsetter(int p_225150_, RandomSource p_225151_, FloatProvider p_225152_) {
            this.originY = p_225150_;
            float config = p_225152_.sample(p_225151_);
            float random = Mth.randomBetween(p_225151_, 0.0F, 3.1415927F);
            this.windSpeed = new Vec3((double)(Mth.cos(random) * config), 0.0, (double)(Mth.sin(random) * config));
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        static ColumnFeature.WindOffsetter noWind() {
            return new ColumnFeature.WindOffsetter();
        }

        BlockPos offset(BlockPos p_160009_) {
            if (this.windSpeed == null) {
                return p_160009_;
            } else {
                int level = this.originY - p_160009_.getY();
                Vec3 origin = this.windSpeed.scale((double)level);
                return p_160009_.offset(Mth.floor(origin.x), 0, Mth.floor(origin.z));
            }
        }
    }
}
