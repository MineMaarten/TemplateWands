package com.minemaarten.templatewands.templates.ingredients;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class TemplateIngredientFluidStack extends TemplateIngredient<FluidStack>{

    public TemplateIngredientFluidStack(FluidStack stack){
        super(stack.copy());
    }

    public TemplateIngredientFluidStack(NBTTagCompound tag){
        this(FluidStack.loadFluidStackFromNBT(tag));
    }

    @Override
    public boolean appliesTo(FluidStack ingredient){
        return this.ingredient.isFluidEqual(ingredient);
    }

    @Override
    public void addAmount(int amount){
        ingredient.amount += amount;
    }

    @Override
    public int getAmount(){
        return ingredient.amount;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        ingredient.writeToNBT(tag);
    }

    @Override
    protected EnumIngredientType getIngredientType(){
        return EnumIngredientType.FLUID_STACK;
    }

    @Override
    public String toString(){
        return ingredient.amount + "mB " + ingredient.getLocalizedName();
    }
}
