package com.minemaarten.templatewands.api.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IIngredientList{
    /**
     * A stack matched by NBT.
     * @param stack
     */
    void addItemStack(ItemStack stack);

    void addFluidStack(FluidStack fluid);

    /**
     * Mark changed, while not adding an ingredient. This basically indicates the block is free (the case of one side of a bed/door/...)
     */
    void markChanged();
}
