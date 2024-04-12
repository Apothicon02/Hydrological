package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hydrological.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level pLevel = event.getLevel();
        BlockPos pPos = event.getHitVec().getBlockPos();
        BlockState pBlock = pLevel.getBlockState(pPos);
        ItemStack pStack = event.getItemStack();
        Player player = event.getEntity();
        if (!pLevel.isClientSide) { //Runs stuff on the server every time a player right-clicks a block
            if (pStack.getItem() == Items.GLOW_INK_SAC && pBlock.getBlock() == Blocks.AMETHYST_CLUSTER) {
                pLevel.setBlock(pPos, HydrolBlocks.GLOWING_AMETHYST.get().withPropertiesOf(pBlock), 2);
                float f = Mth.randomBetween(pLevel.random, 0.8F, 1.2F);
                pLevel.playSound((Player) null, pPos, SoundEvents.DOLPHIN_EAT, SoundSource.BLOCKS, 1.0F, f);
                player.swing(event.getHand(), true);
                if (!player.isCreative()) {
                    pStack.setCount(pStack.getCount() - 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        boolean cancel = false;
        if (event.getLevel() instanceof ServerLevel level && !level.dimensionType().hasCeiling()) {
            for (int x = 0; x < 128000; x = x+128) {
                for (int z = 0; z < 128000; z = z+128) {
                    BlockPos spawnPos = new BlockPos(x, 63, z);
                    Holder<Biome> center = level.getBiome(spawnPos);
                    if (!center.is(BiomeTags.IS_OCEAN) && !center.toString().contains("caldera") && !center.toString().contains("swamp")) {
                        Holder<Biome> northEast = level.getBiome(spawnPos.north(24).east(24));
                        if (!northEast.is(BiomeTags.IS_OCEAN) && !northEast.toString().contains("caldera") && !northEast.toString().contains("swamp")) {
                            Holder<Biome> northWest = level.getBiome(spawnPos.north(24).west(24));
                            if (!northWest.is(BiomeTags.IS_OCEAN) && !northWest.toString().contains("caldera") && !northWest.toString().contains("swamp")) {
                                Holder<Biome> southEast = level.getBiome(spawnPos.south(24).east(24));
                                if (!southEast.is(BiomeTags.IS_OCEAN) && !southEast.toString().contains("caldera") && !southEast.toString().contains("swamp")) {
                                    Holder<Biome> southWest = level.getBiome(spawnPos.south(24).west(24));
                                    if (!southWest.is(BiomeTags.IS_OCEAN) && !southWest.toString().contains("caldera") && !southWest.toString().contains("swamp")) {
                                        ChunkPos chunkPos = level.getChunkAt(spawnPos).getPos();
                                        level.setChunkForced(chunkPos.x, chunkPos.z, true);
                                        event.getSettings().setSpawn(spawnPos.atY(level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z)), 0.0F);
                                        cancel = true;
                                        x = 128000;
                                        z = 128000;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        event.setCanceled(cancel);
    }
}
