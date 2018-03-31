package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TemplateIngredientItemStackExact extends TemplateIngredientItemStack{

    public TemplateIngredientItemStackExact(ItemStack stack){
        super(stack);
    }

    public TemplateIngredientItemStackExact(NBTTagCompound tag){
        super(tag);
    }

    @Override
    public boolean appliesTo(ItemStack ingredient){
        return super.appliesTo(ingredient) && ItemStack.areItemStackTagsEqual(this.ingredient, ingredient);
    }

    @Override
    protected EnumIngredientType getIngredientType(){
        return EnumIngredientType.ITEM_STACK_EXACT;
    }
}
