package com.minemaarten.templatewands.templates.ingredients.providers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderShearable implements IIngredientProvider{

    @Override
    public EventPriority getPriority(){
        return EventPriority.LOW;
    }

    @Override
    public void addIngredients(BlockContext context, IInputIngredientList ingredients){
        if(context.block instanceof IShearable) {
            IShearable shearable = (IShearable)context.block;
            if(shearable.isShearable(ItemStack.EMPTY, context.world, context.pos)) {
                for(ItemStack stack : shearable.onSheared(ItemStack.EMPTY, context.world, context.pos, 0)) {
                    ingredients.addItemStackExact(stack);
                }
            }
        }
    }
}
