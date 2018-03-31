package com.minemaarten.templatewands.templates.ingredients.providers.blocks;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderBlockDrops implements IBlockIngredientProvider{

    private Set<Block> freeBlocks = new HashSet<>();

    public ProviderBlockDrops(){
        freeBlocks.add(Blocks.BED);
        freeBlocks.add(Blocks.DOUBLE_PLANT);
        freeBlocks.add(Blocks.PISTON_EXTENSION);
        freeBlocks.add(Blocks.PISTON_HEAD);
    }

    @Override
    public EventPriority getPriority(){
        return EventPriority.LOWEST;
    }

    @Override
    public void addIngredients(BlockContext context, IInputIngredientList ingredients){
        NonNullList<ItemStack> drops = NonNullList.create();
        context.block.getDrops(drops, context.world, context.pos, context.state, 0);
        for(ItemStack stack : drops) {
            ingredients.addItemStackExact(stack);
        }

        if(freeBlocks.contains(context.block) || context.block instanceof BlockDoor) {
            ingredients.markChanged();
        }
    }

}
