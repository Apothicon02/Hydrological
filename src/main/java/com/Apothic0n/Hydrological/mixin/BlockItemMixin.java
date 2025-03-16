package com.Apothic0n.Hydrological.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow public abstract Block getBlock();

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void appendHoverText(ItemStack item, Item.TooltipContext context, List<Component> list, TooltipFlag tip, CallbackInfo ci) {
        if (item.is(Items.OAK_SAPLING)) {
            list.add(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.DARK_OAK_SAPLING)) {
            list.add(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.BIRCH_SAPLING)) {
            list.add(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
            list.add(Component.translatable("block.minecraft.gravel").withStyle(ChatFormatting.DARK_GRAY));
        } else if (item.is(Items.SPRUCE_SAPLING)) {
            list.add(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.snow").withStyle(ChatFormatting.WHITE));
            list.add(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
            list.add(Component.translatable("block.minecraft.gravel").withStyle(ChatFormatting.DARK_GRAY));
        } else if (item.is(Items.ACACIA_SAPLING)) {
            list.add(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.JUNGLE_SAPLING)) {
            list.add(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
        } else if (item.is(Items.CHERRY_SAPLING)) {
            list.add(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.MANGROVE_PROPAGULE)) {
            list.add(Component.translatable("block.minecraft.muddy_mangrove_roots").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.RED_MUSHROOM)) {
            list.add(Component.translatable("block.minecraft.mycelium").withStyle(ChatFormatting.DARK_PURPLE));
        } else if (item.is(Items.BROWN_MUSHROOM)) {
            list.add(Component.translatable("block.minecraft.mycelium").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
