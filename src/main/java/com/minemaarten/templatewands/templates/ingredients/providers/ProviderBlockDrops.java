package com.minemaarten.templatewands.templates.ingredients.providers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderBlockDrops implements IIngredientProvider{

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
    }

}
