package com.minemaarten.templatewands.templates.ingredients.providers.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderSilkTouch implements IBlockIngredientProvider{

    @Override
    public EventPriority getPriority(){
        return EventPriority.LOW;
    }

    @Override
    public void addIngredients(BlockContext context, IIngredientList ingredients){
        if(context.block.canSilkHarvest(context.world, context.pos, context.state, context.player)) {
            ingredients.addItemStack(getSilkTouchBlock(context.block, context.state));
        }
    }

    private static ItemStack getSilkTouchBlock(Block block, IBlockState state){
        Item item = Item.getItemFromBlock(block);
        if(item == Items.AIR) {
            return ItemStack.EMPTY;
        } else {
            int meta = item.getHasSubtypes() ? block.getMetaFromState(state) : 0;
            return new ItemStack(item, 1, meta);
        }
    }
}
