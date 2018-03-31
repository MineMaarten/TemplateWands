package com.minemaarten.templatewands.api.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IIngredientList{
    void addItemStack(ItemStack stack);

    void addFluidStack(FluidStack fluid);
}
