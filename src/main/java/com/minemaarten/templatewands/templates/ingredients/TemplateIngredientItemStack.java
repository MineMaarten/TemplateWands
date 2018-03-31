package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TemplateIngredientItemStack extends TemplateIngredient<ItemStack>{

    public TemplateIngredientItemStack(ItemStack stack){
        super(stack.copy());
    }

    public TemplateIngredientItemStack(NBTTagCompound tag){
        this(new ItemStack(tag));
    }

    @Override
    public boolean appliesTo(ItemStack ingredient){
        return this.ingredient.isItemEqual(ingredient);
    }

    @Override
    public void addAmount(int amount){
        ingredient.grow(amount);
    }

    @Override
    public int getAmount(){
        return ingredient.getCount();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        ingredient.writeToNBT(tag);
    }

    @Override
    protected EnumIngredientType getIngredientType(){
        return EnumIngredientType.ITEM_STACK;
    }

    @Override
    public String toString(){
        return "ItemStack: " + ingredient.toString();
    }
}
