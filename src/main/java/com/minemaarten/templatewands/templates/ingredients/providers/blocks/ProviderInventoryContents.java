package com.minemaarten.templatewands.templates.ingredients.providers.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;
import com.minemaarten.templatewands.templates.ingredients.providers.IngredientList;

@TemplateWands
public class ProviderInventoryContents implements IBlockIngredientProvider{

    @Override
    public EventPriority getPriority(){
        return EventPriority.NORMAL;
    }

    @Override
    public void addIngredients(BlockContext context, IInputIngredientList ingredients){
        if(context.te != null) {
            IItemHandler handler = context.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if(handler != null) {
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if(!stack.isEmpty()) {
                        ingredients.addItemStackExact(stack);
                    }
                }

                //Reset so other ingredient providers can append the inventory block itself
                ((IngredientList)ingredients).resetChanged(); //TODO cleaner way to allow multiple IIngredientProviders
            }
        }
    }

}
