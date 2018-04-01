package com.minemaarten.templatewands.templates.ingredients.providers.blocks;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import com.minemaarten.templatewands.api.TemplateWands;
import com.minemaarten.templatewands.api.ingredients.IBlockIngredientProvider;
import com.minemaarten.templatewands.api.ingredients.IIngredientList;
import com.minemaarten.templatewands.api.util.BlockContext;

@TemplateWands
public class ProviderFluidSource implements IBlockIngredientProvider{

    @Override
    public EventPriority getPriority(){
        return EventPriority.LOW;
    }

    @Override
    public void addIngredients(BlockContext context, IIngredientList ingredients){
        Fluid fluid = FluidRegistry.lookupFluidForBlock(context.block);
        if(fluid != null) {
            ingredients.addFluidStack(new FluidStack(fluid, 1000));
        }
    }

}
