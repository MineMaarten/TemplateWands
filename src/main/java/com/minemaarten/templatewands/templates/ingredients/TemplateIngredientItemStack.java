package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.item.ItemStack;

public class TemplateIngredientItemStack extends TemplateIngredient<ItemStack>{

    private final ItemStack stack;

    public TemplateIngredientItemStack(ItemStack stack){
        super(ItemStack.class);
        this.stack = stack;
    }

    @Override
    public boolean appliesTo(ItemStack ingredient){
        return stack.isItemEqual(ingredient);
    }

}
