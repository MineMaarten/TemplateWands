package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TemplateIngredientItemStack extends TemplateIngredient<ItemStack>{

    /**
     * Use a separate int as ItemStack's item count is a byte when serialized.
     */
    private int amount;

    public TemplateIngredientItemStack(ItemStack stack){
        super(stack.copy());
        amount = stack.getCount();
    }

    public TemplateIngredientItemStack(NBTTagCompound tag){
        this(new ItemStack(tag));
        amount = tag.getInteger("amount");
    }

    @Override
    public boolean appliesTo(ItemStack ingredient){
        return this.ingredient.isItemEqual(ingredient);
    }

    @Override
    public void addAmount(int amount){
        this.amount += amount;
    }

    @Override
    public int getAmount(){
        return amount;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        ingredient.writeToNBT(tag);
        tag.setInteger("amount", amount);
    }

    @Override
    protected EnumIngredientType getIngredientType(){
        return EnumIngredientType.ITEM_STACK;
    }

    @Override
    public String toString(){
        return amount + "x " + ingredient.getDisplayName();
    }
}
