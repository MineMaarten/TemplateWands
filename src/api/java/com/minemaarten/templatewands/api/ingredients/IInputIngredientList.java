package com.minemaarten.templatewands.api.ingredients;

import net.minecraft.item.ItemStack;

public interface IInputIngredientList extends IIngredientList{
    /**
     * A stack matched by NBT.
     * @param stack
     */
    void addItemStackExact(ItemStack stack);
}
