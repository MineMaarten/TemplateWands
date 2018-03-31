package com.minemaarten.templatewands.templates.ingredients.providers;

import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IInputIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderJukeBox implements IIngredientProvider{
    @Override
    public EventPriority getPriority(){
        return EventPriority.HIGHEST;
    }

    @Override
    public void addIngredients(BlockContext context, IInputIngredientList ingredients){
        if(context.block == Blocks.JUKEBOX && context.te instanceof TileEntityJukebox) {
            ItemStack record = ((TileEntityJukebox)context.te).getRecord();
            if(!record.isEmpty()) {
                ingredients.addItemStackExact(record);

                //Reset so other ingredient providers can append the inventory block itself
                ((IngredientList)ingredients).resetChanged(); //TODO cleaner way to allow multiple IIngredientProviders
            }
        }
    }
}
