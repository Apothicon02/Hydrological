package com.Apothic0n.Hydrological.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class BlockItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void appendHoverText(ItemStack item, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag tip, CallbackInfo ci) {
        if (item.is(Items.OAK_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.DARK_OAK_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.BIRCH_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
            list.accept(Component.translatable("block.minecraft.gravel").withStyle(ChatFormatting.DARK_GRAY));
        } else if (item.is(Items.SPRUCE_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.snow").withStyle(ChatFormatting.WHITE));
            list.accept(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
            list.accept(Component.translatable("block.minecraft.gravel").withStyle(ChatFormatting.DARK_GRAY));
        } else if (item.is(Items.ACACIA_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.coarse_dirt").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.JUNGLE_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
            list.accept(Component.translatable("block.minecraft.sand").withStyle(ChatFormatting.YELLOW));
        } else if (item.is(Items.CHERRY_SAPLING)) {
            list.accept(Component.translatable("block.minecraft.podzol").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.MANGROVE_PROPAGULE)) {
            list.accept(Component.translatable("block.minecraft.muddy_mangrove_roots").withStyle(ChatFormatting.GOLD));
        } else if (item.is(Items.RED_MUSHROOM)) {
            list.accept(Component.translatable("block.minecraft.mycelium").withStyle(ChatFormatting.DARK_PURPLE));
        } else if (item.is(Items.BROWN_MUSHROOM)) {
            list.accept(Component.translatable("block.minecraft.mycelium").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
